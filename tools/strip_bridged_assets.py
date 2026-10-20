#!/usr/bin/env python3
from __future__ import annotations

import argparse
import re
import sys
from pathlib import Path

REPO = Path(__file__).resolve().parents[1]
MANIFEST = REPO / "src/main/resources/assets/betteroplenty/asset-bridge.properties"
RESOURCES = REPO / "src/main/resources"


def bridged_paths() -> list[str]:
    if not MANIFEST.is_file():
        sys.exit(f"manifest not found: {MANIFEST}\nRun tools/gen_asset_bridge_manifest.py first.")

    text = re.sub(r"\\\s*\n\s*", "", MANIFEST.read_text(encoding="utf-8"))

    paths: list[str] = []
    for line in text.splitlines():
        line = line.strip()
        if not line or line.startswith("#"):
            continue
        _, _, value = line.partition("=")
        for path in value.split(","):
            path = path.strip()
            if path:
                paths.append(path)
    return paths


def main() -> int:
    parser = argparse.ArgumentParser()
    mode = parser.add_mutually_exclusive_group()
    mode.add_argument("--check", action="store_true",
                      help="exit non-zero if any bridged file is present; change nothing")
    mode.add_argument("--dry-run", action="store_true", help="list what would be deleted")
    args = parser.parse_args()

    paths = bridged_paths()
    present = [p for p in paths if (RESOURCES / p).is_file()]

    if args.check:
        if present:
            print(f"FAIL: {len(present)} file(s) the bridge is supposed to supply are in the tree.")
            print("These are Biomes O' Plenty's or Mojang's art and must not be committed or shipped:")
            for path in present[:40]:
                print(f"  {path}")
            if len(present) > 40:
                print(f"  ... and {len(present) - 40} more")
            print("\nRun: python tools/strip_bridged_assets.py")
            return 1
        print(f"OK: none of the {len(paths)} bridged files are in the tree.")
        return 0

    if not present:
        print(f"nothing to do -- none of the {len(paths)} bridged files are present.")
        return 0

    if args.dry_run:
        for path in present:
            print(f"would delete {path}")
        print(f"\n{len(present)} file(s)")
        return 0

    touched: set[Path] = set()
    for path in present:
        target = RESOURCES / path
        target.unlink()
        touched.add(target.parent)

    removed_dirs = 0
    for directory in sorted(touched, key=lambda d: len(d.parts), reverse=True):
        current = directory
        while current != RESOURCES and current.is_dir() and not any(current.iterdir()):
            current.rmdir()
            removed_dirs += 1
            current = current.parent

    print(f"deleted {len(present)} file(s)"
          + (f" and {removed_dirs} empty director{'y' if removed_dirs == 1 else 'ies'}" if removed_dirs else ""))
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
