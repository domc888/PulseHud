package com.pulsehud

import com.pulsehud.config.Config
import com.pulsehud.modules.CrosshairModule
import com.pulsehud.modules.FpsModule
import com.pulsehud.modules.ModuleRegistry
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ModuleSearchTest : StringSpec({
    "empty query shows all" {
        val r = ModuleRegistry(Config())
        r.register(CrosshairModule())
        r.register(FpsModule())
        r.modulesMatching("").size shouldBe 2
    }
    "case insensitive id match" {
        val r = ModuleRegistry(Config())
        r.register(CrosshairModule())
        r.modulesMatching("1").size shouldBe 1
    }
    "alias match" {
        val r = ModuleRegistry(Config())
        r.register(FpsModule())
        r.registerAlias("frames", "2")
        r.modulesMatching("frames").single().id shouldBe "2"
    }
})
