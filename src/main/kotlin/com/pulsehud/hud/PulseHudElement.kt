package com.pulsehud.hud

import com.pulsehud.modules.ModuleRegistry
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

class PulseHudElement(private val registry: ModuleRegistry) {

    fun render(context: DrawContext, tickDelta: Float, scale: Float) {
        val client = MinecraftClient.getInstance()
        if (client.player == null) return
        registry.render(context, scale)
    }
}
