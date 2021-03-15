package application.dao

import application.model.Event

interface EventDao {
    fun getEvents(): List<Event>
    fun addEvent(event: Event)
}
