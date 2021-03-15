package application.dao

import application.model.Enter
import application.model.Event
import application.model.Exit

class InMemoryReportDao : ReportDao {
    private val events = mutableSetOf<Event>()

    override fun updateVisits(dao: EventDao) {
        events += dao.getEvents().filter { it.type is Enter || it.type is Exit }
    }

    override fun visitsGroupByUser(): List<List<Pair<Event, Event>>> {
        return events
            .sortedBy { it.timestamp }
            .groupBy { it.subscriptionId }
            .map {
                val enters = it.value.filter { e -> e.type is Enter }
                val exits = it.value.filter { e -> e.type is Exit }

                enters.zip(exits)
            }
    }
}
