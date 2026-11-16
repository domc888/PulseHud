package com.pulsehud

import com.pulsehud.util.CpsWindow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class CpsWindowTest : StringSpec({
    "counts presses within window" {
        val w = CpsWindow(20)
        w.tick(0, true); w.tick(1, true); w.tick(2, false)
        w.count() shouldBe 2
    }
    "resets after window" {
        val w = CpsWindow(20)
        w.tick(0, true)
        w.tick(20, true)
        w.count() shouldBe 1
    }
    "no presses is zero" {
        CpsWindow(20).run { tick(0, false); count() shouldBe 0 }
    }
})
