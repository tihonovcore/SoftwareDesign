class FrequencyCounter {
    fun count(hashtag: String): List<Int> {
        val currentTime = System.currentTimeMillis() / 1000
        val hourInSeconds = 60 * 60
        val dayInSeconds = 24 * hourInSeconds

        //todo get as arg and configure here
        val hashtagReader = HashtagReader(hashtag, currentTime - dayInSeconds, currentTime)

        val items = mutableListOf<Item>()
        while (hashtagReader.hasNext()) {
            items += ResponseParser(hashtagReader.read()).parseDates()
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
    val counter = FrequencyCounter()

//    println(counter.count("a"))
    println(counter.count("cat"))
}
