package parsing

import junit.framework.TestCase
import org.junit.Test
import readFiles
import toJsonVK

class TestResponseParserVK : TestCase() {
    private val json = readFiles("responseSampleVK").toJsonVK().single()

    @Test
    fun testParseDates() {
        val actualResult = ResponseParserVK().parseDates(json).toSet()
        val expectedResult = setOf<Long>(1602244353, 1602244200, 1602243001)

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun testParseNextPageRequest() {
        val actualResult = ResponseParserVK().parseNextPageRequest(json)
        val expectedResult = "3/-134839254_198407"

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun testWrongJson() {
        assert(false) { "todo" }
    }
}
