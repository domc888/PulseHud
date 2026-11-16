package com.pulsehud.ui

class ToastQueue(private val capacity: Int = 3) {
    data class Toast(val text: String, val expireTick: Long)
    private val toasts = mutableListOf<Toast>()

    fun add(text: String, currentTick: Long, ttlTicks: Long) {
        if (toasts.size >= capacity) toasts.removeAt(0)
        toasts.add(Toast(text, currentTick + ttlTicks))
    }

    fun tick(currentTick: Long) {
        toasts.removeAll { it.expireTick <= currentTick }
    }

    fun current(): List<Toast> = toasts.toList()
    fun size(): Int = toasts.size
}
