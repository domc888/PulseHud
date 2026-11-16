package com.pulsehud.util

import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI
import kotlin.math.roundToInt

class HexGrid {

    fun axialToPixel(q: Int, r: Int, size: Float): IntPair {
        val x = size * kotlin.math.sqrt(3.0).toFloat() * (q + r / 2.0f)
        val y = size * 1.5f * r
        return IntPair(x.roundToInt(), y.roundToInt())
    }

    fun axialToPixelF(q: Int, r: Int, size: Float): FloatPair {
        val x = size * kotlin.math.sqrt(3.0).toFloat() * (q + r / 2.0f)
        val y = size * 1.5f * r
        return FloatPair(x, y)
    }

    fun neighborsOfOrigin(): List<IntPair> = listOf(
        IntPair(1, 0), IntPair(1, -1), IntPair(0, -1),
        IntPair(-1, 0), IntPair(-1, 1), IntPair(0, 1)
    )

    fun anchorPosition(anchor: String, sx: Int, sy: Int, w: Int, h: Int, offsetX: Int, offsetY: Int): IntPair {
        val x = when (anchor) {
            "top-left", "bottom-left" -> 0
            "top-right", "bottom-right" -> sx - w
            "center" -> (sx - w) / 2
            else -> 0
        } + offsetX
        val y = when (anchor) {
            "top-left", "top-right" -> 0
            "bottom-left", "bottom-right" -> sy - h
            "center" -> (sy - h) / 2
            else -> 0
        } + offsetY
        return IntPair(x, y)
    }

    fun pointyTopVertices(centerX: Float, centerY: Float, size: Float): FloatArray {
        val verts = FloatArray(12)
        for (i in 0 until 6) {
            val angle = PI / 2 + i * PI / 3
            verts[i * 2] = centerX + size * cos(angle).toFloat()
            verts[i * 2 + 1] = centerY + size * sin(angle).toFloat()
        }
        return verts
    }

    data class IntPair(val first: Int, val second: Int)
    data class FloatPair(val first: Float, val second: Float)
}
