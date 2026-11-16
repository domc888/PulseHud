package com.pulsehud.modules

import com.pulsehud.modules.ModuleCategory.MISC
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

data class HotbarKeysConfig : ModuleConfig() {
    var labelColor: Int = 0xFF_FFFFFF
    var highlightColor: Int = 0xFF_00FF00
    var show: Boolean = true
}

data class HotbarKeysSnapshot : ModuleSnapshot() {
    var heldSlot: Int = 0
}

class HotbarKeysModule : Module<HotbarKeysConfig>() {
    override val id = "16"
    override val category = MISC
    override val defaultEnabled = false
    override val config: HotbarKeysConfig = HotbarKeysConfig()

    override fun update(client: MinecraftClient): ModuleSnapshot {
        val snap = HotbarKeysSnapshot()
        snap.heldSlot = client.player?.inventory?.selectedSlot ?: 0
        return snap
    }

    override fun render(context: DrawContext, snapshot: ModuleSnapshot, scale: Float) {
        if (!config.show) return
        val snap = snapshot as HotbarKeysSnapshot
        val client = MinecraftClient.getInstance()
        val cx = (client.getWindow().getFramebufferWidth() / 2.0f).toInt()
        val cy = (client.getWindow().getFramebufferHeight() / 2.0f).toInt()
        val slot = (24f * scale).toInt()
        val gap = (4f * scale).toInt()
        val start = cx - (slot * 9 + gap * 8) / 2
        val y = cy + 30
        for (i in 0 until 9) {
            val x = start + i * (slot + gap)
            val color = if (i == snap.heldSlot) config.highlightColor else config.labelColor
            context.drawText(client.textRenderer, (i + 1).toString(), x + slot / 2 - 3, y, color, false)
        }
    }
}
