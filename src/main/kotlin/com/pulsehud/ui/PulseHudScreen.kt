package com.pulsehud.ui

import com.pulsehud.config.ConfigManager
import com.pulsehud.modules.Module
import com.pulsehud.modules.ModuleCategory
import com.pulsehud.modules.ModuleRegistry
import com.pulsehud.theme.Preset
import com.pulsehud.theme.Theme
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.CheckboxWidget
import net.minecraft.client.gui.widget.SliderWidget
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.client.option.KeyBinding
import net.minecraft.text.Text
import org.slf4j.LoggerFactory

class PulseHudScreen(
    private val registry: ModuleRegistry,
    private val configManager: ConfigManager,
    private val onClose: () -> Unit
) : Screen(Text.literal("PulseHUD Settings")) {

    private val logger = LoggerFactory.getLogger("pulsehud")
    private var currentTab: ModuleCategory = ModuleCategory.HUD
    private var selectedModule: Module<*>? = null
    private var search = ""
    private var dirty = false
    private var closing = false

    private lateinit var searchField: TextFieldWidget
    private val tabButtons = mutableListOf<ButtonWidget>()
    private val moduleList = mutableListOf<ModuleRow>()

    override fun init() {
        val w = width
        val h = height
        val sbX = 10
        val sbW = 200
        searchField = TextFieldWidget(textRenderer, sbX, 30, sbW, 20, Text.literal("search"))
        searchField.setChangedListener { search = it.text }
        addDrawableChild(searchField)
        val tabs = listOf(ModuleCategory.HUD, ModuleCategory.COMBAT, ModuleCategory.VISUALS, ModuleCategory.MISC, ModuleCategory.SETTINGS)
        var tx = sbX
        for (t in tabs) {
            val b = ButtonWidget(tx, 60, 40, 20, Text.literal(t.name)) { currentTab = t; refreshRows() }
            addDrawableChild(b)
            tabButtons.add(b)
            tx += 46
        }
        val edit = ButtonWidget(sbX, h - 30, 100, 20, Text.literal("Edit HUD Layout")) { openLayoutEditor() }
        addDrawableChild(edit)
        val reset = ButtonWidget(sbX + 110, h - 30, 100, 20, Text.literal("Reset to defaults")) { confirmReset() }
        addDrawableChild(reset)
        refreshRows()
    }

    private fun refreshRows() {
        moduleList.clear()
        val children = mutableListOf<Any>()
        var y = 100
        for (m in registry.allModules().filter { it.category == currentTab }) {
            if (search.isNotEmpty() && !m.id.contains(search, ignoreCase = true)) continue
            val row = ModuleRow(m, y)
            moduleList.add(row)
            children.add(row.toggle)
            children.add(row.expand)
            y += 24
        }
        selectedModule = null
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, tickDelta: Float) {
        context.fill(0, 0, width, height, 0x7F_000000)
        context.fill(10, 10, 220, height - 10, 0xFF_1A1A1A)
        context.drawText(textRenderer, "PulseHUD", 20, 20, 0xFF_FFFFFF, false)
        searchField.render(context, mouseX, mouseY, tickDelta)
        for (row in moduleList) {
            row.render(context, mouseX, mouseY, tickDelta)
        }
        if (selectedModule != null) {
            renderSettingsPane(context, selectedModule!!)
        }
        super.render(context, mouseX, mouseY, tickDelta)
    }

    private fun renderSettingsPane(context: DrawContext, m: Module<*>) {
        context.fill(240, 10, width - 10, height - 10, 0xFF_1A1A1A)
        context.drawText(textRenderer, m.id, 250, 20, 0xFF_FFFFFF, false)
        val toggle = CheckboxWidget(250, 40, 20, 20, Text.literal("Enabled"), m.config.enabled)
        toggle.setValue(m.config.enabled)
        toggle.onPress = { registry.setEnabled(m.id, toggle.isSelected); dirty = true }
        addDrawableChild(toggle)
        context.drawText(textRenderer, "Enabled", 275, 44, 0xFF_FFFFFF, false)
        var yy = 70
        renderModuleSettings(m, context, 250, yy)
    }

    private fun renderModuleSettings(m: Module<*>, context: DrawContext, x: Int, y: Int) {
        when (m) {
            is com.pulsehud.modules.FpsModule -> renderBool(m, context, x, y, "Show min/max", { m.config.showMinMax }, { v -> m.config.showMinMax = v; dirty = true })
            is com.pulsehud.modules.PingModule -> {
                renderInt(m, context, x, y, "Warn threshold", { m.config.warnThreshold }, { v -> m.config.warnThreshold = v; dirty = true }, 0, 1000)
                renderInt(m, context, x, y + 24, "Crit threshold", { m.config.critThreshold }, { v -> m.config.critThreshold = v; dirty = true }, 0, 1000)
                renderBool(m, context, x, y + 48, "Show bar", { m.config.showBar }, { v -> m.config.showBar = v; dirty = true })
            }
            is com.pulsehud.modules.SpeedModule -> renderEnum(m, context, x, y, "Unit", arrayOf("BPS", "km/h"), { m.config.unit.ordinal }, { v -> m.config.unit = com.pulsehud.modules.SpeedUnit.values()[v]; dirty = true })
            is com.pulsehud.modules.CrosshairModule -> {
                renderEnum(m, context, x, y, "Style", arrayOf("plus", "dot", "cross", "circle", "T"), { m.config.style.ordinal }, { v -> m.config.style = com.pulsehud.modules.CrosshairStyle.values()[v]; dirty = true })
                renderColor(m, context, x, y + 24, "Color", { m.config.color }, { v -> m.config.color = v; dirty = true })
                renderInt(m, context, x, y + 48, "Size", { m.config.size }, { v -> m.config.size = v.coerceIn(1, 20); dirty = true }, 1, 20)
            }
            else -> context.drawText(textRenderer, "No settings", x, y, 0xFF_B0B0B0, false)
        }
    }

    private fun renderBool(m: Module<*>, context: DrawContext, x: Int, y: Int, label: String, get: () -> Boolean, set: (Boolean) -> Unit) {
        val cb = CheckboxWidget(x, y, 20, 20, Text.literal(label), get())
        cb.setValue(get())
        cb.onPress = { set(cb.isSelected); dirty = true }
        addDrawableChild(cb)
        context.drawText(textRenderer, label, x + 24, y + 4, 0xFF_FFFFFF, false)
    }

    private fun renderInt(m: Module<*>, context: DrawContext, x: Int, y: Int, label: String, get: () -> Int, set: (Int) -> Unit, min: Int, max: Int) {
        context.drawText(textRenderer, "$label: ${get()}", x, y, 0xFF_FFFFFF, false)
        val slider = SliderWidget(x, y + 10, 100, 20, Text.literal(label), 0f, 1f, 0f)
        slider.setValue(((get() - min).toFloat() / (max - min)).coerceIn(0f, 1f))
        slider.onValueChanged = { v -> set(min + ((v * (max - min)).toInt())); dirty = true }
        addDrawableChild(slider)
    }

    private fun renderEnum(m: Module<*>, context: DrawContext, x: Int, y: Int, label: String, options: Array<String>, get: () -> Int, set: (Int) -> Unit) {
        context.drawText(textRenderer, "$label: ${options[get()]}", x, y, 0xFF_FFFFFF, false)
        val b = ButtonWidget(x, y + 10, 100, 20, Text.literal(options[get()])) {
            val n = (get() + 1) % options.size
            set(n)
            dirty = true
            b.setMessage(Text.literal(options[n]))
        }
        addDrawableChild(b)
    }

    private fun renderColor(m: Module<*>, context: DrawContext, x: Int, y: Int, label: String, get: () -> Int, set: (Int) -> Unit) {
        context.drawText(textRenderer, "$label: ${get().toHexString()}", x, y, 0xFF_FFFFFF, false)
        val b = ButtonWidget(x, y + 10, 100, 20, Text.literal("edit")) {
            val cur = get()
            val r = (cur shr 16 and 0xFF)
            val g = (cur shr 8 and 0xFF)
            val bb = (cur and 0xFF)
            val nr = (r + 10) % 256
            val ng = (g + 10) % 256
            val nb = (bb + 10) % 256
            set((0xFF shl 24) or (nr shl 16) or (ng shl 8) or nb)
            dirty = true
        }
        addDrawableChild(b)
    }

    private fun openLayoutEditor() {
        client?.setScreen(LayoutEditorScreen(registry, configManager, { this@PulseHudScreen.client?.setScreen(this@PulseHudScreen) }))
    }

    private fun confirmReset() {
    }

    override fun keyPressed(key: Int, scanCode: Int, modifiers: Int): Boolean {
        if (key == 256) { closeScreen(); return true }
        return super.keyPressed(key, scanCode, modifiers)
    }

    private fun closeScreen() {
        if (dirty && !closing) {
            closing = true
            client?.setScreen(ConfirmScreen(Text.literal("Unsaved changes"), Text.literal("Save, discard or cancel?"),
                listOf("Save", "Discard", "Cancel")) { choice ->
                when (choice) {
                    "Save" -> { configManager.save(config); dirty = false }
                    "Discard" -> dirty = false
                }
                client?.setScreen(this@PulseHudScreen)
                closing = false
            })
        } else {
            dirty = false
            client?.setScreen(null)
            onClose()
        }
    }

    private class ModuleRow(
        val module: Module<*>,
        val y: Int
    ) {
        val toggle = CheckboxWidget(30, 0, 20, 20, Text.literal(""), module.config.enabled)
        val expand = ButtonWidget(50, 0, 20, 20, Text.literal("+")) {}
        var expanded = false
    }
}
