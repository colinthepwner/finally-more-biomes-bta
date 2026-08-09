#!/usr/bin/env python3
"""
Generate assets/betteroplenty/asset-bridge.properties from the provenance classification.

The manifest is what BOPAssetBridge reads at runtime: it maps a file inside a copy of
Biomes O' Plenty that the *player* supplies onto the path this mod wants that art at,
inside a generated texture pack. This mod ships none of the files on the left.

    python tools/classify_asset_provenance.py --json build/provenance.json
    python tools/gen_asset_bridge_manifest.py --provenance build/provenance.json

Generated rather than hand-written because there are 450 of them and because a hand list
would drift the moment the importer's precedence changed. The classifier decides what is
upstream art by comparing bytes; this only reshapes that answer into a properties file.

Keys are the last two path segments, not the base name
-----------------------------------------------------
Mo' Creatures gets away with base names because mob skins have unique ones. BOP does not:
`blank.png` is in both `blocks/` and `items/`, `dandelion.png` is a block and a particle,
six armour textures share a name with their item icon, and four mobs each have a
`say.ogg` and a `hurt.ogg`. Thirteen collisions in all, every one resolved by keeping the
parent directory. The bridge still falls back to a base-name match for any key written
without a slash, which is what the Minecraft section below relies on.

One key can feed several paths
------------------------------
The right-hand side is a comma-separated list, for the places where one upstream file is
the source of several files here -- BOP draws one flower band and this port needs it as
both an armour layer and an inventory icon, and `bones_large.png` is byte-identical to
its medium and small siblings upstream.
"""
from __future__ import annotations

import argparse
import hashlib
import json
import re
from collections import defaultdict
from pathlib import Path

REPO = Path(__file__).resolve().parents[1]
DEFAULT_OUT = REPO / "src/main/resources/assets/betteroplenty/asset-bridge.properties"

# Written alongside the manifest: manifest key -> sha256 of the bytes this port was built and
# verified against.
#
# It exists because "first source to supply a file wins" is not good enough when the player hands
# over both BOP trees, which is what the README tells them to do. 71 textures exist in both trees
# with DIFFERENT bytes -- the shrub, every magic and pine leaf, most of the log hearts, amethyst ore
# -- and the port was built against 1.2.1's versions of them. Source order decided which one won,
# and source order came down to the archives' filenames: "BiomesOPlenty-BOP-1.1.2-1.6.4.zip" sorts
# before "BiomesOPlenty-BOP-b1.7.3.zip", so the older art won all 71 and the mod quietly rendered
# art it had never been checked against.
#
# With a hash per entry the bridge can prefer the exact bytes regardless of which archive it reads
# first, or what those archives are called. A non-matching file is still accepted when nothing
# better turns up, so an unusual BOP build degrades to "slightly different art" rather than to
# "missing texture".
DEFAULT_HASH_OUT = REPO / "src/main/resources/assets/betteroplenty/asset-bridge-hashes.properties"

PACK_PREFIX = "assets/"

# Vanilla art this port needs because the BOP features that place these blocks place
# *vanilla* blocks BTA 8.0 does not ship, so the block had to be ported alongside the
# feature and needs its vanilla face. Mojang's, so it is bridged rather than shipped, out
# of whatever Minecraft jar or resource pack the player already has.
#
# Keyed on base name alone: the directory moved from `textures/blocks/` to
# `textures/block/` in 1.13, and none of these names collides with anything in BOP.
MINECRAFT_SECTION = [
    ("mycelium_top.png", ["betteroplenty/textures/block/mycel_top.png"]),
    ("mycelium_side.png", ["betteroplenty/textures/block/mycel_side.png"]),
    ("mushroom_block_skin_brown.png", ["betteroplenty/textures/block/mushroom_skin_brown.png"]),
    ("mushroom_block_skin_red.png", ["betteroplenty/textures/block/mushroom_skin_red.png"]),
    ("mushroom_block_skin_stem.png", ["betteroplenty/textures/block/mushroom_skin_stem.png"]),
    ("hardened_clay.png", ["betteroplenty/textures/block/hardened_clay.png"]),
    ("hardened_clay_stained_orange.png", ["betteroplenty/textures/block/hardened_clay_stained_orange.png"]),
    ("hardened_clay_stained_red.png", ["betteroplenty/textures/block/hardened_clay_stained_red.png"]),
]

