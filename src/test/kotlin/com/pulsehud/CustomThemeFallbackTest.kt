package com.pulsehud

import com.pulsehud.theme.Preset
import com.pulsehud.theme.Theme
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class CustomThemeFallbackTest : StringSpec({
    "editing custom switches preset to Custom" {
        val theme = Theme(Preset.HONEYCOMB_AMBER, Preset.HONEYCOMB_AMBER.tokens)
        val next = theme.withCustomToken("accent", 0xFF_0000FF)
        next.preset shouldBe Preset.CUSTOM
        next.custom.accent shouldBe 0xFF_0000FF
    }
    "unknown token leaves custom unchanged (fallback)" {
        val theme = Theme(Preset.HONEYCOMB_AMBER, Preset.HONEYCOMB_AMBER.tokens)
        val next = theme.withCustomToken("bogus", 0xFF_123456)
        next.custom shouldBe theme.custom
        next.preset shouldBe Preset.CUSTOM
    }
    "each preset token falls back to its own default when custom omitted" {
        Preset.VALUES.forEach { p ->
            p.tokens.accent.shr(24) and 0xFF shouldBe 255
        }
    }
})
