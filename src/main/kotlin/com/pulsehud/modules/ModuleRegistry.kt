package com.pulsehud.modules

import com.pulsehud.config.Config
import com.pulsehud.config.ModuleState
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import java.util.concurrent.ConcurrentHashMap

class ModuleRegistry(private val config: Config) {

    private val modules = mutableListOf<Module<*>>()
    private val byId = ConcurrentHashMap<String, Module<*>>()
    private val snapshots = ConcurrentHashMap<String, ModuleSnapshot>()
    private val aliases = ConcurrentHashMap<String, String>()

    fun register(module: Module<*>) {
        modules.add(module)
        byId[module.id] = module
        val state = config.moduleStates[module.id] ?: ModuleState(defaultEnabled = module.defaultEnabled)
        module.config.enabled = state.enabled
    }

    fun registerAlias(alias: String, moduleId: String) {
        aliases[alias] = moduleId
    }

    fun modulesMatching(query: String): List<Module<*>> {
        val q = query.lowercase()
        return modules.filter { m ->
            if (q.isEmpty()) true
            else m.id.lowercase().contains(q) || aliases.entries.any { (a, id) -> a.lowercase().contains(q) && id == m.id } || m.category.name.lowercase().contains(q)
        }
    }

    fun <C : ModuleConfig> getModule(id: String): Module<C>? = byId[id] as? Module<C>

    fun update(client: MinecraftClient) {
        for (module in modules) {
            if (!module.config.enabled) continue
            snapshots[module.id] = module.update(client)
        }
    }

    fun render(context: DrawContext, scale: Float) {
        for (module in modules) {
            if (!module.config.enabled) continue
            val snapshot = snapshots[module.id] ?: ModuleSnapshot.Empty
            module.render(context, snapshot, scale)
        }
    }

    fun snapshot(id: String): ModuleSnapshot? = snapshots[id]

    fun allModules(): List<Module<*>> = modules.toList()
    fun enabledModules(): List<Module<*>> = modules.filter { it.config.enabled }

    fun setEnabled(id: String, enabled: Boolean) {
        config.moduleStates[id]?.let { config.moduleStates[id] = it.copy(enabled = enabled) }
        byId[id]?.config?.enabled = enabled
    }

    fun setPosition(id: String, anchor: String, offsetX: Int, offsetY: Int) {
        config.moduleStates[id]?.let { config.moduleStates[id] = it.copy(anchor = anchor, offsetX = offsetX, offsetY = offsetY) }
    }

    fun moduleState(id: String): ModuleState = config.moduleStates[id] ?: ModuleState(defaultEnabled = byId[id]?.defaultEnabled ?: false)
}
