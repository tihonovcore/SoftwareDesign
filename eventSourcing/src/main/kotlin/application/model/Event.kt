package application.model

import java.time.Instant

class Event private constructor(
    val type: EventType,
    val subscriptionId: Long,
    val timestamp: Instant = Instant.now()
) {
    constructor(type: CreateEvent) : this(type, newId) {
        newId++
    }

    constructor(type: UpdateEvent, sid: Long) : this(type as EventType, sid)

    override fun toString(): String {
        return "$subscriptionId \t $type \t $timestamp"
    }

    companion object {
        private var newId: Long = 0
    }
}
