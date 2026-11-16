package com.pulsehud.integration

import com.pulsehud.PulseHUDMod
import com.pulsehud.config.ConfigManager
import com.pulsehud.ui.PulseHudScreen
import net.minecraft.client.MinecraftClient

class ModMenuIntegration {

    fun openScreen() {
        val client = MinecraftClient.getInstance()
        client.setScreen(PulseHudScreen(PulseHUDMod.REGISTRY, ConfigManager(), { }))
    }
}
