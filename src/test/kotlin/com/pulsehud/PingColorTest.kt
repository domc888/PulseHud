package com.pulsehud

import com.pulsehud.modules.PingModule
import com.pulsehud.modules.PingColor
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldBeInstanceOf

class PingColorTest : StringSpec({
    "under warn is green" {
        PingModule.bucket(50, 80, 150) shouldBeInstanceOf PingColor.Green::class.java
    }
    "between warn and crit is yellow" {
        PingModule.bucket(100, 80, 150) shouldBeInstanceOf PingColor.Yellow::class.java
    }
    "above crit is red" {
        PingModule.bucket(200, 80, 150) shouldBeInstanceOf PingColor.Red::class.java
    }
    "not connected is green" {
        PingModule.bucket(-1, 80, 150) shouldBeInstanceOf PingColor.Green::class.java
    }
    "boundary warn is green" {
        PingModule.bucket(80, 80, 150) shouldBeInstanceOf PingColor.Yellow::class.java
    }
    "boundary crit is red" {
        PingModule.bucket(150, 80, 150) shouldBeInstanceOf PingColor.Red::class.java
    }
})
