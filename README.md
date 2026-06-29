# Storm Cape ⚡🔵

A NeoForge mod for **Minecraft 1.21.11** that adds the **Storm Cape** — an elytra-like cape, 50% bigger than a normal elytra, that crackles with electricity.

> Designed by a kid, built together. 💙

## Features
- **Glides like an elytra**, but the wings render **1.5× bigger** with a custom electric-blue texture.
- **Electric sparks** trail behind you while you fly.
- A **blue soul-torch glow** swirls around you whenever it's worn.
- **Electrocutes** any mob or player that touches you — 2 hearts per zap, with a short cooldown.
- **Craftable** (elytra + copper + amethyst) and available in the **Combat** creative tab.

## Crafting
```
A C A      A = Amethyst Shard
C E C      C = Copper Ingot
A C A      E = Elytra
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
