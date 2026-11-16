package com.pulsehud.modules

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

abstract class ModuleConfig {
    open var enabled: Boolean = false
}

sealed class ModuleSnapshot {
    object Empty : ModuleSnapshot()
}

abstract class Module<C : ModuleConfig> {
    abstract val id: String
    abstract val category: ModuleCategory
    abstract val defaultEnabled: Boolean
    abstract val config: C

    abstract fun update(client: MinecraftClient): ModuleSnapshot
    abstract fun render(context: DrawContext, snapshot: ModuleSnapshot, scale: Float)
}
