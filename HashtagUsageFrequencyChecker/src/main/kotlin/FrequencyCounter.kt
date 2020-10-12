import parsing.Date
import parsing.ResponseParser
import parsing.ResponseParserVK
import reading.CoreReader
import reading.CoreReaderVK
import reading.FullReader

/**
 * Count hashtags usage frequency
 */
class FrequencyCounter(
    private val coreReader: CoreReader,
    private val responseParser: ResponseParser
) {
    /**
     * Count and returns hashtags usage frequency in next format:
     * list of 24 numbers, where i-th means that (24 - i) hours ago
     * have posted correspond number of posts.
     */
    fun count(hashtag: String): List<Int> {
        val currentTime = System.currentTimeMillis() / 1000
        val fullReader = FullReader(hashtag, currentTime - dayInSeconds, currentTime, coreReader)

        val items = mutableListOf<Date>()
        while (fullReader.hasNext()) {
            items += responseParser.parseDates(fullReader.read())
        }

        val timeRanges = List(24) { i ->
            val from = currentTime - (24 - i) * hourInSeconds
            val to = from + hourInSeconds

            Pair(from, to)
        }

        val result = mutableListOf<Int>()
        for ((from, to) in timeRanges) {
            result += items.count { it in from until to }
        }

        return result
    }

    companion object {
        const val hourInSeconds = 60 * 60
        const val dayInSeconds = 24 * hourInSeconds
    }
}
