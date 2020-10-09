import com.google.gson.JsonElement
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.IllegalStateException
import java.net.URL

/* Possible implementation
HR_Interface {
    read
    hasNext
}

HR {
    val hr
    configure(config) {
        hr = HR_internal(config)
    }
    read = hr.read
    hasNext = hr.hasNext
}

HR_internal {
    this one
}
 */

class HashtagReader(
    val hashtag: String,
    val startTime: Long,
    val endTime: Long
) {
    private val prefix = "https://api.vk.com/method"
    private val request = "newsfeed.search?"
    //todo: move to config
    private val token = "def35358a44036e714c0b5da143f906c0a9b8532b0854e7130e40f8054f0256b6e2ea5f1db011c0260fcb"

    private var lastReadValue: ReadValue = Empty
    fun hasNext(): Boolean {
        tryToRead()

        return when (lastReadValue) {
            is New -> true
            else -> false
        }
    }

    fun read(): JsonElement {
        tryToRead()

        val currentValue = lastReadValue
        if (currentValue is New) {
            lastReadValue = Old(currentValue.json)
            return currentValue.json
        }

        throw IllegalStateException("Reading ended")
    }

    //todo: clear
    private fun tryToRead() {
        if (lastReadValue is New) return

        val link: String
        if (isFirstRequest()) {
            link = buildRequest(startTime, endTime)
        } else {
            val nextFrom = ResponseParser((lastReadValue as Old).json).parseNextFrom() ?: return
            link = buildRequest(nextFrom)
        }
        println(link)
        val url = URL(link)
        BufferedReader(InputStreamReader(url.openStream())).use {
            val text = it.readText()
            val json = ResponseParser(text).json
            lastReadValue = New(json)
        }
    }

    private fun commonBuildRequest(): Pair<String, String> {
        return Pair("$prefix/${request}q=%23$hashtag", "&count=200&v=5.52&access_token=$token")
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
