package com.pulsehud

import com.pulsehud.ui.ToastQueue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ToastQueueTest : StringSpec({
    "capacity 3 drops oldest on fourth" {
        val q = ToastQueue(3)
        q.add("a", 0, 1000)
        q.add("b", 0, 1000)
        q.add("c", 0, 1000)
        q.add("d", 0, 1000)
        q.current().map { it.text } shouldBe listOf("b", "c", "d")
    }
    "auto dismiss after ttl" {
        val q = ToastQueue(3)
        q.add("a", 0, 100)
        q.tick(50)
        q.current().size shouldBe 1
        q.tick(100)
        q.current().size shouldBe 0
    }
    "empty queue" {
        ToastQueue().current() shouldBe emptyList()
    }
})
