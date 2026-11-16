package com.pulsehud.modules

import com.pulsehud.modules.ModuleCategory.MISC
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

data class WatermarkConfig : ModuleConfig() {
    var textOverride: String = ""
    var shadow: Boolean = true
    var opacity: Int = 100
    var showFps: Boolean = false
    var showTime: Boolean = false
}

data class WatermarkSnapshot : ModuleSnapshot() {
    var text: String = ""
    var alpha: Int = 255
}

class WatermarkModule : Module<WatermarkConfig>() {
    override val id = "12"
    override val category = MISC
    override val defaultEnabled = false
    override val config: WatermarkConfig = WatermarkConfig()

    override fun update(client: MinecraftClient): ModuleSnapshot {
        val snap = WatermarkSnapshot()
        val base = if (config.textOverride.isNotEmpty()) config.textOverride else "PulseHUD 1.0.0"
        val suffix = mutableListOf<String>()
        if (config.showFps) suffix.add("FPS ${client.currentFps.toInt()}")
        if (config.showTime) suffix.add(java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")))
        snap.text = base + (if (suffix.isEmpty()) "" else " " + suffix.joinToString("|"))
        snap.alpha = (255 * config.opacity / 100).coerceIn(0, 255)
        return snap
    }

    override fun render(context: DrawContext, snapshot: ModuleSnapshot, scale: Float) {
        val snap = snapshot as WatermarkSnapshot
        val client = MinecraftClient.getInstance()
        val color = (snap.alpha shl 24) or 0xFFFFFF
        context.drawText(client.textRenderer, snap.text, 2, client.getWindow().getFramebufferHeight() - 10, color, config.shadow)
    }
}
