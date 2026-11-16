package com.pulsehud.modules

import com.pulsehud.modules.ModuleCategory.HUD
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

data class FpsConfig : ModuleConfig() {
    var showMinMax: Boolean = true
}

data class FpsSnapshot : ModuleSnapshot() {
    var fps: Float = 0f
    var min: Float = 0f
    var max: Float = 0f
}

class FpsModule : Module<FpsConfig>() {
    override val id = "2"
    override val category = HUD
    override val defaultEnabled = true
    override val config: FpsConfig = FpsConfig()

    private val buffer = FloatArray(600)
    private var idx = 0
    private var count = 0

    override fun update(client: MinecraftClient): ModuleSnapshot {
        val snap = FpsSnapshot()
        val fps = client.currentFps
        snap.fps = fps
        buffer[idx] = fps
        idx = (idx + 1) % buffer.size
        count = (count + 1).coerceAtMost(buffer.size)
        var mn = Float.MAX_VALUE
        var mx = -Float.MAX_VALUE
        for (i in 0 until count) {
            val v = buffer[i]
            if (v < mn) mn = v
            if (v > mx) mx = v
        }
        snap.min = if (mn == Float.MAX_VALUE) 0f else mn
        snap.max = if (mx == -Float.MAX_VALUE) 0f else mx
        return snap
    }

    override fun render(context: DrawContext, snapshot: ModuleSnapshot, scale: Float) {
        val snap = snapshot as FpsSnapshot
        val client = MinecraftClient.getInstance()
        val text = if (config.showMinMax) "FPS ${snap.fps.toInt()} [${snap.min.toInt()}-${snap.max.toInt()}]" else "FPS ${snap.fps.toInt()}"
        context.drawText(client.textRenderer, text, 2, 2, 0xFF_FFFFFF, false)
    }
}
