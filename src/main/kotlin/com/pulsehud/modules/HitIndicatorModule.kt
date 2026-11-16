package com.pulsehud.modules

import com.pulsehud.modules.ModuleCategory.COMBAT
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

enum class HitStyle { CROSSHAIR_TINT, ICON, BOTH }

data class HitIndicatorConfig : ModuleConfig() {
    var flashColor: Int = 0xFF_FF0000
    var duration: Int = 150
    var style: HitStyle = HitStyle.BOTH
}

data class HitIndicatorSnapshot : ModuleSnapshot() {
    var flashing: Boolean = false
    var endTick: Long = 0
}

class HitIndicatorModule : Module<HitIndicatorConfig>() {
    override val id = "14"
    override val category = COMBAT
    override val defaultEnabled = false
    override val config: HitIndicatorConfig = HitIndicatorConfig()

    private var flashing = false
    private var endTick = 0L

    fun flash(tick: Long) {
        flashing = true
        endTick = tick + config.duration.toLong() * 20L / 1000L
    }

    override fun update(client: MinecraftClient): ModuleSnapshot {
        val snap = HitIndicatorSnapshot()
        val now = client.player?.tickCount ?: 0L
        if (flashing && now >= endTick) flashing = false
        snap.flashing = flashing
        snap.endTick = endTick
        return snap
    }

    override fun render(context: DrawContext, snapshot: ModuleSnapshot, scale: Float) {
        val snap = snapshot as HitIndicatorSnapshot
        if (!snap.flashing) return
        val client = MinecraftClient.getInstance()
        val cx = (client.getWindow().getFramebufferWidth() / 2.0f).toInt()
        val cy = (client.getWindow().getFramebufferHeight() / 2.0f).toInt()
        val s = (10f * scale).toInt()
        if (config.style == HitStyle.CROSSHAIR_TINT || config.style == HitStyle.BOTH) {
            context.fill(cx - s, cy - 2, cx + s, cy + 2, config.flashColor)
            context.fill(cx - 2, cy - s, cx + 2, cy + s, config.flashColor)
        }
        if (config.style == HitStyle.ICON || config.style == HitStyle.BOTH) {
            context.drawText(client.textRenderer, "!", cx - 3, cy - 8, config.flashColor, false)
        }
    }
}
