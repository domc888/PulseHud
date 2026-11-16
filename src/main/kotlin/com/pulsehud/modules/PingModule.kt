package com.pulsehud.modules

import com.pulsehud.modules.ModuleCategory.HUD
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.entity.player.PlayerEntity

sealed class PingColor { object Green : PingColor(); object Yellow : PingColor(); object Red : PingColor() }

data class PingConfig : ModuleConfig() {
    var warnThreshold: Int = 80
    var critThreshold: Int = 150
    var showBar: Boolean = false
}

data class PingSnapshot : ModuleSnapshot() {
    var latency: Int = -1
    var color: PingColor = PingColor.Green
}

class PingModule : Module<PingConfig>() {
    override val id = "3"
    override val category = HUD
    override val defaultEnabled = false
    override val config: PingConfig = PingConfig()

    companion object {
        fun bucket(latency: Int, warn: Int, crit: Int): PingColor = when {
            latency < 0 -> PingColor.Green
            latency < warn -> PingColor.Green
            latency < crit -> PingColor.Yellow
            else -> PingColor.Red
        }
    }

    override fun update(client: MinecraftClient): ModuleSnapshot {
        val snap = PingSnapshot()
        val entry = client.getPlayerListEntry(client.player?.getUuid())
        snap.latency = entry?.latency?.let { it.toInt() } ?: -1
        snap.color = when {
            snap.latency < 0 -> PingColor.Green
            snap.latency < config.warnThreshold -> PingColor.Green
            snap.latency < config.critThreshold -> PingColor.Yellow
            else -> PingColor.Red
        }
        return snap
    }

    override fun render(context: DrawContext, snapshot: ModuleSnapshot, scale: Float) {
        val snap = snapshot as PingSnapshot
        val client = MinecraftClient.getInstance()
        val text = if (snap.latency < 0) "Ping: N/A" else "Ping: ${snap.latency} ms"
        val color = when (snap.color) {
            PingColor.Green -> 0xFF_00FF00
            PingColor.Yellow -> 0xFF_FFFF00
            PingColor.Red -> 0xFF_FF0000
        }
        context.drawText(client.textRenderer, text, 2, 14, color, false)
        if (config.showBar && snap.latency >= 0) {
            val w = (snap.latency.coerceAtMost(300) * 2).toInt()
            context.fill(2, 24, 2 + w, 28, color)
        }
    }
}
