package reading

import parsing.ResponseParserVK
import com.google.gson.JsonParser
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

open class CoreReaderVK(val prefix: String = "https://api.vk.com/method") : CoreReader {
//    private val prefix = "https://api.vk.com/method"
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
            println("text: $text") //todo: remove
            val json = JsonParser.parseString(text).asJsonObject["response"] //todo: move to parser
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
