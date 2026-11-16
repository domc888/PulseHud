package com.pulsehud

import com.pulsehud.util.HexGrid
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class LayoutAnchorTest : StringSpec({
    "top-left anchor" {
        val g = HexGrid()
        val p = g.anchorPosition("top-left", 800, 600, 100, 50, 10, 20)
        p.first shouldBe 10
        p.second shouldBe 20
    }
    "top-right anchor" {
        val g = HexGrid()
        val p = g.anchorPosition("top-right", 800, 600, 100, 50, 0, 0)
        p.first shouldBe 700
        p.second shouldBe 0
    }
    "bottom-left anchor" {
        val g = HexGrid()
        val p = g.anchorPosition("bottom-left", 800, 600, 100, 50, 0, 0)
        p.first shouldBe 0
        p.second shouldBe 550
    }
    "bottom-right anchor" {
        val g = HexGrid()
        val p = g.anchorPosition("bottom-right", 800, 600, 100, 50, 0, 0)
        p.first shouldBe 700
        p.second shouldBe 550
    }
    "center anchor" {
        val g = HexGrid()
        val p = g.anchorPosition("center", 800, 600, 100, 50, 0, 0)
        p.first shouldBe 350
        p.second shouldBe 275
    }
})
