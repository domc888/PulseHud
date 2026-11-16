package com.pulsehud.modules

import com.pulsehud.modules.ModuleCategory.HUD
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.item.ItemStack

data class ArmorConfig : ModuleConfig() {
}

data class ArmorEntry(val name: String, val damage: Int, val maxDamage: Int)

data class ArmorSnapshot : ModuleSnapshot() {
    var armor: List<ArmorEntry> = emptyList()
    var held: ArmorEntry? = null
}

class ArmorModule : Module<ArmorConfig>() {
    override val id = "8"
    override val category = HUD
    override val defaultEnabled = false
    override val config: ArmorConfig = ArmorConfig()

    override fun update(client: MinecraftClient): ModuleSnapshot {
        val snap = ArmorSnapshot()
        val p = client.player
        snap.armor = p.armor.map { stack -> entry(stack) }
        snap.held = entry(p.mainHandStack)
        return snap
    }

    private fun entry(stack: ItemStack): ArmorEntry = ArmorEntry(stack.getName(), stack.damage, stack.maxDamage)

    override fun render(context: DrawContext, snapshot: ModuleSnapshot, scale: Float) {
        val snap = snapshot as ArmorSnapshot
        val client = MinecraftClient.getInstance()
        var y = 62
        fun row(label: String, entry: ArmorEntry?) {
            if (entry == null || entry.maxDamage <= 0) return
            val pct = 1.0 - entry.damage.toFloat() / entry.maxDamage
            val color = 0xFF_00FF00
            context.drawText(client.textRenderer, "$label ${entry.name}", 2, y, 0xFF_FFFFFF, false)
            context.fill(2, y + 10, 2 + (50 * pct).toInt(), y + 14, color)
            y += 20
        }
        snap.armor.forEachIndexed { i, e -> row("A$i:", e) }
        row("Held:", snap.held)
    }
}
