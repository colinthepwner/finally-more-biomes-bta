#!/usr/bin/env python3
from __future__ import annotations

import argparse
import hashlib
import json
import sys
from collections import defaultdict
from pathlib import Path

REPO = Path(__file__).resolve().parents[1]
SHIPPED = REPO / "src/main/resources/assets"

REFERENCE_TREES = {
    "b1.7.3": REPO / "_reference/BiomesOPlenty-b1.7.3",
    "1.6.4": REPO / "_reference/BiomesOPlenty-1.6.4",
}

DERIVED_PREFIXES = (
    "betteroplenty/textures/block/ore/",
    "betteroplenty/textures/particle/magictree_",
    "betteroplenty/textures/particle/pixietrail_",
    "betteroplenty/textures/particle/steam_",
    "betteroplenty/textures/item/bucket_amethyst/",
    "minecraft/textures/item/bucket_",
    "betteroplenty/textures/item/jar",
    "betteroplenty/textures/item/ambrosia.png",
    "betteroplenty/textures/block/hardened_clay",
    "betteroplenty/textures/block/mycel_",
    "betteroplenty/textures/block/mushroom_skin_",
)

MOJANG_VERBATIM: dict[str, str] = {}

ASSET_SUFFIXES = {".png", ".ogg", ".mcmeta"}


def digest(path: Path) -> str:
    return hashlib.sha256(path.read_bytes()).hexdigest()


def index_reference() -> dict[str, list[tuple[str, str]]]:
    index: dict[str, list[tuple[str, str]]] = defaultdict(list)
    for label, root in REFERENCE_TREES.items():
        if not root.is_dir():
            sys.exit(f"reference tree missing: {root}\n"
                     "This has to run against the same trees the port was built from.")
        for path in root.rglob("*"):
            if path.is_file() and path.suffix.lower() in ASSET_SUFFIXES:
                index[digest(path)].append((label, str(path.relative_to(root)).replace("\\", "/")))
    return index


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("--json", type=Path, help="also write the full classification here")
    args = parser.parse_args()

    reference = index_reference()

    buckets: dict[str, list[dict]] = {"bridged": [], "derived": [], "own": [], "mojang": []}

    for path in sorted(SHIPPED.rglob("*")):
        if not path.is_file() or path.suffix.lower() not in ASSET_SUFFIXES:
            continue
        relative = str(path.relative_to(SHIPPED)).replace("\\", "/")
        record = {"path": relative, "bytes": path.stat().st_size}

        if relative in MOJANG_VERBATIM:
            record["upstream"] = MOJANG_VERBATIM[relative]
            buckets["mojang"].append(record)
            continue

        matches = reference.get(digest(path))
        if matches:
            matches.sort(key=lambda m: (m[0] != "b1.7.3", m[1]))
            record["upstream"] = matches[0][1]
            record["tree"] = matches[0][0]
            record["also_in"] = [m[1] for m in matches[1:]]
            buckets["bridged"].append(record)
        elif any(relative.startswith(p) for p in DERIVED_PREFIXES):
            buckets["derived"].append(record)
        else:
            buckets["own"].append(record)

    for name in ("bridged", "derived", "mojang", "own"):
        rows = buckets[name]
        total = sum(r["bytes"] for r in rows)
        print(f"{name:9} {len(rows):4} files  {total/1024:8.1f} KiB")

    print()
    print("--- own (ship) " + "-" * 56)
    for record in buckets["own"]:
        print(f"  {record['path']}")

    print()
    print("--- derived (ship, by Colin's call) " + "-" * 35)
    for record in buckets["derived"]:
        print(f"  {record['path']}")

    print()
    print("--- mojang verbatim (bridge from the player's own Minecraft jar) " + "-" * 6)
    for record in buckets["mojang"]:
        print(f"  {record['path']:60} <- {record['upstream']}")

    by_base: dict[str, list[str]] = defaultdict(list)
    for record in buckets["bridged"]:
        by_base[Path(record["upstream"]).name.lower()].append(record["path"])
    collisions = {k: v for k, v in by_base.items() if len(v) > 1}
    print()
    print(f"--- base-name collisions among bridged files: {len(collisions)} " + "-" * 20)
    for base, paths in sorted(collisions.items()):
        print(f"  {base}: {', '.join(paths)}")

    if args.json:
        args.json.write_text(json.dumps(buckets, indent=2), encoding="utf-8")
        print(f"\nwrote {args.json}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
