package com.pulsehud.modules

import com.pulsehud.modules.ModuleCategory.MISC
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import kotlin.math.floor

data class MemoryConfig : ModuleConfig() {
    var showMax: Boolean = true
    var warnColor: Int = 0xFF_FF0000
}

data class MemorySnapshot : ModuleSnapshot() {
    var used: Double = 0
    var max: Double = 0
}

class MemoryModule : Module<MemoryConfig>() {
    override val id = "19"
    override val category = MISC
    override val defaultEnabled = false
    override val config: MemoryConfig = MemoryConfig()

    private var lastTick = 0L

    override fun update(client: MinecraftClient): ModuleSnapshot {
        val snap = MemorySnapshot()
        val now = client.player?.tickCount ?: 0L
        if (now - lastTick < 20) {
            snap.used = -1
            return snap
        }
        lastTick = now
        val rt = java.lang.Runtime.getRuntime()
        snap.used = (rt.totalMemory - rt.freeMemory) / 1024.0 / 1024.0 / 1024.0
        snap.max = rt.maxMemory / 1024.0 / 1024.0 / 1024.0
        return snap
    }

    override fun render(context: DrawContext, snapshot: ModuleSnapshot, scale: Float) {
        val snap = snapshot as MemorySnapshot
        if (snap.used < 0) return
        val client = MinecraftClient.getInstance()
        val text = if (config.showMax) "MEM ${"%.1f".format(snap.used)}/${"%.1f".format(snap.max)} GB" else "MEM ${"%.1f".format(snap.used)} GB"
        val pct = snap.used / snap.max
        val color = if (pct > 0.85) config.warnColor else 0xFF_FFFFFF
        context.drawText(client.textRenderer, text, 2, 136, color, false)
    }
}