# Order and headings for the BOP half, by upstream directory. Anything not named here is
# emitted under "Other" rather than dropped, so a new upstream directory shows up in the
# diff instead of vanishing.
SECTIONS = [
    ("blocks", "Blocks", "BOP's block art. The bulk of the mod."),
    ("items", "Items", "Item icons, including the ones this port also uses as armour layers."),
    ("armor", "Armour layers",
     "Worn-armour overlays. Each is also the source of its own inventory icon, above --\n"
     "# one upstream file, two paths, which is what the comma-separated value is for."),
    ("mobs", "Mob skins", "The four flying mobs this port carries."),
    ("particles", "Particles",
     "Only dandelion, which BOP draws as a single 8x8 sprite. The other three particle\n"
     "# textures are 128x128 sheets that this mod cuts into frames itself, so they are\n"
     "# generated rather than bridged and do not appear here."),
    ("records", "Music discs",
     "The two BOP discs. Audio is the one asset class here that is neither code nor\n"
     "# texture, and a music track can carry authorship distinct from the mod's, so it is\n"
     "# bridged like everything else and called out separately in the NOTICE."),
    ("mob", "Mob sounds",
     "Keyed by mob directory, because all four mobs have a say.ogg and a hurt.ogg."),
]


def normalise(path: str) -> str:
    return path.replace("\\", "/")


def key_for(upstream: str) -> str:
    parts = normalise(upstream).split("/")
    return (parts[-2] + "/" + parts[-1]).lower()


def choose_upstream(record: dict) -> str:
    """
    Which upstream name to key a shipped file on, when several hold the same bytes.

    Prefer the candidate whose own base name matches the shipped file's -- `bones_medium`
    from `bones_medium.png` rather than from its byte-identical `bones_large.png` sibling.
    It reads better, and it stays correct if a later BOP redraws one of the three.
    """
    candidates = [record["upstream"]] + record.get("also_in", [])
    target = Path(record["path"]).name.lower()
    for candidate in candidates:
        if Path(normalise(candidate)).name.lower() == target:
            return candidate
    return record["upstream"]


