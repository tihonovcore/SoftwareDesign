package statistics

import clock.SettableClock
import org.junit.Assert.*
import org.junit.Test
import java.time.Duration
import java.time.Instant

internal class LastHourStatisticTest {
    @Test
    fun `test events within one hour`() {
        val oneHourAgo = Instant.now() - Duration.ofHours(1)
        val statistic = LastHourStatistic(SettableClock(oneHourAgo))

        val first = mapOf("me" to 1, "li" to 1)
        statistic.action(first)
        statistic.check(first)

        val second = mapOf("s" to 2, "a" to 1)
        statistic.action(second)
        statistic.check(first + second)
    }

    @Test
    fun `test escaping old event`() {
        val oneHourAgo = Instant.now() - Duration.ofMinutes(61)
        val clock = SettableClock(oneHourAgo)
        val statistic = LastHourStatistic(clock)

        val first = mapOf("ember" to 1)
        statistic.action(first)
        statistic.check(first)

        clock.now = Instant.now()

        val second = mapOf("spirit" to 1)
        statistic.action(second)
        statistic.check(second)
    }

    @Test
    fun `test long history`() {
        val twoHourAgo = Instant.now() - Duration.ofMinutes(121)
        val clock = SettableClock(twoHourAgo)
        val statistic = LastHourStatistic(clock)

        val first = mapOf("quas" to 102, "q" to 2)
        statistic.action(first)
        statistic.check(first)

        clock.now += Duration.ofMinutes(61)

        val second = mapOf("wex" to 203, "w" to 3)
        statistic.action(second)
        statistic.check(second)

        clock.now += Duration.ofMinutes(61)

        val third = mapOf("exort" to 304, "e" to 4)
        statistic.action(third)
        statistic.check(third)
    }

    private fun EventsStatistic.action(events: Map<EventName, Int>) {
        events.forEach { (event, times) ->
            repeat(times) { incEvent(event) }
        }
    }

    private fun EventsStatistic.check(events: Map<EventName, Int>) {
        val all = getAllEventStatistic()
        for ((event, times) in events.entries) {
            assertEquals(times / 60.0, all[event])
        }
    }
}
