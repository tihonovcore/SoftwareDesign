package application.controller

import application.annotation.Query

class ReportController {
    @Query
    fun statisticPerDay() {}

    @Query
    fun avgFrequency() {}

    @Query
    fun avgDuration() {}
}
