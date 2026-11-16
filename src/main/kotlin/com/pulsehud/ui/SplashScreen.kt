package com.pulsehud.ui

import com.pulsehud.theme.Preset
import com.pulsehud.theme.Theme
import com.pulsehud.util.HexGrid
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import net.minecraft.client.MinecraftClient

class SplashScreen(
    private val theme: Theme,
    private val onSettings: () -> Unit
) : Screen(Text.literal("PulseHUD")) {

    private val grid = HexGrid()
    private var startTime: Long = -1
    private var hovered: IntPair? = null

    override fun init() {
        startTime = if (startTime < 0) MinecraftClient.getInstance().player?.tickCount ?: 0 else startTime
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, tickDelta: Float) {
        context.fill(0, 0, width, height, theme.activeTokens().panelBackground)
        val client = MinecraftClient.getInstance()
        val scale = client.options.guiScale.toFloat()
        val size = (28f * scale / 2f).coerceAtLeast(1f)
        val cx = width / 2f
        val cy = height / 2f
        val tokens = theme.activeTokens()
        // center
        drawHex(context, cx, cy, size * 2.2f, tokens.panelBackground, tokens.accent, 2f)
        context.drawText(client.textRenderer, "PulseHUD", cx - textRenderer.getWidth("PulseHUD") / 2, cy - 10, tokens.textPrimary, false)
        context.drawText(client.textRenderer, "1.0.0", cx - textRenderer.getWidth("1.0.0") / 2, cy + 6, tokens.textSecondary, false)
        // ring + outer
        val dirs = grid.neighborsOfOrigin()
        for (d in dirs) {
            val p = grid.axialToPixel(d.first, d.second, size)
            drawHex(context, cx + p.first, cy + p.second, size, blend(tokens.accent, 0.10f), tokens.accent, 0f)
        }
        for (q in -2..2) for (r in -2..2) {
            val dist = ringDistance(q, r)
            if (dist == 2) {
                val p = grid.axialToPixel(q, r, size)
                drawHex(context, cx + p.first, cy + p.second, size, blend(tokens.gridColor, 0.06f), 0, 0f)
            }
        }
        super.render(context, mouseX, mouseY, tickDelta)
    }

    override fun keyPressed(key: Int, scanCode: Int, modifiers: Int): Boolean {
        if (key == 257 || key == 335) { onSettings(); return true }
        if (key == 256) { client?.setScreen(null); return true }
        return super.keyPressed(key, scanCode, modifiers)
    }

    private fun drawHex(context: DrawContext, x: Float, y: Float, size: Float, fill: Int, border: Int, borderWidth: Float) {
        val verts = com.pulsehud.util.HexGrid().pointyTopVertices(x, y, size)
        for (i in 0 until 6) {
            val x1 = verts[i * 2].toInt()
            val y1 = verts[i * 2 + 1].toInt()
            val x2 = verts[((i + 1) % 6) * 2].toInt()
            val y2 = verts[((i + 1) % 6) * 2 + 1].toInt()
            context.fill(x1, y1, x2, y2, fill)
        }
    }

    private fun ringDistance(q: Int, r: Int): Int = (Math.max(Math.abs(q), Math.max(Math.abs(r), Math.abs(-q - r))))

    private fun blend(color: Int, alpha: Float): Int {
        val a = (255 * alpha).toInt()
        return (a shl 24) or (color and 0xFFFFFF)
    }

    private data class IntPair(val first: Int, val second: Int)
}
