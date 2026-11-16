# PulseHUD — ASSUMPTION list (Minecraft 26.3 / Fabric API)

Minecraft 26.3 and its Fabric API mappings are not verifiable in this environment. Every Fabric/Minecraft API name below is an ASSUMPTION. If compilation fails, check the exact name listed and the suggested replacement.

- **HUD rendering callback**: assumed `net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback` registers `{ guiGraphics, tickDelta -> ... }`. ASSUMPTION: it may be deprecated in 26.3 with a replacement such as `net.fabricmc.fabric.api.client.hud.v1.HudRenderCallback` or a `HudElementRegistry`. Check the 26.3 Fabric API changelog for the current HUD callback and use it in `hud/PulseHudElement.kt`.
- **Keybinding**: assumed `net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper.registerKeyBinding(KeyBinding)` persists via vanilla options. ASSUMPTION: 26.3 may use a different keybind registration class (e.g. `KeyBindingRegistry` or `KeyMappingHelper`). Check `net.fabricmc.fabric.api.client.keybinding.v1`.
- **Client tick**: assumed `net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.END_CLIENT_TICK`.
- **Attack entity callback (client)**: assumed `net.fabricmc.fabric.api.client.event.v1.ClientEntityEvents.ATTACK_ENTITY` provides `{ world, player, entity, hand -> }`. If unavailable, Combo Counter / Hit Indicator cannot detect hits without a mixin; list under DROPPED.
- **Damage source position**: assumed the 26.3 client exposes a real damage-source position (e.g. `DamageSource#getSourcePosition` or client damage tracker). If not exposed, Damage Direction is dropped and listed in DROPPED.md. Do not fake direction.
- **Block light / tool hint**: assumed `World#getLightLevel`/`getBlockState` and an exposed `getRequiredTool` or similar. If unavailable, Looking At Block tool hint and Light Level lose those fields (simplified).
- **Key state edge detection**: assumed `MinecraftClient#getInstance().options` exposes key bindings with `isPressed()` each tick, readable without mixin.
- **Blur**: assumed impractical in the 26.3 render pipeline; fallback is a semi-transparent overlay. Listed in DROPPED.md if blur cannot be done with `GuiGraphics`.
- **FPS counter**: assumed `MinecraftClient#getInstance().getCurrentFps()` or a debug FPS provider is accessible.
- **Latency / player info**: assumed `MinecraftClient#getInstance().getPlayerListEntry(uuid)` or network handler provides latency for the local player.
- **Biome localized name**: assumed `RegistryKeys.BIOME` / `Biome#getTranslationKey` with a translator.
- **Mixin**: at most one mixin to hide the vanilla crosshair, targeting an assumed method on `net.minecraft.client.gui.hud.InGameHud`. Not shipped; crosshair is drawn as an overlay unless the mixin target is verified (see DROPPED.md).
- **GuiGraphics replacement**: assumed `net.minecraft.client.gui.DrawContext` for HUD rendering with `fill` and `drawText`. If 26.3 uses `GuiGraphics` instead, swap the type in `hud/PulseHudElement.kt` and `modules/*Module.kt`.
- **Mod Menu integration**: assumed `net.modmenu.ModMenuApi` / `ModMenuIntegration` entrypoint loads reflectively only when Mod Menu is present.