def write_hashes(out: Path, keys, references: list[Path]) -> None:
    """
    Emit `manifest key = sha256` for the upstream file that key should resolve to.

    Reference trees are consulted in the order given, and the FIRST one that has the key wins --
    so the list must be ordered exactly as the port's own precedence is (1.2.1 first, 1.1.1 as
    fallback, per PORTING-STANDARD 9). That ordering is the whole content of this file: it is what
    lets the bridge pick 1.2.1's shrub at runtime out of two archives it cannot otherwise tell
    apart.

    A key with no upstream file is skipped rather than written blank. The Minecraft section has
    none, since vanilla art is not in these trees, and the bridge treats an absent hash as "no
    preference" -- which is the correct behaviour for a texture that legitimately comes from
    whatever Minecraft build the player owns.
    """
    index: list[dict[str, str]] = []
    for root in references:
        if not root.is_dir():
            continue
        tree: dict[str, str] = {}
        for path in root.rglob("*"):
            if path.is_file() and path.suffix.lower() in {".png", ".ogg"}:
                tree.setdefault((path.parent.name + "/" + path.name).lower(),
                                hashlib.sha256(path.read_bytes()).hexdigest())
        index.append(tree)

    lines = [
        "# Expected content hash per manifest key, as sha256.",
        "#",
        "# The bridge prefers a file whose bytes match, and falls back to any file when nothing",
        "# matches. This is what stops source ORDER deciding which art you get: 71 textures exist in",
        "# both classic BOP trees with different bytes, and without this the archive whose filename",
        "# happened to sort first supplied all 71.",
        "#",
        "# Absent key = no preference, which is correct for the vanilla-sourced textures.",
        "#",
        "# GENERATED by tools/gen_asset_bridge_manifest.py -- do not hand-edit.",
        "",
    ]
    written = 0
    for key in sorted(keys):
        for tree in index:
            if key in tree:
                lines.append(f"{key} = {tree[key]}")
                written += 1
                break

    out.parent.mkdir(parents=True, exist_ok=True)
    out.write_text("\n".join(lines) + "\n", encoding="utf-8")
    print(f"wrote {out}\n  {written} hashes")


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("--provenance", type=Path)
    parser.add_argument("--hashes-only", action="store_true",
                        help="leave the manifest alone and only regenerate the hash sidecar, "
                             "reading the keys out of the existing manifest. Use this once the "
                             "tree has been stripped, when a fresh classification would see no "
                             "upstream art and produce an empty manifest.")
    parser.add_argument("--out", type=Path, default=DEFAULT_OUT)
    parser.add_argument("--hash-out", type=Path, default=DEFAULT_HASH_OUT)
    parser.add_argument("--reference", type=Path, action="append", default=None,
                        help="a BOP reference tree root; repeatable. Used only to hash the "
                             "upstream file each key should resolve to.")
    args = parser.parse_args()

    references = args.reference or [
        REPO / "_reference/BiomesOPlenty-b1.7.3",
        REPO / "_reference/BiomesOPlenty-1.6.4",
    ]

    if args.hashes_only:
        text = re.sub(r"\\\s*\n\s*", "", args.out.read_text(encoding="utf-8"))
        keys = [line.split("=", 1)[0].strip()
                for line in text.splitlines()
                if line.strip() and not line.lstrip().startswith("#") and "=" in line]
        write_hashes(args.hash_out, keys, references)
        return 0

    if args.provenance is None:
        parser.error("--provenance is required unless --hashes-only is given")

    data = json.loads(args.provenance.read_text(encoding="utf-8"))

    # key -> set of pack paths. A set because several shipped files can resolve to the
    # same key and the same destination via different upstream candidates.
    mapping: dict[str, set[str]] = defaultdict(set)
    section_of: dict[str, str] = {}

    for record in data["bridged"]:
        # Frame timings, not art. Shipped instead -- see the NOTICE. Bridging a number
        # would leave every fluid static for anyone without the archive.
        if record["path"].endswith(".mcmeta"):
            continue
        upstream = normalise(choose_upstream(record))
        key = key_for(upstream)
        mapping[key].add(PACK_PREFIX + record["path"])
        # Grouped by upstream directory, except that mob sounds sit one level deeper --
        # sound/mob/<mob>/say.ogg -- so their parent is the mob's name, not a category.
        # Collapse those onto one heading rather than getting a section per mob.
        section_of[key] = "mob" if "/sound/" in upstream else Path(upstream).parent.name.lower()

    grouped: dict[str, list[str]] = defaultdict(list)
    for key in mapping:
        grouped[section_of[key]].append(key)

    out: list[str] = []
    out.append("# Asset bridge manifest -- Better O' Plenty")
    out.append("#")
    out.append("# Maps a file inside a player-supplied copy of Biomes O' Plenty onto the path this mod")
    out.append("# wants that art at. THIS MOD SHIPS NONE OF THE FILES ON THE LEFT. The bridge only ever")
    out.append("# reads a copy the player already has on disk; nothing is downloaded and nothing is")
    out.append("# redistributed.")
    out.append("#")
    out.append("#   <trailing path inside the archive> = <path inside the generated texture pack>")
    out.append("#")
    out.append("# Keys are the last two path segments, lower case, because BOP has thirteen base-name")
    out.append("# collisions -- blank.png is a block and an item, dandelion.png is a block and a")
    out.append("# particle, six armour textures share a name with their item icon, and all four mobs")
    out.append("# have a say.ogg and a hurt.ogg. A key written without a slash is matched on base name")
    out.append("# instead, which is what the Minecraft section at the bottom uses.")
    out.append("#")
    out.append("# The value may be a comma-separated list, for one upstream file that feeds several")
    out.append("# paths here.")
    out.append("#")
    out.append("# GENERATED by tools/gen_asset_bridge_manifest.py -- do not hand-edit.")
    out.append("")

    emitted: set[str] = set()
    for directory, heading, blurb in SECTIONS:
        keys = sorted(grouped.get(directory, []))
        if not keys:
            continue
        emitted.add(directory)
        out.append(f"# --- {heading} " + "-" * max(4, 78 - len(heading)))
        for line in blurb.split("\n"):
            out.append(f"# {line}" if not line.startswith("#") else line)
        out.append("")
        for key in keys:
            paths = sorted(mapping[key])
            out.append(f"{key} = {', \\\n    '.join(paths)}" if len(paths) > 1
                       else f"{key} = {paths[0]}")
        out.append("")

    leftover = sorted(set(grouped) - emitted)
    if leftover:
        out.append("# --- Other " + "-" * 68)
        out.append("# Upstream directories with no section above. If anything lands here, give it one.")
        out.append("")
        for directory in leftover:
            for key in sorted(grouped[directory]):
                paths = sorted(mapping[key])
                out.append(f"{key} = {', '.join(paths)}")
        out.append("")

    out.append("# --- Minecraft " + "-" * 64)
    out.append("# Vanilla art, from any Minecraft jar or resource pack the player already has. These")
    out.append("# are Mojang's, not BOP's, and they are here because a handful of ported BOP features")
    out.append("# place vanilla blocks that BTA 8.0 does not ship -- so the block had to come along and")
    out.append("# needs its vanilla face. Matched on base name, because the directory was renamed from")
    out.append("# textures/blocks/ to textures/block/ in 1.13 and these names collide with nothing.")
    out.append("")
    for name, paths in MINECRAFT_SECTION:
        out.append(f"{name} = {', '.join(PACK_PREFIX + p for p in paths)}")
    out.append("")

    args.out.parent.mkdir(parents=True, exist_ok=True)
    args.out.write_text("\n".join(out), encoding="utf-8")

    write_hashes(args.hash_out, mapping.keys(), references)

    total = sum(len(v) for v in mapping.values())
    print(f"wrote {args.out}")
    print(f"  {len(mapping)} BOP keys -> {total} pack paths")
    print(f"  {len(MINECRAFT_SECTION)} Minecraft keys -> {len(MINECRAFT_SECTION)} pack paths")
    for directory, heading, _ in SECTIONS:
        if grouped.get(directory):
            print(f"    {heading:16} {len(grouped[directory]):4}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
