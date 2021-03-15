package application.dao

import application.model.*
import java.time.Duration

class InMemoryEventDao : EventDao {
    private val events = mutableListOf<Event>()

    init {
        events += Event(NewSubscription(Duration.ofDays(3)))
        val first = events.last().subscriptionId

        events += Event(NewSubscription(Duration.ofDays(5)))
        val second = events.last().subscriptionId

        events += Event(ProlongSubscription(Duration.ofDays(3)), first)
        events += Event(Enter, second)
        events += Event(Exit, second)
    }

    override fun getEvents(): List<Event> = events

    override fun addEvent(event: Event) {
        events += event
    }
}
