package com.pulsehud

import com.pulsehud.config.Config
import com.pulsehud.config.ConfigManager
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ConfigMigrationTest : StringSpec({
    "old schema migrates to version 1" {
        val m = ConfigManager()
        val c = m.fromJson("{\"schemaVersion\":0}")
        m.migrate(c).schemaVersion shouldBe 1
    }
    "current schema stays" {
        val m = ConfigManager()
        val c = m.fromJson("{\"schemaVersion\":1,\"theme\":{\"preset\":\"MIDNIGHT_BLUE\"}}")
        m.migrate(c).schemaVersion shouldBe 1
    }
    "malformed json falls back to defaults" {
        val m = ConfigManager()
        m.fromJson("{not valid json").schemaVersion shouldBe Config().schemaVersion
    }
    "missing file returns defaults" {
        ConfigManager().load().schemaVersion shouldBe 1
    }
})
