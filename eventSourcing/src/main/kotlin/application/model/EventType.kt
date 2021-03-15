package application.model

import java.time.Duration

sealed class EventType

abstract class CreateEvent : EventType()

abstract class UpdateEvent : EventType()

data class NewSubscription(val duration: Duration) : CreateEvent()

data class ProlongSubscription(val duration: Duration) : UpdateEvent()

object Enter : UpdateEvent() {
    override fun toString() = "Enter"
}

object Exit : UpdateEvent() {
    override fun toString() = "Exit"
}
