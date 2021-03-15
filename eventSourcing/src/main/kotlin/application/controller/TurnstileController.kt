package application.controller

import application.annotation.Command
import application.dao.EventDao
import application.model.*
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
class TurnstileController(val dao: EventDao) {
    @Command
    @PostMapping("/turnstile/enter")
    fun enter(@RequestParam("sid") sid: Long): String {
        val eventsForCurrentSubscription = dao.getEvents().filter { it.subscriptionId == sid }

        val creation = eventsForCurrentSubscription.first()
        require(creation.type is NewSubscription) { return "fail" }

        val prolongations = eventsForCurrentSubscription.filter { it.type is ProlongSubscription }
        val endTime = prolongations.fold(creation.timestamp + creation.type.duration) { accum, prolong ->
            accum + (prolong.type as ProlongSubscription).duration
        }

        //TODO: use clock pattern
        if (Instant.now() < endTime) {
            dao.addEvent(Event(Enter, sid))
            return "success"
        } else {
            return "fail"
        }
    }

    @Command
    @PostMapping("/turnstile/exit")
    fun exit(@RequestParam("sid") sid: Long) {
        dao.addEvent(Event(Exit, sid))
    }
}
