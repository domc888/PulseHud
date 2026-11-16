package com.pulsehud.ui

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.text.Text

class ConfirmScreen(
    title: Text,
    message: Text,
    private val choices: List<String>,
    private val onChoice: (String) -> Unit
) : Screen(title) {

    override fun init() {
        var y = height / 2 - 20
        for (c in choices) {
            val b = ButtonWidget(width / 2 - 50, y, 100, 20, Text.literal(c)) { onChoice(c) }
            addDrawableChild(b)
            y += 24
        }
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, tickDelta: Float) {
        context.fill(0, 0, width, height, 0x7F_000000)
        context.drawText(textRenderer, title.getString(), width / 2 - textRenderer.getWidth(title.getString()) / 2, height / 2 - 40, 0xFF_FFFFFF, false)
        context.drawText(textRenderer, message.getString(), width / 2 - textRenderer.getWidth(message.getString()) / 2, height / 2 - 20, 0xFF_FFFFFF, false)
        super.render(context, mouseX, mouseY, tickDelta)
    }
}
