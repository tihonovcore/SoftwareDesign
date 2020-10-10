package parsing

import com.google.gson.JsonParser
import junit.framework.TestCase
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Paths

class TestResponseParserVK : TestCase() {
    private val filePath = System.getProperty("user.dir") + "/src/test/resources/responseSampleVK"
    private val response = Files.readString(Paths.get(filePath))
    private val json = JsonParser.parseString(response).asJsonObject["response"] //todo: move `.asJsonObject["response"]` to parser

    @Test
    fun testParseDates() {
        val actualResult = ResponseParserVK().parseDates(json).map{ it.date }.toSet()
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
