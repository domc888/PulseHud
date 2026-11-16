package com.pulsehud.theme

data class ThemeTokens(
    val accent: Int = 0xFF_FF8C00,
    val panelBackground: Int = 0xFF_1A1A1A,
    val panelBorder: Int = 0xFF_333333,
    val textPrimary: Int = 0xFF_FFFFFF,
    val textSecondary: Int = 0xFF_B0B0B0,
    val gridColor: Int = 0xFF_444444,
    val hudTint: Int = 0xFF_FF8C00
)

enum class Preset(val tokens: ThemeTokens) {
    HONEYCOMB_AMBER(ThemeTokens(
        accent = 0xFF_FF8C00,
        panelBackground = 0xFF_1A1A1A,
        panelBorder = 0xFF_4A3728,
        textPrimary = 0xFF_FFFFFF,
        textSecondary = 0xFF_D4A373,
        gridColor = 0xFF_8B5A2B,
        hudTint = 0xFF_FF8C00
    )),
    MIDNIGHT_BLUE(ThemeTokens(
        accent = 0xFF_2B4A7A,
        panelBackground = 0xFF_0F172A,
        panelBorder = 0xFF_1E3A8A,
        textPrimary = 0xFF_E2E8F0,
        textSecondary = 0xFF_94A3B8,
        gridColor = 0xFF_334155,
        hudTint = 0xFF_2B4A7A
    )),
    MONO_LIGHT(ThemeTokens(
        accent = 0xFF_444444,
        panelBackground = 0xFF_F5F5F5,
        panelBorder = 0xFF_999999,
        textPrimary = 0xFF_111111,
        textSecondary = 0xFF_555555,
        gridColor = 0xFF_CCCCCC,
        hudTint = 0xFF_444444
    )),
    CUSTOM(null);

    companion {
        val VALUES = values().filter { it != CUSTOM }
    }
}

data class Theme(
    val preset: Preset = Preset.HONEYCOMB_AMBER,
    val custom: ThemeTokens = ThemeTokens()
) {
    fun activeTokens(): ThemeTokens = if (preset == Preset.CUSTOM) custom else preset.tokens

    fun selectPreset(p: Preset): Theme = when {
        p == Preset.CUSTOM -> copy(preset = p)
        else -> copy(preset = p, custom = p.tokens)
    }

    fun withCustomToken(token: String, value: Int): Theme {
        val c = when (token) {
            "accent" -> custom.copy(accent = value)
            "panelBackground" -> custom.copy(panelBackground = value)
            "panelBorder" -> custom.copy(panelBorder = value)
            "textPrimary" -> custom.copy(textPrimary = value)
            "textSecondary" -> custom.copy(textSecondary = value)
            "gridColor" -> custom.copy(gridColor = value)
            "hudTint" -> custom.copy(hudTint = value)
            else -> custom
        }
        return copy(preset = Preset.CUSTOM, custom = c)
    }

    fun defaultTheme(): Theme = Theme(Preset.HONEYCOMB_AMBER, Preset.HONEYCOMB_AMBER.tokens)
}
