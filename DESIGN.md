# PulseHUD Design Doc

## Module architecture
A `ModuleRegistry` keyed by module id (`"1"`..`"25"`). Each module implements `Module<C : ModuleConfig>` with:
- `id`, `category`, `defaultEnabled`, `config: C`
- `update(client, tickDelta): Unit` — runs on client tick, reads only local client state, writes into an immutable `ModuleSnapshot`.
- `render(guiGraphics, snapshot, scale): Unit` — reads snapshot only, no allocations in hot paths (FPS/Ping/CPS/Speed reuse buffers).

Configs are data classes under `modules.data`; theme tokens live in `theme.Theme`. UI reads `Config`, which holds a `Theme` and per-module `ModuleConfig` wrappers (enabled + position/anchor/offset).

## Package layout
```
com.pulsehud
  PulseHUDMod.kt                 mod init, keybind, screen, config
  config/                        Config, ConfigManager, migration
  theme/                         Theme, Preset
  modules/                       Module, ModuleRegistry, ModuleCategory, ModuleSnapshot
  modules/data/                  all 25 config data classes
  hud/                           PulseHudElement (Fabric API HUD element), HudRegistry
  input/                         Keybinds, AttackListener (AttackEntityCallback edge detection)
  ui/                            PulseHudScreen, SplashScreen, LayoutEditor, Toasts, theme widgets
  ui/widgets/                    toggle, slider, hex color, cycle button
  util/                          HexGrid (no MC imports), Animation, Easing
```

## Threading model
State updates run on the client tick thread. Rendering (HUD element) reads an immutable snapshot captured at tick end. No cross-thread writes. Config load/save happens on the client thread during init and on change (fast, small JSON).

## Three riskiest parts & mitigations
1. **Unverified Fabric API names for MC 26.3** (HUD callback replacement, keybind class, attack callback, damage source position). Mitigation: all uncertain names are listed in ASSUMPTIONS.md; code compiles against assumed names; a build failure points to the exact assumption to check.
2. **Damage Direction real source**. MC 26.3 may not expose a real damage-source position. Mitigation: if no real source exists, drop the module (listed in DROPPED.md); never fake direction.
3. **Blur / reduced-motion / animations pausing when game paused**. Mitigation: drive animations by tick + partial tick (pause-safe); if blur impractical, fall back to semi-transparent overlay (listed in DROPPED.md).

## Config
`config/pulsehud.json` with `schemaVersion`. Load on startup, save on every change. Migration warns once per unparseable field and falls back to default.
