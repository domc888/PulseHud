package com.pulsehud

import com.pulsehud.util.ComboTracker
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class HitComboTest : StringSpec({
    "one attack source increments both combo and hit indicator" {
        val combo = ComboTracker(100)
        var indicatorFlashes = 0
        fun onAttack(tick: Long) {
            combo.hit(tick)
            indicatorFlashes++
        }
        onAttack(10)
        onAttack(20)
        combo.tick(20) shouldBe 2
        indicatorFlashes shouldBe 2
    }
    "both reset together after timeout" {
        val combo = ComboTracker(20)
        var indicatorFlashes = 0
        fun onAttack(tick: Long) { combo.hit(tick); indicatorFlashes++ }
        onAttack(0)
        combo.tick(0) shouldBe 1
        combo.tick(21) shouldBe 0
    }
})
