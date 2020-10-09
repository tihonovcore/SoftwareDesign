import com.google.gson.JsonElement
import com.google.gson.JsonParser
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.IllegalStateException
import java.net.URL

//data class Configuration(val hashtag: String, val startTime: Long, val endTime: Long)

class FullReader(
    val hashtag: String,
    val startTime: Long,
    val endTime: Long,
    private val coreReader: CoreReader
){
    private var lastReadValue: ReadValue = Empty

    fun hasNext(): Boolean {
        lastReadValue = coreReader.tryToRead(lastReadValue, hashtag, startTime, endTime)

        return when (lastReadValue) {
            is New -> true
            else -> false
        }
    }

    fun read(): JsonElement {
        lastReadValue = coreReader.tryToRead(lastReadValue, hashtag, startTime, endTime)

        val currentValue = lastReadValue
        if (currentValue is New) {
            lastReadValue = Old(currentValue.json)
            return currentValue.json
        }

        throw IllegalStateException("Reading ended")
    }
}

interface CoreReader {
    fun tryToRead(lastReadValue: ReadValue, hashtag: String, startTime: Long, endTime: Long): ReadValue
}

class CoreReaderVK : CoreReader {
    private val prefix = "https://api.vk.com/method"
    private val request = "newsfeed.search?"

    //todo: move to config
    private val token = "def35358a44036e714c0b5da143f906c0a9b8532b0854e7130e40f8054f0256b6e2ea5f1db011c0260fcb"

    override fun tryToRead(lastReadValue: ReadValue, hashtag: String, startTime: Long, endTime: Long): ReadValue {
        if (lastReadValue is New) return lastReadValue

        val link: String
        if (isFirstRequest(lastReadValue)) {
            link = buildRequest(hashtag, startTime, endTime)
        } else {
            val nextFrom = ResponseParserVK().parseNextPageRequest((lastReadValue as Old).json) ?: return lastReadValue
            link = buildRequest(hashtag, nextFrom)
        }
        println(link) //todo: remove
        val url = URL(link)
        BufferedReader(InputStreamReader(url.openStream())).use {
            val text = it.readText()
            val json = JsonParser.parseString(text).asJsonObject["response"]
            return New(json)
        }
    }

    private fun commonBuildRequest(hashtag: String): Pair<String, String> {
        return Pair("$prefix/${request}q=%23${hashtag}", "&count=200&v=5.52&access_token=$token")
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

sealed class ReadValue
object Empty : ReadValue()
data class New(val json: JsonElement) : ReadValue()
data class Old(val json: JsonElement) : ReadValue()
