package com.pulsehud.modules

import com.pulsehud.modules.ModuleCategory.MISC
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

enum class MoveState { SPRINTING, SNEAKING, SWIMMING, FLYING, WALKING }

data class MovementStateConfig : ModuleConfig() {
    var showIcon: Boolean = true
}

data class MovementStateSnapshot : ModuleSnapshot() {
    var state: MoveState = MoveState.WALKING
}

class MovementStateModule : Module<MovementStateConfig>() {
    override val id = "24"
    override val category = MISC
    override val defaultEnabled = false
    override val config: MovementStateConfig = MovementStateConfig()

    override fun update(client: MinecraftClient): ModuleSnapshot {
        val snap = MovementStateSnapshot()
        val p = client.player
        snap.state = when {
            p.isSprinting -> MoveState.SPRINTING
            p.isSneaking -> MoveState.SNEAKING
            p.isSwimming -> MoveState.SWIMMING
            p.isFlying -> MoveState.FLYING
            else -> MoveState.WALKING
        }
        return snap
    }

    override fun render(context: DrawContext, snapshot: ModuleSnapshot, scale: Float) {
        val snap = snapshot as MovementStateSnapshot
        val client = MinecraftClient.getInstance()
        val label = snap.state.name
        context.drawText(client.textRenderer, label, 2, 182, 0xFF_FFFFFF, false)
    }
}
