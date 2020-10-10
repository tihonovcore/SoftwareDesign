package reading

import parsing.ResponseParserVK
import com.google.gson.JsonParser
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

open class CoreReaderVK(
    private val prefix: String = "https://api.vk.com/method/newsfeed.search"
) : CoreReader {
    private val token = Config.loadToken()

    override fun tryToRead(
        lastReadValue: ReadValue,
        hashtag: String,
        startTime: Long,
        endTime: Long
    ): ReadValue {
        if (lastReadValue is New) return lastReadValue

        val request = buildRequest(lastReadValue, hashtag, startTime, endTime) ?: return lastReadValue
        val url = URL(request)
        BufferedReader(InputStreamReader(url.openStream())).use {
            val text = it.readText()
            val json = JsonParser.parseString(text)
            return New(json)
        }
    }

    private fun buildRequest(lastReadValue: ReadValue, hashtag: String, startTime: Long, endTime: Long): String? {
        if (isFirstRequest(lastReadValue)) {
            return buildRequest(hashtag, startTime, endTime)
        } else {
            val json = (lastReadValue as Old).json
            val nextFrom = ResponseParserVK().parseNextPageRequest(json) ?: return null
            return buildRequest(hashtag, nextFrom)
        }
    }

    private fun commonBuildRequest(hashtag: String): Pair<String, String> {
        return Pair("$prefix?q=%23${hashtag}", "&count=200&v=5.52&access_token=$token")
    }

    private fun buildRequest(hashtag: String, startTime: Long, endTime: Long): String {
        val (prefix, suffix) = commonBuildRequest(hashtag)
        return "$prefix&start_time=$startTime&end_time=$endTime$suffix"
    }

    private fun buildRequest(hashtag: String, nextFrom: String): String {
        val (prefix, suffix) = commonBuildRequest(hashtag)
        return "$prefix&start_from=$nextFrom$suffix"
    }

    private fun isFirstRequest(lastReadValue: ReadValue): Boolean {
        return lastReadValue is Empty
    }
}
