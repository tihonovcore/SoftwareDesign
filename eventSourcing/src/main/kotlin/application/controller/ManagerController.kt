package application.controller

import application.annotation.Command
import application.annotation.Query
import application.dao.EventDao
import application.model.Event
import application.model.NewSubscription
import application.model.ProlongSubscription
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Duration

@RestController
class ManagerController(val dao: EventDao) {
    @Query
    @PostMapping("/manager/subscription_info")
    fun subscriptionInfo(@RequestParam("sid") sid: Long): String {
        return dao.getEvents().filter { it.subscriptionId == sid }.joinToString(System.lineSeparator())
    }

    @Command
    @PostMapping("/manager/new_subscription")
    fun newSubscription(@RequestParam("days") days: Long) {
        dao.addEvent(Event(NewSubscription(Duration.ofDays(days))))
    }

    @Command
    @PostMapping("/manager/prolong_subscription")
    fun prolongSubscription(
        @RequestParam("sid") sid: Long,
        @RequestParam("days") days: Long
    ) {
        dao.addEvent(Event(ProlongSubscription(Duration.ofDays(days)), sid))
    }
}
