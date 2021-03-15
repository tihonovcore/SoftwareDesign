package application.controller

import application.annotation.Command

class TurnstileController {
    @Command
    fun enter() {}

    @Command
    fun exit() {}
}
