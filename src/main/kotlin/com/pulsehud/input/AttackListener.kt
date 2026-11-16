package com.pulsehud.input

import net.minecraft.entity.Entity

class AttackListener(private val registry: com.pulsehud.modules.ModuleRegistry) {

    var onPlayerHit: ((Entity) -> Unit)? = null

    init {
        net.fabricmc.fabric.api.client.event.v1.ClientEntityEvents.ATTACK_ENTITY.register { world, player, entity, hand ->
            if (entity is net.minecraft.entity.player.PlayerEntity) {
                onPlayerHit?.invoke(entity)
            }
        }
    }

    fun tick(client: net.minecraft.client.MinecraftClient) {
    }
}
