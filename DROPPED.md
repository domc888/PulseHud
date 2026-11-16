# PulseHUD — Dropped or simplified

1. **Damage Direction (Module 15)** — Dropped. Requires a real damage-source position exposed by the 26.3 client; none verified. The client does not expose a usable source position without mixins that read network/packet data, which would risk revealing non-local info. Reinstated only if a real source API is confirmed.
2. **Blur behind settings panel** — Simplified. If the 26.3 render pipeline makes blur impractical with `GuiGraphics`, a semi-transparent dark overlay is used instead.
3. **True vanilla crosshair hiding** — Simplified. The custom crosshair is drawn as an overlay via the HUD callback. To fully replace (hide) the vanilla crosshair requires ONE mixin targeting an unverified 26.3 method on `InGameHud`; not shipped because the target cannot be verified. Add the mixin (see ASSUMPTIONS.md) when the target is confirmed.
4. **Mod Menu hard dependency** — Simplified. Mod Menu is optional (`suggests`). The `ModMenuIntegration` class is registered as a `modmenu` entrypoint and only loads if Mod Menu is present; Fabric skips missing optional entrypoints.

All other 24 modules plus theme, splash, settings, layout editor, toasts, commands, and config migration are implemented.
