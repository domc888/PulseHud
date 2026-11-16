package com.pulsehud.modules

import com.pulsehud.modules.ModuleCategory.MISC
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

enum class Corner { TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT }

data class BiomeConfig : ModuleConfig() {
    var show: Boolean = true
    var position: Corner = Corner.TOP_LEFT
}

data class BiomeSnapshot : ModuleSnapshot() {
    var name: String = ""
}

class BiomeModule : Module<BiomeConfig>() {
    override val id = "25"
    override val category = MISC
    override val defaultEnabled = false
    override val config: BiomeConfig = BiomeConfig()

    override fun update(client: MinecraftClient): ModuleSnapshot {
        val snap = BiomeSnapshot()
        val p = client.player
        snap.name = client.world.getBiome(p.getBlockPos()).getName()
        return snap
    }

    override fun render(context: DrawContext, snapshot: ModuleSnapshot, scale: Float) {
        if (!config.show) return
        val snap = snapshot as BiomeSnapshot
        val client = MinecraftClient.getInstance()
        val w = client.getWindow().getFramebufferWidth()
        val x = when (config.position) {
            Corner.TOP_LEFT, Corner.BOTTOM_LEFT -> 2
            Corner.TOP_RIGHT, Corner.BOTTOM_RIGHT -> w - client.textRenderer.getWidth(snap.name) - 2
        }
        val y = when (config.position) {
            Corner.TOP_LEFT, Corner.TOP_RIGHT -> 2
            Corner.BOTTOM_LEFT, Corner.BOTTOM_RIGHT -> client.getWindow().getFramebufferHeight() - 10
        }
        context.drawText(client.textRenderer, snap.name, x, y, 0xFF_FFFFFF, false)
    }
}
