package com.pulsehud.input

import net.minecraft.client.option.KeyBinding

class Keybinds {
    val pulseHud = KeyBinding("key.pulsehud", 340, "category.pulsehud")
    private var previous = false

    fun tick() {
        previous = pulseHud.isPressed
    }

    fun wasPressed(): Boolean = pulseHud.isPressed && !previous
}
