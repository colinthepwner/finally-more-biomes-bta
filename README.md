# Finally More Biomes

A port of **Biomes O' Plenty** to **Better than Adventure!** `8.0.1`, built on Babric.

> **This mod ships none of Biomes O' Plenty's art.** Drop your own copy of BOP into your game
> directory and the mod reads the textures and sounds out of it. Nothing is downloaded and nothing
> is redistributed — see [Textures and sounds](#textures-and-sounds--the-asset-bridge) and
> [NOTICE.md](NOTICE.md).

| | |
|---|---|
| **Minecraft / BTA** | Better than Adventure! `8.0.1` (`release` channel) |
| **Mod loader** | Babric / fabric-loader `0.18.4-bta.11` |
| **Requires** | HalpLibe `6.1.4+8.0` — ships inside the jar, no separate download |
| **Java** | 17 |
| **Licence** | CC BY-NC-ND 4.0, inherited from Biomes O' Plenty |

## Lineage

**Biomes O' Plenty** is [Glitchfiend's](https://github.com/Glitchfiend/BiomesOPlenty) — Forstride,
Adubbz, Amnet and ted80. This is a reimplementation of it against BTA 8.0's API, built from the two
classic CC-era releases: `BOP-b1.7.3` and `BOP-1.1.2-1.6.4`.

It is not affiliated with or endorsed by Glitchfiend, and claims no trademark in the name
"Biomes O' Plenty". It is called **Finally More Biomes** so that the two are not confused with each
other.

## Install

1. Install [Better than Adventure!](https://betterthanadventure.net/) `8.0.1` with Babric.
2. Drop `betteroplenty-<version>+8.0.1.jar` into `mods/`. It carries
   [HalpLibe](https://github.com/Turnip-Labs/bta-halplibe) inside it; if your instance already has
   HalpLibe, the newest copy wins.
3. **Drop your own copy of Biomes O' Plenty somewhere under your game directory.** That is the
   whole step — see below.
4. Start a new world and pick the **Finally More Biomes** world type.

## Textures and sounds — the asset bridge

Biomes O' Plenty is licensed CC BY-NC-ND, whose NoDerivatives term bars *Sharing*. Its art
therefore cannot live in this repository or in the released jar, and none of it does.

Instead: **drop your own copy of BOP anywhere under your game directory.** On startup the mod reads
the images and sounds out of it, writes a texture pack called `FinallyMoreBiomesAssets`, and enables it
for you. Nothing is downloaded and nothing is redistributed — the file has to already be on your
disk.

### Where to get it

BOP's classic releases are still on Glitchfiend's own GitHub, which is the most legitimate source
there is. **Download the source archive for the tag** — the art lives in the repository tree, not
in the release attachments:

| | Download | Supplies |
|---|---|---|
| **BOP for Beta 1.7.3** | [`BOP-b1.7.3.zip`](https://github.com/Glitchfiend/BiomesOPlenty/archive/refs/tags/BOP-b1.7.3.zip) | 438 of the 450 files |
| **BOP 1.1.2 for 1.6.4** | [`BOP-1.1.2-1.6.4.zip`](https://github.com/Glitchfiend/BiomesOPlenty/archive/refs/tags/BOP-1.1.2-1.6.4.zip) | the remaining 17 |

**Take both.** They overlap heavily but neither is sufficient alone: the beta tree is the primary
source and the only one with 120 of the files, while the 1.6.4 tree is the only source for 17
others — the pre-rename apatite art, the six gem frames, the altar frame and a few more the later
tree dropped or renamed. With only one of them the mod still runs; the files the other would have
supplied render as the missing-texture checker, and the startup log names them.

> **On mcarchive.** [mcarchive.net](https://mcarchive.net/mods/biomes-o-plenty) is the usual mirror
> for mods of this era, and it is worth saying plainly that it **does not carry either of these
> versions** — its Biomes O' Plenty entries stop at Minecraft 1.4.7. There is no beta-era or
> 1.6.4-era BOP there to link to. Glitchfiend's own repository above is the source.

### It really is anywhere

Nothing about the search depends on the file being called the right thing or sitting in the right
folder:

- **Any folder**, not just `mods/`, to six levels deep.
- **Any name.** A container is recognised by its zip header, so the extension is irrelevant —
  `.zip`, `.jar`, `.disabled`, none at all, all the same.
- **Unpacked works too.** If you extracted it, the loose files are picked up individually, so a
  folder is as good as an archive.
- **Nested archives are followed**, three levels deep.

The name still counts for one thing: it decides what gets looked at first, so an obviously named
archive wins over an unrelated mod jar that happens to contain an `amethyst.png`. More than one
source can contribute — which is the normal case here, with two archives — and the first to supply
a given file wins, so a partial copy is topped up rather than rejected.

It will not load as a mod. It is Beta 1.7.3 and 1.6.4 Forge code and BTA ignores it; it is read
purely as a data source.

What the pack was built from is recorded inside it, so later launches skip the search entirely.
**Delete `texturepacks/FinallyMoreBiomesAssets` to force a fresh look.**

### A few textures come from Minecraft, not from BOP

Some ported BOP features place *vanilla* blocks that BTA 8.0 does not ship — mycelium, giant
mushrooms and hardened clay — so those blocks had to be ported alongside the features that place
them, and they need their vanilla faces. The bridge picks those eight textures up from any
Minecraft jar or resource pack you already have under the game directory, by the same search. If it
finds none, those eight blocks render as the checker and everything else is unaffected.

## Build

```bash
./gradlew build
```

Output lands in `build/libs/`. JDK 17 is required; Gradle will fetch the toolchain automatically.

## What's in it

Biomes O' Plenty's biomes, blocks, plants, fluids, tools and mobs, rebuilt on BTA 8.0:

- **Biomes** across the overworld, woven in by their own world type, plus a **BOP Nether** and the
  **Promised Land** as separate world types with their own terrain, sky and fog.
- **Blocks and plants** — the wood sets, the flowers and ground cover, the gem ores in their host
  rocks, and the decorative sets.
- **Fluids** — spring water, liquid poison and honey, with the bucket states that go with them.
- **Mobs** — the bird, wasp, pixie, phantom, glob, jungle spider and rosester.
- **Items** — the gem tools and armour, the flower bands, the scythes, the jars and the food.

The startup log prints an audit of what registered, biome by biome.

## Known limits

- **Without a copy of BOP, the mod's blocks have no art.** They register, generate and behave
  correctly; they render as the missing-texture checker. This is the design, not a bug — see above.
- **Both archives are wanted, not one.** With only one, the files the other would have supplied are
  missing. The log names them at startup.
- The 8 vanilla textures need a Minecraft jar or resource pack on disk as well; see above.

## Licence

**CC BY-NC-ND 4.0**, in [LICENSE](LICENSE) — the same licence as the work this is derived from,
because a derivative of a CC BY-NC-ND work cannot be relicensed more permissively.

[NOTICE.md](NOTICE.md) records the provenance of every file in the repository: what is
Glitchfiend's, what is Turnip Labs', what is Mojang's, what was built from whose pixels, and what
was written for this port. If you are a rights holder and want something removed, open an issue —
see [NOTICE §5](NOTICE.md#5-if-you-are-a-rights-holder).

<!-- keywords: finally more biomes, biomes o plenty, BOP, BTA, Better than Adventure, babric, fabric,
minecraft beta 1.7.3, b1.7.3, halplibe, bta mod, bta 8.0.1, Glitchfiend, biome mod, worldgen -->
