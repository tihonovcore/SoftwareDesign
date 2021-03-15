package application.controller

import application.annotation.Query
import application.dao.EventDao
import application.model.Enter
import application.model.Exit
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration

@RestController
class ReportController(val dao: EventDao) {
    @Query
    @PostMapping("/report/per_day")
    fun statisticsPerDay(): String {
        val dayToVisits = visits().flatten().groupBy {
            val dayInSeconds = 24 * 60 * 60
            it.first.timestamp.epochSecond % dayInSeconds
        }.mapValues { it.value.size }

        return dayToVisits.toString()
    }

    @Query
    @PostMapping("/report/avg_frequency")
    fun avgFrequency(): String {
        val frequency = visits().map { it.size }
        val visitsCount = frequency.sum()
        val clientsCount = frequency.size

        return "$visitsCount visits, $clientsCount clinets, avg: ${visitsCount / clientsCount} visits per client"
    }

    @Query
    @PostMapping("/report/avg_duration")
    fun avgDuration(): String {
        val visits = visits().flatten()

        val spentTime = visits.fold(Duration.ZERO) { accum, (enter, exit) ->
            val diff = exit.timestamp.epochSecond - enter.timestamp.epochSecond
            accum + Duration.ofSeconds(diff)
        }

        return "${visits.size} visits, $spentTime spent, avg: ${spentTime.seconds / visits.size} seconds per visit"
    }

    private fun visits() = dao
        .getEvents()
        .asSequence()
        .filter { it.type is Enter || it.type is Exit }
        .sortedBy { it.timestamp }
        .groupBy { it.subscriptionId }
        .map {
            val enters = it.value.filter { e -> e.type is Enter }
            val exits = it.value.filter { e -> e.type is Exit }

            enters.zip(exits)
        }
}
