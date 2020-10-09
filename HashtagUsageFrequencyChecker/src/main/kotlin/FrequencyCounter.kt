class FrequencyCounter(
    private val hashtagReader: HashtagReader,
    private val responseParser: ResponseParser
) {
    fun count(hashtag: String): List<Int> {
        val currentTime = System.currentTimeMillis() / 1000
        val hourInSeconds = 60 * 60
        val dayInSeconds = 24 * hourInSeconds

        hashtagReader.configure(hashtag, currentTime - dayInSeconds, currentTime)

        val items = mutableListOf<Date>()
        while (hashtagReader.hasNext()) {
            items += responseParser.parseDates(hashtagReader.read())
        }

        val timeRanges = List(24) { i ->
            val from = currentTime - (24 - i) * hourInSeconds
            val to  = from +  hourInSeconds

            Pair(from, to)
        }

        val result = mutableListOf<Int>()
        for ((from, to) in timeRanges) {
            result += items.count { it.date in from until to }
        }

        return result
    }
}

fun main() {
    //test VK
    val counter = FrequencyCounter(HashtagReaderVK(), ResponseParserVK())

//    println(counter.count("a"))
    println(counter.count("cat"))
}
