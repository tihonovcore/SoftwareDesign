package application.controller

import application.annotation.Query
import application.dao.EventDao
import application.dao.ReportDao
import application.model.Enter
import application.model.Exit
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration

@RestController
class ReportController(val dao: EventDao, val reportDao: ReportDao) {
    @Query
    @PostMapping("/report/per_day")
    fun statisticsPerDay(): String {
        reportDao.updateVisits(dao)

        val dayToVisits = reportDao.visitsGroupByUser()
            .flatten()
            .groupBy {
                val dayInSeconds = 24 * 60 * 60
                it.first.timestamp.epochSecond % dayInSeconds
            }
            .mapValues { it.value.size }

        return dayToVisits.toString()
    }

    @Query
    @PostMapping("/report/avg_frequency")
    fun avgFrequency(): String {
        reportDao.updateVisits(dao)

        val frequency = reportDao.visitsGroupByUser().map { it.size }
        val visitsCount = frequency.sum()
        val clientsCount = frequency.size

        return "$visitsCount visits, $clientsCount clients, avg: ${visitsCount / clientsCount} visits per client"
    }

    @Query
    @PostMapping("/report/avg_duration")
    fun avgDuration(): String {
        reportDao.updateVisits(dao)

        val visits = reportDao.visitsGroupByUser().flatten()

        val spentTime = visits.fold(Duration.ZERO) { accum, (enter, exit) ->
            val diff = exit.timestamp.epochSecond - enter.timestamp.epochSecond
            accum + Duration.ofSeconds(diff)
        }

        return "${visits.size} visits, $spentTime spent, avg: ${spentTime.seconds / visits.size} seconds per visit"
    }
}
