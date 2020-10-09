import com.google.gson.JsonElement
import com.google.gson.JsonParser
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.IllegalStateException
import java.net.URL

interface HashtagReader {
    fun hasNext(): Boolean
    fun read(): JsonElement
    fun configure(hashtag: String, startTime: Long, endTime: Long)
}

data class Configuration(val hashtag: String, val startTime: Long, val endTime: Long)

class HashtagReaderVK : HashtagReader {
    private var configuration: Configuration = Configuration("", -1, -1)
    private val prefix = "https://api.vk.com/method"
    private val request = "newsfeed.search?"

    //todo: move to config
    private val token = "def35358a44036e714c0b5da143f906c0a9b8532b0854e7130e40f8054f0256b6e2ea5f1db011c0260fcb"

    private var lastReadValue: ReadValue = Empty
    override fun hasNext(): Boolean {
        tryToRead()

        return when (lastReadValue) {
            is New -> true
            else -> false
        }
    }

    override fun read(): JsonElement {
        tryToRead()

        val currentValue = lastReadValue
        if (currentValue is New) {
            lastReadValue = Old(currentValue.json)
            return currentValue.json
        }

        throw IllegalStateException("Reading ended")
    }

    override fun configure(hashtag: String, startTime: Long, endTime: Long) {
        configuration = Configuration(hashtag, startTime, endTime)
        lastReadValue = Empty
    }

    //todo: clear
    private fun tryToRead() {
        if (lastReadValue is New) return

        val link: String
        if (isFirstRequest()) {
            link = buildRequest(configuration.startTime, configuration.endTime)
        } else {
            val nextFrom = ResponseParserVK().parseNextPageRequest((lastReadValue as Old).json) ?: return
            link = buildRequest(nextFrom)
        }
        println(link) //todo: remove
        val url = URL(link)
        BufferedReader(InputStreamReader(url.openStream())).use {
            val text = it.readText()
            val json = JsonParser.parseString(text).asJsonObject["response"]
            lastReadValue = New(json)
        }
    }

    private fun commonBuildRequest(): Pair<String, String> {
        return Pair("$prefix/${request}q=%23${configuration.hashtag}", "&count=200&v=5.52&access_token=$token")
    }

    private fun buildRequest(startTime: Long, endTime: Long): String {
        val (prefix, suffix) = commonBuildRequest()
        return "$prefix&start_time=$startTime&end_time=$endTime$suffix"
    }

    private fun buildRequest(nextFrom: String): String {
        val (prefix, suffix) = commonBuildRequest()
        return "$prefix&start_from=$nextFrom$suffix"
    }

    private fun isFirstRequest(): Boolean {
        return lastReadValue is Empty
    }
}

sealed class ReadValue
object Empty : ReadValue()
data class New(val json: JsonElement) : ReadValue()
data class Old(val json: JsonElement) : ReadValue()
