# Storm Set ⚡🔵

A NeoForge mod for **Minecraft 1.21.11** that adds the **Storm Set** — electric-blue gear that crackles with lightning. Three pieces: the **Storm Cape**, the **Storm Helmet**, and the **Storm Staff**.

> Designed by a kid, built together. 💙

## The Set

### ⚡ Storm Cape
An elytra-like cape that glides like an elytra, but the wings render **1.5× bigger** with a custom electric-blue texture.
- **Electric sparks** trail behind you while you fly.
- A **blue soul-torch glow** swirls around you whenever it's worn.
- **Electrocutes** any mob or player that touches you — 2 hearts per zap, with a short cooldown.

### 🪖 Storm Helmet
Head armor (iron-level protection, repairs with copper) with a black-and-blue texture and glowing yellow **lightning horns** that tilt with your head.

### 🔱 Storm Staff
- **Right-click** — a firework-style speed boost (much stronger while gliding with the Cape), with blue and yellow sparks trailing behind.
- **Left-click** — shoot **lightning** that strikes the ground at any range.

All three are **EPIC** rarity and show up in the **Combat** creative tab.

## Crafting

**Storm Cape** — elytra + copper + amethyst
```
A C A      A = Amethyst Shard
C E C      C = Copper Ingot
A C A      E = Elytra
```

**Storm Helmet**
```
A C A      A = Amethyst Shard
C   C      C = Copper Ingot
```

**Storm Staff**
```
  A        A = Amethyst Shard
  C        C = Copper Ingot
  C
```

## Build
Requires JDK 21.
```bash
./gradlew build      # jar -> build/libs/stormcape-<version>.jar
```

## Install
Drop `stormcape-<version>.jar` into your `mods/` folder. It's a **both-side** mod (client *and* server need it, matching versions).

## Releases
CI builds every push; pushing a tag `v<version>` publishes the jar to **Modrinth** and a **GitHub Release**. See [RELEASING.md](RELEASING.md).

## License
[MIT](LICENSE)
