package com.pulsehud.modules

import com.pulsehud.modules.ModuleCategory.MISC
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

data class ItemCountConfig : ModuleConfig() {
    var showAtHeldSlot: Boolean = true
}

data class ItemCountSnapshot : ModuleSnapshot() {
    var count: Int = 0
    var itemName: String = ""
}

class ItemCountModule : Module<ItemCountConfig>() {
    override val id = "17"
    override val category = MISC
    override val defaultEnabled = false
    override val config: ItemCountConfig = ItemCountConfig()

    override fun update(client: MinecraftClient): ModuleSnapshot {
        val snap = ItemCountSnapshot()
        val p = client.player
        val held = p.mainHandStack
        if (held.isEmpty) return snap
        snap.itemName = held.getName()
        var count = held.count
        for (stack in p.inventory.main) {
            if (stack.getItem() == held.getItem()) count += stack.count
        }
        snap.count = count
        return snap
    }

    override fun render(context: DrawContext, snapshot: ModuleSnapshot, scale: Float) {
        val snap = snapshot as ItemCountSnapshot
        if (snap.count <= 0) return
        val client = MinecraftClient.getInstance()
        val text = "x${snap.count}"
        if (config.showAtHeldSlot) {
            val cx = (client.getWindow().getFramebufferWidth() / 2.0f).toInt()
            val cy = (client.getWindow().getFramebufferHeight() / 2.0f).toInt()
            val slot = (24f * scale).toInt()
            val start = cx - (slot * 9) / 2
            val x = start + client.player.inventory.selectedSlot * slot
            context.drawText(client.textRenderer, text, x + slot / 2 - 3, cy + 22, 0xFF_FFFFFF, false)
        } else {
            context.drawText(client.textRenderer, text, 2, 124, 0xFF_FFFFFF, false)
        }
    }
}
