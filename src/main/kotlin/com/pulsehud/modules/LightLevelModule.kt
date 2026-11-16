package com.pulsehud.modules

import com.pulsehud.modules.ModuleCategory.MISC
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

data class LightLevelConfig : ModuleConfig() {
    var showSkyLight: Boolean = true
}

data class LightLevelSnapshot : ModuleSnapshot() {
    var blockLight: Int = 0
    var skyLight: Int = 0
}

class LightLevelModule : Module<LightLevelConfig>() {
    override val id = "22"
    override val category = MISC
    override val defaultEnabled = false
    override val config: LightLevelConfig = LightLevelConfig()

    override fun update(client: MinecraftClient): ModuleSnapshot {
        val snap = LightLevelSnapshot()
        val p = client.player
        val pos = p.getBlockPos()
        snap.blockLight = client.world.getLightLevel(pos)
        snap.skyLight = client.world.getSkyLight(pos)
        return snap
    }

    override fun render(context: DrawContext, snapshot: ModuleSnapshot, scale: Float) {
        val snap = snapshot as LightLevelSnapshot
        val client = MinecraftClient.getInstance()
        val text = if (config.showSkyLight) "Light: ${snap.blockLight}/${snap.skyLight}" else "Light: ${snap.blockLight}"
        context.drawText(client.textRenderer, text, 2, 158, 0xFF_FFFF00, false)
    }
}
