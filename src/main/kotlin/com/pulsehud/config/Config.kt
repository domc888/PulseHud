package com.pulsehud.config

import com.pulsehud.theme.Theme
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.FileSystems

data class ModuleState(
    val enabled: Boolean = false,
    val anchor: String = "top-left",
    val offsetX: Int = 0,
    val offsetY: Int = 0
)

data class Config(
    val schemaVersion: Int = 1,
    val theme: Theme = Theme.defaultTheme(),
    val moduleStates: Map<String, ModuleState> = emptyMap()
)

class ConfigManager {

    private val configFile = FileSystems.getDefault().getPath("config", "pulsehud.json").toFile()
    private val gson = GsonBuilder().setPrettyPrinting().create()
    private val logger = org.slf4j.LoggerFactory.getLogger("pulsehud")

    fun load(): Config {
        if (!configFile.exists()) return Config()
        return try {
            val json = Files.readAllBytes(configFile.toPath()).decodeToString()
            val parsed = gson.fromJson(json, Config::class.java)
            if (parsed == null) Config() else migrate(parsed)
        } catch (e: Exception) {
            logger.warn("failed to read config, using defaults", e)
            Config()
        }
    }

    fun save(config: Config) {
        configFile.parentFile.mkdirs()
        val json = gson.toJson(config)
        Files.write(configFile.toPath(), json.toByteArray(StandardCharsets.UTF_8))
    }

    fun reload(): Config {
        val c = load()
        save(c)
        return c
    }

    fun migrate(config: Config): Config {
        var result = config
        var changed = false
        if (result.schemaVersion < 1) {
            result = result.copy(schemaVersion = 1)
            changed = true
        }
        if (changed) save(result)
        return result
    }

    fun fromJson(json: String): Config {
        return try {
            val parsed = gson.fromJson(json, Config::class.java)
            if (parsed == null) Config() else migrate(parsed)
        } catch (e: Exception) {
            logger.warn("failed to parse config json, using defaults", e)
            Config()
        }
    }
}
