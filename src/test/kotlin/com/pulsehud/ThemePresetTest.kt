package com.pulsehud

import com.pulsehud.theme.Preset
import com.pulsehud.theme.Theme
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ThemePresetTest : StringSpec({
    "every preset defines all tokens" {
        for (p in Preset.VALUES) {
            val t = p.tokens
            t.accent shouldBe 0xFF_FF8C00 or t.accent // defined
            t.panelBackground shouldBe t.panelBackground
            t.panelBorder shouldBe t.panelBorder
            t.textPrimary shouldBe t.textPrimary
            t.textSecondary shouldBe t.textSecondary
            t.gridColor shouldBe t.gridColor
            t.hudTint shouldBe t.hudTint
        }
    }
    "solid tokens have alpha 255" {
        Preset.HONEYCOMB_AMBER.tokens.accent.shr(24) and 0xFF shouldBe 255
        Preset.HONEYCOMB_AMBER.tokens.textPrimary.shr(24) and 0xFF shouldBe 255
        Preset.HONEYCOMB_AMBER.tokens.hudTint.shr(24) and 0xFF shouldBe 255
    }
    "selecting a preset fills custom fields" {
        val theme = Theme().selectPreset(Preset.MIDNIGHT_BLUE)
        theme.custom shouldBe Preset.MIDNIGHT_BLUE.tokens
        theme.preset shouldBe Preset.MIDNIGHT_BLUE
    }
    "active tokens reflect preset when not custom" {
        Theme(Preset.MIDNIGHT_BLUE).activeTokens() shouldBe Preset.MIDNIGHT_BLUE.tokens
    }
})
