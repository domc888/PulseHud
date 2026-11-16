package com.pulsehud

import com.pulsehud.util.MovingAverage
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class SpeedMovingAverageTest : StringSpec({
    "moving average of 5 samples" {
        val ma = MovingAverage(5)
        ma.add(1.0)
        ma.add(2.0)
        ma.add(3.0)
        ma.add(4.0)
        ma.add(5.0)
        ma.average() shouldBe 3.0
    }
    "oldest sample drops out" {
        val ma = MovingAverage(3)
        ma.add(1.0); ma.add(2.0); ma.add(3.0)
        ma.add(4.0)
        ma.average() shouldBe 3.0
    }
    "empty average is zero" {
        MovingAverage(5).average() shouldBe 0.0
    }
})
