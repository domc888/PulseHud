package com.pulsehud.modules

import com.pulsehud.modules.ModuleCategory.MISC
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

data class CompassConfig : ModuleConfig() {
    var width: Int = 200
    var showDegrees: Boolean = true
}

data class CompassSnapshot : ModuleSnapshot() {
    var yaw: Float = 0f
}

class CompassModule : Module<CompassConfig>() {
    override val id = "20"
    override val category = MISC
    override val defaultEnabled = false
    override val config: CompassConfig = CompassConfig()

    override fun update(client: MinecraftClient): ModuleSnapshot {
        val snap = CompassSnapshot()
        snap.yaw = client.player.yaw
        return snap
    }

    override fun render(context: DrawContext, snapshot: ModuleSnapshot, scale: Float) {
        val snap = snapshot as CompassSnapshot
        val client = MinecraftClient.getInstance()
        val cx = (client.getWindow().getFramebufferWidth() / 2.0f).toInt()
        val cy = (client.getWindow().getFramebufferHeight() / 2.0f).toInt()
        val w = config.width.coerceIn(100, 400)
        val x0 = cx - w / 2
        val y0 = cy - 30
        context.fill(x0, y0, x0 + w, y0 + 10, 0x7F_000000)
        val dirs = listOf(0f to "N", 90f to "W", 180f to "S", 270f to "E")
        for ((d, name) in dirs) {
            val dx = ((d - snap.yaw + 360) % 360 / 360.0) * w
            context.drawText(client.textRenderer, name, x0 + dx.toInt() - 4, y0 - 10, 0xFF_FFFFFF, false)
        }
        if (config.showDegrees) {
            context.drawText(client.textRenderer, "${snap.yaw.toInt()}°", cx - 10, y0 + 12, 0xFF_FFFFFF, false)
        }
    }
}
