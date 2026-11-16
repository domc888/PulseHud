package com.pulsehud.modules

import com.pulsehud.modules.ModuleCategory.COMBAT
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

enum class PotionFilter { BENEFICIAL, HARMFUL, BOTH }

data class PotionConfig : ModuleConfig() {
    var showIcon: Boolean = true
    var filter: PotionFilter = PotionFilter.BOTH
}

data class PotionEntry(val name: String, val duration: Int, val beneficial: Boolean)

data class PotionSnapshot : ModuleSnapshot() {
    var effects: List<PotionEntry> = emptyList()
}

class PotionModule : Module<PotionConfig>() {
    override val id = "9"
    override val category = COMBAT
    override val defaultEnabled = false
    override val config: PotionConfig = PotionConfig()

    override fun update(client: MinecraftClient): ModuleSnapshot {
        val snap = PotionSnapshot()
        val p = client.player
        val effects = p.statusEffects.map { effect ->
            PotionEntry(effect.getName(), effect.duration, effect.isBeneficial)
        }.filter { e ->
            when (config.filter) {
                PotionFilter.BENEFICIAL -> e.beneficial
                PotionFilter.HARMFUL -> !e.beneficial
                PotionFilter.BOTH -> true
            }
        }.sortedByDescending { it.duration }
        snap.effects = effects
        return snap
    }

    override fun render(context: DrawContext, snapshot: ModuleSnapshot, scale: Float) {
        val snap = snapshot as PotionSnapshot
        val client = MinecraftClient.getInstance()
        var y = 82
        for (e in snap.effects) {
            val text = "${e.name} ${e.duration / 20}s"
            context.drawText(client.textRenderer, text, 2, y, 0xFF_FFFFFF, false)
            y += 10
        }
    }
}
