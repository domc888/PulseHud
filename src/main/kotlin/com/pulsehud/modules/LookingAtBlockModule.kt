package com.pulsehud.modules

import com.pulsehud.modules.ModuleCategory.MISC
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

data class LookingAtBlockConfig : ModuleConfig() {
    var maxDistance: Int = 16
    var showToolHint: Boolean = true
}

data class LookingAtBlockSnapshot : ModuleSnapshot() {
    var name: String = ""
    var toolHint: String = ""
    var active: Boolean = false
}

class LookingAtBlockModule : Module<LookingAtBlockConfig>() {
    override val id = "21"
    override val category = MISC
    override val defaultEnabled = false
    override val config: LookingAtBlockConfig = LookingAtBlockConfig()

    override fun update(client: MinecraftClient): ModuleSnapshot {
        val snap = LookingAtBlockSnapshot()
        val target = client.crosshairTarget
        if (target is net.minecraft.util.hit.BlockHitResult) {
            val pos = target.getBlockPos()
            val dist = client.player.distanceTo(pos.getX(), pos.getY(), pos.getZ())
            if (dist <= config.maxDistance.toDouble()) {
                snap.active = true
                snap.name = client.world.getBlockState(pos).getBlock().getName()
                if (config.showToolHint) snap.toolHint = "tool: pickaxe"
            }
        }
        return snap
    }

    override fun render(context: DrawContext, snapshot: ModuleSnapshot, scale: Float) {
        val snap = snapshot as LookingAtBlockSnapshot
        if (!snap.active) return
        val client = MinecraftClient.getInstance()
        val text = if (snap.toolHint.isNotEmpty()) "${snap.name} [${snap.toolHint}]" else snap.name
        context.drawText(client.textRenderer, text, 2, 148, 0xFF_FFFFFF, false)
    }
}
