# Notice — provenance, licensing and what is actually in this repository

**Short version.** Better O' Plenty is a port of **Biomes O' Plenty**, which is
**Glitchfiend's** work, licensed **CC BY-NC-ND 4.0**. This repository and the released jar
contain **none of Biomes O' Plenty's textures or audio**. Those are fetched at runtime from a copy
of BOP that *you* already have on your own disk. Nothing is downloaded and nothing is mirrored here.

This project is non-commercial, is not affiliated with or endorsed by Glitchfiend, and claims no
trademark in the name "Biomes O' Plenty".

This notice is provenance documentation, not legal advice.

---

## 1. What is Glitchfiend's, and where it is

Biomes O' Plenty is by **Glitchfiend** — Forstride, Adubbz, Amnet and ted80, with credits to
gamax92, enchilado, Tim Rurkowski and Soaryn. This port is built from the two **classic, CC-era**
releases, and not from any modern All-Rights-Reserved BOP:

| Source | Upstream tag | What it supplies here |
|---|---|---|
| BOP for Minecraft Beta 1.7.3 | [`BOP-b1.7.3`](https://github.com/Glitchfiend/BiomesOPlenty/tree/BOP-b1.7.3) | 438 of the 450 bridged files; the only source for 120 of them |
| BOP 1.1.2 for Minecraft 1.6.4 | [`BOP-1.1.2-1.6.4`](https://github.com/Glitchfiend/BiomesOPlenty/tree/BOP-1.1.2-1.6.4) | the other 17 — the pre-rename apatite art, the six gem frames, the altar frame and a handful more that the later tree dropped or renamed |

Ported Java classes keep their original `biomesoplenty.*` package names and their original
copyright headers, unchanged. That is deliberate: attribution is not something a port gets to
tidy away.

## 2. What ships, file by file

The line here is drawn by **content**, not by category, and it is drawn mechanically. Every asset
in the tree is byte-compared against the upstream trees by
[`tools/classify_asset_provenance.py`](tools/classify_asset_provenance.py), and what that tool
decides is what happens.

### Bridged — 458 files, **not** in this repository or the jar

| Count | What |
|---|---|
| 331 | BOP block textures |
| 83 | BOP item textures |
| 10 | BOP armour layers |
| 6 | BOP mob skins |
| 9 | BOP mob sounds (`.ogg`) |
| 2 | BOP music discs (`.ogg`) |
| 1 | BOP particle sprite |
| 8 | **Mojang's** vanilla textures — mycelium, giant-mushroom skins and hardened clay |

Every one of these is byte-identical to a file in an upstream tree. They are listed in
[`asset-bridge.properties`](src/main/resources/assets/betteroplenty/asset-bridge.properties) and
supplied at runtime by the asset bridge. See the README for how that works.

The eight Mojang textures are here because a handful of ported BOP features place *vanilla* blocks
that BTA 8.0 does not ship, so the block had to be ported alongside the feature and needs its
vanilla face. They come from whatever Minecraft jar or resource pack you already have. They are
Mojang's, not Glitchfiend's, and shipping them would be a separate problem from the BOP one — so
they are bridged for the same reason and by the same mechanism.

### Adapted — 95 files, **shipped**, and the part worth reading twice

These are not byte-identical to anything upstream, but they are **built out of upstream pixels**:

| Count | What | Made from |
|---|---|---|
| 35 | gem ore variants | BOP's gem pixels composited onto BTA's rock |
| 24 | particle frames | BOP's 128×128 particle sheets, cut into the 8×8 frames BTA's atlas stitches |
| 19 | amethyst bucket states | BOP's amethyst bucket body, with fills recoloured from BTA's own bucket art |
| 12 | iron and steel bucket states | **BTA's** own bucket art, recoloured for the fluids this mod adds |
| 5 | jars and ambrosia | redrawn from **BTA's** `jar.png`, because BTA has no glass bottle |

**Be aware of what this means.** CC BY-NC-ND calls this *Adapted Material*, and the licence
restricts sharing it at least as firmly as it restricts sharing a verbatim copy. The 35 ore
variants, the 24 particle frames and the amethyst bucket body carry Glitchfiend's pixels and are
distributed here. That is a deliberate decision by this project's author, made with the position
understood — not an oversight, and not a claim that the licence permits it.

The 17 files built from BTA's art rather than BOP's are a separate matter: BTA is
[Turnip Labs'](https://betterthanadventure.net/) work, and those are recolours of it.

If you are Glitchfiend, or Turnip Labs, and you would like any of this removed — see
[§5](#5-if-you-are-a-rights-holder).

### The port's own — shipped without reservation

- **452 Java source files.** 269 ported `biomesoplenty.*` classes (derivative of BOP, and so under
  BOP's licence) and 183 `com.betteroplenty.*` classes written for this port.
- **76 model JSON files.** BTA-format block and entity models written for this port. BOP had Java
  model classes and no JSON; none of this is transcribed from upstream.
- **7 `.png.mcmeta` files.** Animation frame timings — `{"animation":{"frametime":3}}` and the
  like. Five are byte-identical to upstream's, because the same number is the same number. They
  ship anyway: a frame timing is a parameter, not creative work, and bridging one would leave every
  fluid in the mod visibly static for anyone without the archive.
- **The language file.** `betteroplenty.lang` — display names for this port's blocks, items and
  biomes, transcribed from BOP's own `en_US` strings. It ships for the same reason: these are the
  labels a block needs to be usable, and without them every block in the game shows a raw
  translation key.
- **The mod icon.** Drawn from scratch by [`tools/gen_mod_icon.py`](tools/gen_mod_icon.py). The
  icon it replaces was BOP's own logo and wordmark; that could not stay, and could not be bridged
  either, because ModMenu reads the icon from the mod's classpath and a texture pack is not on it.

## 3. Build scaffold

The Gradle/Loom scaffold derives from [`Turnip-Labs/bta-example-mod`](https://github.com/Turnip-Labs/bta-example-mod),
released under **CC0 1.0 Universal** (public domain).

## 4. Licence

The port is licensed **CC BY-NC-ND 4.0** — the same licence as the work it is derived from, in
[LICENSE](LICENSE).

It could not honestly be anything else. The ported `biomesoplenty.*` classes are derivative works
of a CC BY-NC-ND original, so this project has no power to relicense them more permissively, and
labelling the repository MIT or CC0 would be claiming a freedom it cannot grant. Non-commercial,
attribution required, and no relicensing.

## 5. If you are a rights holder

If you are **Glitchfiend**, or **Turnip Labs**, and you want this repository, the release, or any
specific file taken down or changed, open an issue or contact the repository owner through GitHub
and it will be done. No argument, no delay. That offer is the point of writing this notice as
precisely as it is written: everything above is specific enough to act on file by file, rather than
requiring anyone to reverse-engineer what is in here.

## 6. Verifying any of this yourself

Nothing above has to be taken on trust. The tools that draw the line are in the repository:

```bash
python tools/strip_bridged_assets.py --check
```

exits non-zero if a single file the bridge is supposed to supply has found its way into the tree.
It runs in CI on every push, and again before every release.
