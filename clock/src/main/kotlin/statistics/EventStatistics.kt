package statistics

interface EventsStatistic {
    fun incEvent(eventName: EventName)
    fun getEventStatisticByName(eventName: EventName): Double
    fun getAllEventStatistic(): Map<EventName, Double>
    fun printStatistic()
}
