package com.pulsehud.modules

import com.pulsehud.modules.ModuleCategory.HUD
import com.pulsehud.util.MovingAverage
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

enum class SpeedUnit { BPS, KMH }

data class SpeedConfig : ModuleConfig() {
    var unit: SpeedUnit = SpeedUnit.BPS
    var decimals: Int = 1
}

data class SpeedSnapshot : ModuleSnapshot() {
    var speed: Double = 0.0
}

class SpeedModule : Module<SpeedConfig>() {
    override val id = "5"
    override val category = HUD
    override val defaultEnabled = false
    override val config: SpeedConfig = SpeedConfig()

    private val average = MovingAverage(5)
    private var lastX = 0.0
    private var lastZ = 0.0
    private var initialized = false

    override fun update(client: MinecraftClient): ModuleSnapshot {
        val snap = SpeedSnapshot()
        val player = client.player
        val x = player.x
        val z = player.z
        var speed = 0.0
        if (initialized) {
            val dx = x - lastX
            val dz = z - lastZ
            speed = kotlin.math.sqrt(dx * dx + dz * dz) * 20.0
        } else {
            initialized = true
        }
        lastX = x
        lastZ = z
        average.add(speed)
        snap.speed = when (config.unit) {
            SpeedUnit.BPS -> average.average()
            SpeedUnit.KMH -> average.average() * 3.6
        }
        return snap
    }

    override fun render(context: DrawContext, snapshot: ModuleSnapshot, scale: Float) {
        val snap = snapshot as SpeedSnapshot
        val client = MinecraftClient.getInstance()
        val unit = if (config.unit == SpeedUnit.BPS) "BPS" else "km/h"
        val fmt = "%." + config.decimals + "f"
        context.drawText(client.textRenderer, "Speed: ${String.format(fmt, snap.speed)} $unit", 2, 38, 0xFF_FFFFFF, false)
    }
}
