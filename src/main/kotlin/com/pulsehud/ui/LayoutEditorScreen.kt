package com.pulsehud.ui

import com.pulsehud.config.ConfigManager
import com.pulsehud.modules.Module
import com.pulsehud.modules.ModuleRegistry
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.text.Text

class LayoutEditorScreen(
    private val registry: ModuleRegistry,
    private val configManager: ConfigManager,
    private val onClose: () -> Unit
) : Screen(Text.literal("Edit HUD Layout")) {

    private var selected: Module<*>? = null
    private lateinit var anchorField: TextFieldWidget
    private lateinit var offsetXField: TextFieldWidget
    private lateinit var offsetYField: TextFieldWidget

    override fun init() {
        var y = 30
        for (m in registry.enabledModules()) {
            val b = ButtonWidget(20, y, 140, 20, Text.literal("${m.id}: ${m.config.enabled}")) { selected = m; refreshFields() }
            addDrawableChild(b)
            y += 24
        }
        val done = ButtonWidget(20, height - 30, 80, 20, Text.literal("Done")) { onClose(); client?.setScreen(null) }
        addDrawableChild(done)
        anchorField = TextFieldWidget(textRenderer, 180, 30, 100, 20, Text.literal("anchor"))
        offsetXField = TextFieldWidget(textRenderer, 180, 60, 100, 20, Text.literal("offsetX"))
        offsetYField = TextFieldWidget(textRenderer, 180, 90, 100, 20, Text.literal("offsetY"))
        addDrawableChild(anchorField)
        addDrawableChild(offsetXField)
        addDrawableChild(offsetYField)
        val apply = ButtonWidget(180, 120, 100, 20, Text.literal("Apply")) { applyFields() }
        addDrawableChild(apply)
        refreshFields()
    }

    private fun refreshFields() {
        val s = registry.moduleState(selected?.id ?: "")
        anchorField.setText(s.anchor)
        offsetXField.setText(s.offsetX.toString())
        offsetYField.setText(s.offsetY.toString())
    }

    private fun applyFields() {
        selected?.let { m ->
            val ax = anchorField.text
            val ox = offsetXField.text.toIntOrNull() ?: 0
            val oy = offsetYField.text.toIntOrNull() ?: 0
            registry.setPosition(m.id, ax, ox, oy)
            configManager.save(configManager.load())
        }
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, tickDelta: Float) {
        context.fill(0, 0, width, height, 0x7F_000000)
        context.drawText(textRenderer, "HUD Layout (drag = snap 5px)", 20, 10, 0xFF_FFFFFF, false)
        anchorField.render(context, mouseX, mouseY, tickDelta)
        offsetXField.render(context, mouseX, mouseY, tickDelta)
        offsetYField.render(context, mouseX, mouseY, tickDelta)
        super.render(context, mouseX, mouseY, tickDelta)
    }
}
