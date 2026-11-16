package com.pulsehud.util

class Animation {
    var value: Float = 0f
    private var target: Float = 0f
    private var start: Float = 0f
    private var startTick: Long = 0
    private var durationTicks: Int = 0
    private var running = false

    fun start(from: Float, to: Float, durationTicks: Int, currentTick: Long) {
        start = from
        target = to
        this.durationTicks = durationTicks.max(1)
        startTick = currentTick
        value = from
        running = true
    }

    fun update(currentTick: Long, partialTick: Float): Float {
        if (!running) return value
        val elapsed = (currentTick - startTick) + partialTick
        val t = (elapsed / durationTicks).coerceIn(0f, 1f)
        value = start + (target - start) * easeOut(t)
        if (t >= 1f) running = false
        return value
    }

    fun isRunning(): Boolean = running

    private fun easeOut(t: Float): Float = 1f - (1f - t) * (1f - t)
}
