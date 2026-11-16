package com.pulsehud.util

class MovingAverage(private val size: Int) {
    private val buffer = DoubleArray(size)
    private var idx = 0
    private var count = 0

    fun add(value: Double) {
        buffer[idx] = value
        idx = (idx + 1) % size
        count = (count + 1).coerceAtMost(size)
    }

    fun average(): Double {
        var sum = 0.0
        for (i in 0 until count) sum += buffer[i]
        return sum / count.max(1)
    }

    fun clear() {
        idx = 0
        count = 0
    }
}

class CpsWindow(private val windowTicks: Int) {
    private var count = 0
    private var lastReset = 0L

    fun tick(now: Long, pressed: Boolean) {
        if (now - lastReset >= windowTicks) {
            count = 0
            lastReset = now
        }
        if (pressed) count++
    }

    fun count(): Int = count
}

class ComboTracker(var resetTimeoutTicks: Long) {
    private var combo = 0
    private var lastHit = 0L

    fun hit(now: Long) {
        combo++
        lastHit = now
    }

    fun tick(now: Long): Int {
        if (now - lastHit > resetTimeoutTicks) combo = 0
        return combo
    }

    fun combo(): Int = combo
}
