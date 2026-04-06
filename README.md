# Beta Evolutions ModLoader Example

This folder is a standalone template-style example ModLoader mod for Beta Evolutions V2.

## What it does

- Adds a `mod_BetaEvolutionsExample` ModLoader entrypoint
- Registers a keybind on `G`
- Prints a short chat message when a world finishes loading
- Shows your current XYZ position in chat when you press `G`
- Optionally includes your facing direction in the readout
- Stays compatible with normal vanilla servers because it is entirely client-side

## Build

```bash
mvn clean package
```

The built mod jar will be written to `target/beta-evolutions-example-mod.jar`.

## Install into Beta Evolutions

1. Build the jar.
2. Copy `target/beta-evolutions-example-mod.jar` into `\evolutions\mods`.
3. Start Beta Evolutions once so ModLoader discovers the mod.
4. From the main menu, open `Options -> Beta Evolution Settings -> ModLoader Mods`.
5. Enable the detected `mod_BetaEvolutionsExample` entry.
6. Restart the game.

The current ModLoader implementation in Beta Evolutions V2 creates newly discovered mods as `off` by default, so enabling the mod and restarting is required.
