package com.pulsehud.modules

import com.pulsehud.modules.ModuleCategory.MISC
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

enum class SessionFormat { HHMMSS, MINUTES }

data class SessionTimerConfig : ModuleConfig() {
    var format: SessionFormat = SessionFormat.HHMMSS
}

data class SessionTimerSnapshot : ModuleSnapshot() {
    var seconds: Int = 0
    var active: Boolean = false
}

class SessionTimerModule : Module<SessionTimerConfig>() {
    override val id = "23"
    override val category = MISC
    override val defaultEnabled = false
    override val config: SessionTimerConfig = SessionTimerConfig()

    private var startTick: Long = 0

    override fun update(client: MinecraftClient): ModuleSnapshot {
        val snap = SessionTimerSnapshot()
        val p = client.player
        if (p == null) {
            startTick = 0
            snap.active = false
            return snap
        }
        if (startTick == 0) startTick = p.tickCount
        val seconds = ((p.tickCount - startTick) / 20).toInt()
        snap.seconds = seconds
        snap.active = true
        return snap
    }

    override fun render(context: DrawContext, snapshot: ModuleSnapshot, scale: Float) {
        val snap = snapshot as SessionTimerSnapshot
        if (!snap.active) return
        val client = MinecraftClient.getInstance()
        val text = when (config.format) {
            SessionFormat.HHMMSS -> "${snap.seconds / 3600}:${(snap.seconds % 3600) / 60}".let { h ->
                "$h:${String.format("%02d", snap.seconds % 60)}"
            }
            SessionFormat.MINUTES -> "${snap.seconds / 60} min"
        }
        context.drawText(client.textRenderer, "Session: $text", 2, 170, 0xFF_FFFFFF, false)
    }
}
