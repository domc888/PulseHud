package com.pulsehud

import com.pulsehud.config.Config
import com.pulsehud.config.ConfigManager
import com.pulsehud.hud.PulseHudElement
import com.pulsehud.input.AttackListener
import com.pulsehud.input.Keybinds
import com.pulsehud.modules.ArmorModule
import com.pulsehud.modules.BiomeModule
import com.pulsehud.modules.CoordinatesModule
import com.pulsehud.modules.ComboModule
import com.pulsehud.modules.CompassModule
import com.pulsehud.modules.ClockModule
import com.pulsehud.modules.CrosshairModule
import com.pulsehud.modules.CpsModule
import com.pulsehud.modules.FpsModule
import com.pulsehud.modules.HitIndicatorModule
import com.pulsehud.modules.HotbarKeysModule
import com.pulsehud.modules.ItemCountModule
import com.pulsehud.modules.KeystrokesModule
import com.pulsehud.modules.LightLevelModule
import com.pulsehud.modules.LookingAtBlockModule
import com.pulsehud.modules.MemoryModule
import com.pulsehud.modules.ModuleRegistry
import com.pulsehud.modules.MovementStateModule
import com.pulsehud.modules.PingModule
import com.pulsehud.modules.PotionModule
import com.pulsehud.modules.SessionTimerModule
import com.pulsehud.modules.SpeedModule
import com.pulsehud.modules.TargetInfoModule
import com.pulsehud.modules.WatermarkModule
import com.pulsehud.ui.PulseHudScreen
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.ClientCommandBuilder
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient

class PulseHUDMod : ClientModInitializer {

    internal companion object {
        internal lateinit var REGISTRY: ModuleRegistry
    }

    private lateinit var config: Config
    private lateinit var configManager: ConfigManager
    private lateinit var registry: ModuleRegistry
    private lateinit var keybinds: Keybinds
    private lateinit var attackListener: AttackListener
    private lateinit var hudElement: PulseHudElement
    private var screenOpen = false
    private var splashShown = false

    override fun onInitialize() {
        configManager = ConfigManager()
        config = configManager.load()
        registry = ModuleRegistry(config)
        REGISTRY = registry
        keybinds = Keybinds()
        KeyBindingHelper.registerKeyBinding(keybinds.pulseHud)
        attackListener = AttackListener(registry)
        hudElement = PulseHudElement(registry)
        registry.register(CrosshairModule())
        registry.register(FpsModule())
        registry.register(PingModule())
        registry.register(CpsModule())
        registry.register(SpeedModule())
        registry.register(KeystrokesModule())
        registry.register(CoordinatesModule())
        registry.register(ArmorModule())
        registry.register(PotionModule())
        registry.register(ComboModule())
        registry.register(ClockModule())
        registry.register(WatermarkModule())
        registry.register(TargetInfoModule())
        registry.register(HitIndicatorModule())
        registry.register(HotbarKeysModule())
        registry.register(ItemCountModule())
        registry.register(MemoryModule())
        registry.register(CompassModule())
        registry.register(LookingAtBlockModule())
        registry.register(LightLevelModule())
        registry.register(SessionTimerModule())
        registry.register(MovementStateModule())
        registry.register(BiomeModule())
        val combo = registry.getModule<ComboModule>("10")
        val hit = registry.getModule<HitIndicatorModule>("14")
        attackListener.onPlayerHit = { entity ->
            combo?.hit(clientTick(entity))
            hit?.flash(clientTick(entity))
        }
        ClientTickEvents.END_CLIENT_TICK.register { client ->
            if (client.player == null) return@register
            keybinds.tick()
            if (keybinds.pulseHud.wasPressed() && !screenOpen) {
                if (!splashShown) {
                    splashShown = true
                    screenOpen = true
                    client.setScreen(com.pulsehud.ui.SplashScreen(config.theme, {
                        client.setScreen(PulseHudScreen(registry, configManager, { screenOpen = false }))
                    }))
                } else {
                    screenOpen = true
                    client.setScreen(PulseHudScreen(registry, configManager, { screenOpen = false }))
                }
            }
            registry.update(client)
            attackListener.tick(client)
        }
        HudRenderCallback.EVENT.register { guiGraphics, tickDelta ->
            val client = MinecraftClient.getInstance()
            hudElement.render(guiGraphics, tickDelta, client.options.guiScale.toFloat())
        }
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(ClientCommandBuilder.literal("pulsehud")
                .then(ClientCommandBuilder.literal("reload").executes { context ->
                    config = configManager.reload()
                    context.source.sendFeedback(java.lang.String("PulseHUD config reloaded"), false)
                    1
                })
                .then(ClientCommandBuilder.literal("reset").executes { context ->
                    registry.allModules().forEach { it.config.enabled = false }
                    configManager.save(config)
                    context.source.sendFeedback(java.lang.String("PulseHUD config reset"), false)
                    1
                })
                .build())
        }
    }

    private fun clientTick(entity: net.minecraft.entity.Entity): Long = entity.world.time
}
