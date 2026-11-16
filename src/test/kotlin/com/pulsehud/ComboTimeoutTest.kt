package com.pulsehud

import com.pulsehud.util.ComboTracker
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ComboTimeoutTest : StringSpec({
    "combo increments on hit" {
        val t = ComboTracker(100)
        t.hit(10); t.hit(20)
        t.tick(20) shouldBe 2
    }
    "resets after timeout" {
        val t = ComboTracker(20)
        t.hit(0)
        t.tick(0) shouldBe 1
        t.tick(21) shouldBe 0
    }
    "no reset within timeout" {
        val t = ComboTracker(100)
        t.hit(0)
        t.tick(50) shouldBe 1
    }
})
