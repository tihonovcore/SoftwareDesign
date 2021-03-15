package application.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HeartbeatController {
    @GetMapping("/")
    fun heartbeat(): String {
        return "i'm alive"
    }
}
