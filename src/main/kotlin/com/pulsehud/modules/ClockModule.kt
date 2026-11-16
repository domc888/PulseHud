package com.pulsehud.modules

import com.pulsehud.modules.ModuleCategory.MISC
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import java.time.LocalTime
import java.time.format.DateTimeFormatter

enum class TimeFormat { H12, H24 }

data class ClockConfig : ModuleConfig() {
    var format: TimeFormat = TimeFormat.H24
}

data class ClockSnapshot : ModuleSnapshot() {
    var time: String = ""
}

class ClockModule : Module<ClockConfig>() {
    override val id = "11"
    override val category = MISC
    override val defaultEnabled = false
    override val config: ClockConfig = ClockConfig()

    override fun update(client: MinecraftClient): ModuleSnapshot {
        val snap = ClockSnapshot()
        val now = LocalTime.now()
        snap.time = when (config.format) {
            TimeFormat.H24 -> now.format(DateTimeFormatter.ofPattern("HH:mm"))
            TimeFormat.H12 -> now.format(DateTimeFormatter.ofPattern("hh:mm a"))
        }
        return snap
    }

    override fun render(context: DrawContext, snapshot: ModuleSnapshot, scale: Float) {
        val snap = snapshot as ClockSnapshot
        val client = MinecraftClient.getInstance()
        context.drawText(client.textRenderer, snap.time, 2, 102, 0xFF_FFFFFF, false)
    }
}
