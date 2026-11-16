package com.pulsehud.modules

import com.pulsehud.modules.ModuleCategory.MISC
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

enum class KeyLayout { WASD_MOUSE, WASD_ONLY, ARROWS }

data class KeystrokesConfig : ModuleConfig() {
    var layout: KeyLayout = KeyLayout.WASD_MOUSE
    var keyColor: Int = 0xFF_333333
    var pressedColor: Int = 0xFF_00FF00
    var background: Boolean = true
}

data class KeystrokesSnapshot : ModuleSnapshot() {
    var w = false
    var a = false
    var s = false
    var d = false
    var space = false
    var lmb = false
    var rmb = false
}

class KeystrokesModule : Module<KeystrokesConfig>() {
    override val id = "6"
    override val category = MISC
    override val defaultEnabled = false
    override val config: KeystrokesConfig = KeystrokesConfig()

    override fun update(client: MinecraftClient): ModuleSnapshot {
        val snap = KeystrokesSnapshot()
        val opts = client.options
        snap.w = opts.keyForward.isPressed
        snap.a = opts.keyLeft.isPressed
        snap.s = opts.keyBack.isPressed
        snap.d = opts.keyRight.isPressed
        snap.space = opts.keyJump.isPressed
        snap.lmb = opts.attackKey.isPressed
        snap.rmb = opts.useItem.isPressed
        return snap
    }

    override fun render(context: DrawContext, snapshot: ModuleSnapshot, scale: Float) {
        val snap = snapshot as KeystrokesSnapshot
        val client = MinecraftClient.getInstance()
        val size = (20f * scale).toInt()
        val gap = (4f * scale).toInt()
        val cx = (client.getWindow().getFramebufferWidth() / 2.0f).toInt()
        val cy = (client.getWindow().getFramebufferHeight() / 2.0f).toInt()
        fun key(x: Int, y: Int, pressed: Boolean, label: String) {
            if (config.background) context.fill(x, y, x + size, y + size, if (pressed) config.pressedColor else config.keyColor)
            val w = client.textRenderer.getWidth(label)
            context.drawText(client.textRenderer, label, x + (size - w) / 2, y + (size - 8) / 2, 0xFF_FFFFFF, false)
        }
        key(cx, cy - size - gap, snap.w, "W")
        key(cx - size - gap, cy, snap.a, "A")
        key(cx, cy, snap.s, "S")
        key(cx + size + gap, cy, snap.d, "D")
        key(cx, cy + size + gap, snap.space, "SPC")
        if (config.layout == KeyLayout.WASD_MOUSE) {
            key(cx - size * 2 - gap * 2, cy + size + gap, snap.lmb, "LMB")
            key(cx + size * 2 + gap * 2, cy + size + gap, snap.rmb, "RMB")
        }
    }
}
