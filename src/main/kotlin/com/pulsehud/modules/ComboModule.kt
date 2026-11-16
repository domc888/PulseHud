package com.pulsehud.modules

import com.pulsehud.modules.ModuleCategory.COMBAT
import com.pulsehud.util.ComboTracker
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

data class ComboConfig : ModuleConfig() {
    var resetTimeout: Int = 3
}

data class ComboSnapshot : ModuleSnapshot() {
    var combo: Int = 0
}

class ComboModule : Module<ComboConfig>() {
    override val id = "10"
    override val category = COMBAT
    override val defaultEnabled = false
    override val config: ComboConfig = ComboConfig()

    private val tracker = ComboTracker(0)

    fun hit(tick: Long) {
        tracker.hit(tick)
    }

    override fun update(client: MinecraftClient): ModuleSnapshot {
        val snap = ComboSnapshot()
        tracker.resetTimeoutTicks = config.resetTimeout.toLong() * 20
        snap.combo = tracker.tick(client.player?.tickCount ?: 0L)
        return snap
    }

    override fun render(context: DrawContext, snapshot: ModuleSnapshot, scale: Float) {
        val snap = snapshot as ComboSnapshot
        val client = MinecraftClient.getInstance()
        context.drawText(client.textRenderer, "Combo: ${snap.combo}", 2, 92, 0xFF_FF00FF, false)
    }
}
