package com.pulsehud.modules

import com.pulsehud.modules.ModuleCategory.COMBAT
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

data class TargetInfoConfig : ModuleConfig() {
    var maxDistance: Int = 64
    var showDistance: Boolean = true
    var fadeOut: Int = 1000
}

data class TargetInfoSnapshot : ModuleSnapshot() {
    var name: String = ""
    var distance: Double = 0
    var heldItem: String = ""
    var alpha: Int = 255
    var active: Boolean = false
}

class TargetInfoModule : Module<TargetInfoConfig>() {
    override val id = "13"
    override val category = COMBAT
    override val defaultEnabled = false
    override val config: TargetInfoConfig = TargetInfoConfig()

    private var lastSeen = 0L
    private var lastName = ""
    private var lastDist = 0.0
    private var lastItem = ""

    override fun update(client: MinecraftClient): ModuleSnapshot {
        val snap = TargetInfoSnapshot()
        val target = client.targetedEntity
        val now = client.player?.tickCount ?: 0L
        if (target != null) {
            val dist = client.player.distanceTo(target)
            if (dist <= config.maxDistance.toDouble()) {
                lastSeen = now
                lastName = target.getName()
                lastDist = dist
                lastItem = target.getHeldItem().getName()
                snap.active = true
                snap.alpha = 255
            } else {
                snap.active = false
            }
        } else {
            snap.active = false
        }
        val age = (now - lastSeen) * 50
        snap.alpha = (255 - (age / config.fadeOut.coerceAtLeast(1) * 255).toInt()).coerceIn(0, 255)
        snap.name = lastName
        snap.distance = lastDist
        snap.heldItem = lastItem
        return snap
    }

    override fun render(context: DrawContext, snapshot: ModuleSnapshot, scale: Float) {
        val snap = snapshot as TargetInfoSnapshot
        if (!snap.active && snap.alpha <= 0) return
        val client = MinecraftClient.getInstance()
        val text = buildString {
            append(snap.name)
            if (config.showDistance) append(" ${snap.distance.toInt()}m")
            if (snap.heldItem.isNotEmpty()) append(" [$snap.heldItem]")
        }
        val color = (snap.alpha shl 24) or 0xFFFFFFFF
        context.drawText(client.textRenderer, text, 2, 114, color, false)
    }
}
