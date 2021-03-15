package application.dao

import application.model.Event

interface ReportDao {
    fun updateVisits(dao: EventDao)

    fun visitsGroupByUser(): List<List<Pair<Event, Event>>>
}
