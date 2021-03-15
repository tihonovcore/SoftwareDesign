package application.controller

import application.annotation.Command
import application.annotation.Query

class ManagerController {
    @Query
    fun subscriptionInfo() {}

    @Command
    fun newSubscription() {}

    @Command
    fun prolongSubscription() {}
}
