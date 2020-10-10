package reading

import com.xebialabs.restito.builder.stub.StubHttp.whenHttp
import com.xebialabs.restito.semantics.Action.stringContent
import com.xebialabs.restito.semantics.Condition.*
import com.xebialabs.restito.server.StubServer
import junit.framework.TestCase
import org.glassfish.grizzly.http.Method
import org.junit.Test
import parsing.ResponseParserVK
import readFiles

class TestCodeReaderVK : TestCase() {
    private val port = 32543
    private val coreReader = CoreReaderVK("http://localhost:$port/method/newsfeed.search")

    @Test
    fun testOnePage() {
        val stubServer = StubServer(port).run()
        val json = readFiles("responseOnOnePageVK").single()
        whenHttp(stubServer).match(method(Method.GET), startsWithUri("/method")).then(stringContent(json))

        val result = coreReader.tryToRead(Empty, hashtag, startTime, endTime)
        stubServer.stop()

        val expected = listOf<Long>(1602244353, 1602244200, 1602243001)
        val dates = ResponseParserVK().parseDates((result as New).json)
        assertEquals(expected, dates)
    }

    @Test
    fun testFewPages() {
        val stubServer = StubServer(port).run()
        val json = readFiles("responseFewPagesVK_1", "responseFewPagesVK_2", "responseFewPagesVK_3")

        whenHttp(stubServer)
            .match(custom { call -> "start_time" in call.request.parameterNames })
            .then(stringContent(json[0]))
        whenHttp(stubServer)
            .match(custom { call -> "page2" == call.request.parameterMap["start_from"]?.single() })
            .then(stringContent(json[1]))
        whenHttp(stubServer)
            .match(custom { call -> "page3" == call.request.parameterMap["start_from"]?.single() })
            .then(stringContent(json[2]))

        val result = mutableListOf<ReadValue>()
        result += coreReader.tryToRead(Empty, hashtag, startTime, endTime)
        result += coreReader.tryToRead(result.last().makeOld(), hashtag, startTime, endTime)
        result += coreReader.tryToRead(result.last().makeOld(), hashtag, startTime, endTime)
        stubServer.stop()

        val expectedDates = listOf<Long>(
            1602244353, 1602244200, 1602243001,
            1602244354, 1602244201, 1602243002,
            1602244355, 1602244204, 1602243031
        )

        val actualDates = result.flatMap { ResponseParserVK().parseDates((it as New).json) }
        assertEquals(expectedDates, actualDates)
    }

    private fun ReadValue.makeOld(): ReadValue {
        require(this is New)
        return Old(json)
    }

    companion object {
        const val hashtag = "cat"
        const val startTime: Long = 0
        const val endTime: Long = 2
    }
}
