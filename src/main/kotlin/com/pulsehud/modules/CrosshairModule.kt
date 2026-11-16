package com.pulsehud.modules

import com.pulsehud.modules.ModuleCategory.HUD
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

enum class CrosshairStyle { PLUS, DOT, CROSS, CIRCLE, T }

data class CrosshairConfig : ModuleConfig() {
    var style: CrosshairStyle = CrosshairStyle.PLUS
    var color: Int = 0xFF_00FF00
    var size: Int = 10
    var thickness: Int = 2
    var gap: Int = 4
    var outline: Boolean = false
    var outlineColor: Int = 0xFF_000000
    var dynamicSpread: Boolean = true
    var hideF3: Boolean = true
}

data class CrosshairSnapshot : ModuleSnapshot() {
    var spread: Float = 0f
}

class CrosshairModule : Module<CrosshairConfig>() {
    override val id = "1"
    override val category = HUD
    override val defaultEnabled = true
    override val config: CrosshairConfig = CrosshairConfig()

    private val prevX = FloatArray(1)
    private val prevY = FloatArray(1)

    override fun update(client: MinecraftClient): ModuleSnapshot {
        val snap = CrosshairSnapshot()
        if (config.dynamicSpread) {
            val player = client.player
            val moving = Math.abs(player.x - player.lastRenderX) > 0.001 || Math.abs(player.z - player.lastRenderZ) > 0.001
            val target = if (moving || player.isSprinting) 0.6f else 0f
            snap.spread = target
        }
        return snap
    }

    override fun render(context: DrawContext, snapshot: ModuleSnapshot, scale: Float) {
        val snap = snapshot as CrosshairSnapshot
        val client = MinecraftClient.getInstance()
        if (config.hideF3 && client.options.debugEnabled) return
        val c = config
        val cx = (client.getWindow().getFramebufferWidth() / 2.0f).toInt()
        val cy = (client.getWindow().getFramebufferHeight() / 2.0f).toInt()
        val s = (c.size * scale).coerceAtLeast(1).toInt()
        val t = (c.thickness * scale).coerceAtLeast(1).toInt()
        val g = (c.gap * scale).toInt()
        val spread = (snap.spread * s).toInt()
        val color = c.color
        val oc = c.outlineColor
        fun line(x1: Int, y1: Int, x2: Int, y2: Int) {
            context.fill(x1, y1, x2, y2, color)
            if (c.outline) {
                context.fill(x1 - 1, y1 - 1, x2 + 1, y2 + 1, oc)
            }
        }
        when (c.style) {
            CrosshairStyle.PLUS -> {
                line(cx - s - g - spread, cy - t / 2, cx - g - spread, cy + t / 2)
                line(cx + g + spread, cy - t / 2, cx + s + g + spread, cy + t / 2)
                line(cx - t / 2, cy - s - g - spread, cx + t / 2, cy - g - spread)
                line(cx - t / 2, cy + g + spread, cx + t / 2, cy + s + g + spread)
            }
            CrosshairStyle.DOT -> {
                line(cx - t / 2, cy - t / 2, cx + t / 2, cy + t / 2)
            }
            CrosshairStyle.CROSS -> {
                line(cx - s - g - spread, cy - s - g - spread, cx - g - spread, cy - g - spread)
                line(cx + g + spread, cy + g + spread, cx + s + g + spread, cy + s + g + spread)
                line(cx - s - g - spread, cy + g + spread, cx - g - spread, cy + s + g + spread)
                line(cx + g + spread, cy - s - g - spread, cx + s + g + spread, cy - g - spread)
            }
            CrosshairStyle.CIRCLE -> {
                val r = s + spread
                for (i in 0 until 360 step 15) {
                    val a = i * kotlin.math.PI / 180
                    val x = (cx + kotlin.math.cos(a).toFloat() * r).toInt()
                    val y = (cy + kotlin.math.sin(a).toFloat() * r).toInt()
                    line(x, y, x + t, y + t)
                }
            }
            CrosshairStyle.T -> {
                line(cx - s - g - spread, cy - t / 2, cx + s + g + spread, cy + t / 2)
                line(cx - t / 2, cy + g + spread, cx + t / 2, cy + s + g + spread)
            }
        }
    }
}
