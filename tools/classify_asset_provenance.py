#!/usr/bin/env python3
"""
Classify every asset this repo ships by where its bytes came from.

The repo is going public, and the rule for that is simple: nothing that is somebody
else's art may sit in the tree or in the jar. This decides, per file, which side of
that line it falls on -- by content, not by filename or by what a doc claims.

Three buckets come out:

  bridged   byte-identical to a file in one of the BOP reference trees. Glitchfiend's
            art under CC BY-NC-ND. Cannot ship; has to be bridged out of a copy the
            player supplies. Emits the asset-bridge manifest line.

  derived   not byte-identical to anything upstream, but built from upstream pixels --
            the composited ore variants. Cannot ship either: a derivative of a
            ND-licensed work is exactly what the licence bars sharing. These are
            generated at runtime instead, so they get a manifest entry for their
            *ingredients* rather than for themselves.

  own       Colin's, or BTA's own art he redrew. Ships.

Anything under assets/minecraft/ is Mojang's regardless of what matching says, and is
reported separately -- it can never ship and has no upstream BOP file to bridge from.

    python tools/classify_asset_provenance.py [--json out.json]

Writes a report to stdout. Reads only; changes nothing.
"""
from __future__ import annotations

import argparse
import hashlib
import json
import sys
from collections import defaultdict
from pathlib import Path

REPO = Path(__file__).resolve().parents[1]
SHIPPED = REPO / "src/main/resources/assets"

# Both reference trees, because the importer reads 1.2.1 first and falls back to 1.1.1,
# so a shipped file can legitimately match either. Order here is only for reporting --
# matching is on content hash and a file matches whichever tree holds those bytes.
REFERENCE_TREES = {
    "b1.7.3": REPO / "_reference/BiomesOPlenty-b1.7.3",
    "1.6.4": REPO / "_reference/BiomesOPlenty-1.6.4",
}

# Art built out of somebody else's pixels without being byte-identical to any single
# file, so hash matching cannot see it. Matched by path instead.
#
# Every one of these ships -- that is Colin's call, made knowing that CC BY-NC-ND calls
# this "Adapted Material" and restricts it more explicitly than a verbatim copy. They are
# listed here so the NOTICE can name them precisely rather than gesture at them.
DERIVED_PREFIXES = (
    # BOP's gem pixels composited onto BTA's rock. See BOPOreVariants.
    "betteroplenty/textures/block/ore/",
    # BOP's 128x128 particle sheets cut into the 8x8 frames BTA's atlas stitches, minus
    # dandelion, which is a whole-file copy and so hash-matches as verbatim instead.
    "betteroplenty/textures/particle/magictree_",
    "betteroplenty/textures/particle/pixietrail_",
    "betteroplenty/textures/particle/steam_",
    # BOP's amethyst bucket body carrying fills recoloured from BTA's own bucket art.
    "betteroplenty/textures/item/bucket_amethyst/",
    # BTA's own iron and steel buckets, recoloured for the fluids this mod registers.
    # BTA's art, not BOP's -- a different rights holder, same reasoning.
    "minecraft/textures/item/bucket_",
    # Redrawn from BTA's jar.png, because BTA has no glass bottle for these to reuse.
    "betteroplenty/textures/item/jar",
    "betteroplenty/textures/item/ambrosia.png",
    # The eight vanilla-block stand-ins: BTA's own mud_baked/grass/dirt/log pixels recoloured
    # (hues sampled from BTA's small mushrooms or chosen constants). tools/gen_vanilla_standins.py.
    # These replaced the MOJANG_VERBATIM bridge entries in 0.1.4 -- bridging them required a
    # Minecraft ~1.6-1.12 jar no player had, and the miss rendered the Badlands solid magenta.
    "betteroplenty/textures/block/hardened_clay",
    "betteroplenty/textures/block/mycel_",
    "betteroplenty/textures/block/mushroom_skin_",
)

# Mojang's, verbatim, sitting under this mod's own namespace. Empty since 0.1.4: the eight
# vanilla-block faces that used to live here (mycelium, giant-mushroom skins, hardened clay)
# stopped being Mojang's pixels at all -- they are generated from BTA's own art by
# tools/gen_vanilla_standins.py and ship as Adapted Material with the other DERIVED_PREFIXES
# entries. The map stays, empty, because the check below is the ratchet that keeps anyone from
# quietly re-copying Mojang art into the tree: name the path here and it is reported instead of
# shipping as "own".
MOJANG_VERBATIM: dict[str, str] = {}

ASSET_SUFFIXES = {".png", ".ogg", ".mcmeta"}


def digest(path: Path) -> str:
    return hashlib.sha256(path.read_bytes()).hexdigest()


def index_reference() -> dict[str, list[tuple[str, str]]]:
    """content hash -> [(tree label, path relative to that tree)]."""
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

        # Checked before the hash, because these are not in the BOP trees at all and would
        # otherwise fall through to "own" and quietly ship.
        if relative in MOJANG_VERBATIM:
            record["upstream"] = MOJANG_VERBATIM[relative]
            buckets["mojang"].append(record)
            continue

        matches = reference.get(digest(path))
        if matches:
            # Prefer the 1.2.1 (b1.7.3) tree in the report, mirroring the importer's own
            # precedence, so the manifest names the revision the port actually ported.
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

    # A bridged file whose upstream base name collides with another's would make a
    # base-name-keyed manifest ambiguous. Worth knowing before the manifest is written.
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
