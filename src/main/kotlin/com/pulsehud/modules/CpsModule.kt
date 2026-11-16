package com.pulsehud.modules

import com.pulsehud.modules.ModuleCategory.COMBAT
import com.pulsehud.util.CpsWindow
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

data class CpsConfig : ModuleConfig() {
}

data class CpsSnapshot : ModuleSnapshot() {
    var left: Int = 0
    var right: Int = 0
}

class CpsModule : Module<CpsConfig>() {
    override val id = "4"
    override val category = COMBAT
    override val defaultEnabled = false
    override val config: CpsConfig = CpsConfig()

    private val leftWindow = CpsWindow(20)
    private val rightWindow = CpsWindow(20)

    override fun update(client: MinecraftClient): ModuleSnapshot {
        val snap = CpsSnapshot()
        val now = client.player?.tickCount ?: 0L
        val opts = client.options
        leftWindow.tick(now, opts.attackKey.isPressed)
        rightWindow.tick(now, opts.useItem.isPressed)
        snap.left = leftWindow.count()
        snap.right = rightWindow.count()
        return snap
    }

    override fun render(context: DrawContext, snapshot: ModuleSnapshot, scale: Float) {
        val snap = snapshot as CpsSnapshot
        val client = MinecraftClient.getInstance()
        context.drawText(client.textRenderer, "L: ${snap.left} | R: ${snap.right}", 2, 26, 0xFF_FFFFFF, false)
    }
}
