package com.pulsehud

import com.pulsehud.util.HexGrid
import com.pulsehud.util.HexGrid.IntPair
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldContain
import io.kotest.matchers.lessThan
import kotlin.math.sqrt

class HexGridTest : StringSpec({
    "axial to pixel for origin" {
        val g = HexGrid()
        val p = g.axialToPixel(0, 0, 28f)
        p.first shouldBe 0
        p.second shouldBe 0
    }
    "axial to pixel known coordinate" {
        val g = HexGrid()
        val p = g.axialToPixel(1, 0, 28f)
        val expectedX = (28f * sqrt(3.0)).toInt()
        p.first shouldBe expectedX
        p.second shouldBe 0
    }
    "six neighbors of origin" {
        val g = HexGrid()
        val n = g.neighborsOfOrigin()
        n.size shouldBe 6
        n.shouldContain(IntPair(1, 0))
        n.shouldContain(IntPair(1, -1))
        n.shouldContain(IntPair(0, -1))
        n.shouldContain(IntPair(-1, 0))
        n.shouldContain(IntPair(-1, 1))
        n.shouldContain(IntPair(0, 1))
    }
    "no gaps or overlaps at scales 1-4" {
        val g = HexGrid()
        for (scale in 1..4) {
            val size = scale.toFloat() * 14f
            val c0 = g.axialToPixelF(0, 0, size)
            val c1 = g.axialToPixelF(1, 0, size)
            val dx = kotlin.math.abs(c1.first - c0.first)
            (dx - size * sqrt(3.0)).absoluteValue shouldBe lessThan(2f)
            val c2 = g.axialToPixelF(0, 1, size)
            val dy = kotlin.math.abs(c2.second - c0.second)
            (dy - size * 1.5f).absoluteValue shouldBe lessThan(2f)
        }
    }
})
