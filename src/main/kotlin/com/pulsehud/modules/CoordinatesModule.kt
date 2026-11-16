package com.pulsehud.modules

import com.pulsehud.modules.ModuleCategory.HUD
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

enum class Precision { INTEGER, DECIMAL }

data class CoordinatesConfig : ModuleConfig() {
    var showY: Boolean = true
    var precision: Precision = Precision.INTEGER
}

data class CoordinatesSnapshot : ModuleSnapshot() {
    var x: Double = 0
    var y: Double = 0
    var z: Double = 0
    var facing: String = ""
}

class CoordinatesModule : Module<CoordinatesConfig>() {
    override val id = "7"
    override val category = HUD
    override val defaultEnabled = false
    override val config: CoordinatesConfig = CoordinatesConfig()

    override fun update(client: MinecraftClient): ModuleSnapshot {
        val snap = CoordinatesSnapshot()
        val p = client.player
        snap.x = p.x
        snap.y = p.y
        snap.z = p.z
        snap.facing = p.horizontalFacing.toString()
        return snap
    }

    override fun render(context: DrawContext, snapshot: ModuleSnapshot, scale: Float) {
        val snap = snapshot as CoordinatesSnapshot
        val client = MinecraftClient.getInstance()
        val fmt = if (config.precision == Precision.DECIMAL) "%.1f" else "%.0f"
        val parts = mutableListOf<String>()
        parts.add("X: ${String.format(fmt, snap.x)}")
        if (config.showY) parts.add("Y: ${String.format(fmt, snap.y)}")
        parts.add("Z: ${String.format(fmt, snap.z)}")
        parts.add(snap.facing)
        context.drawText(client.textRenderer, parts.joinToString(" "), 2, 50, 0xFF_FFFFFF, false)
    }
}
