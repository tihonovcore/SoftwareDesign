package statistics

import clock.Clock
import java.time.Duration
import java.time.Instant

 class LastHourStatistic(
    private val clock: Clock
) : EventsStatistic {
    private val initialTime = clock.now()
    private val statistic = mutableMapOf<EventName, MutableList<Instant>>()

    override fun incEvent(eventName: EventName) {
        statistic.putIfAbsent(eventName, mutableListOf())
        statistic[eventName]!! += clock.now()
    }

    override fun getEventStatisticByName(eventName: EventName): Double {
        val now = clock.now()
        val hour = Duration.ofHours(1)

        val events = statistic[eventName] ?: emptyList()
        return 1.0 * events.filter { now - hour < it && it <= now }.size / 60
    }

    override fun getAllEventStatistic(): Map<EventName, Double> {
        val now = clock.now()
        val hour = Duration.ofHours(1)

        val result = mutableMapOf<EventName, Double>()
        for (eventName in statistic.keys) {
            val events = statistic[eventName] ?: emptyList()
            result[eventName] = 1.0 * events.filter { now - hour < it && it <= now }.size / 60
        }

        return result
    }

    override fun printStatistic() {
        val totalHours = (clock.now().epochSecond - initialTime.epochSecond) / 60.0 / 60.0

        for (eventName in statistic.keys) {
            val events = statistic[eventName] ?: emptyList()
            val rpm = 1.0 * events.size / totalHours
            println("Event: $eventName  ### RPM: $rpm")
        }
    }
}
