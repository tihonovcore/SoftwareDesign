import junit.framework.TestCase
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import parsing.Date
import parsing.ResponseParserVK
import reading.*

class TestFrequencyCounter : TestCase() {
    private val json = readJson("responseOnOnePageVK").single() //JsonParser.parseString(response).asJsonObject["response"] //todo: move `.asJsonObject["response"]` to parser

    private val currentTime = System.currentTimeMillis() / 1000
    private val hourInSeconds = 60 * 60

    private fun <T> any(): T = Mockito.any<T>()
    private fun <T> eq(obj: T): T = Mockito.eq<T>(obj)

    @Test
    fun testCountAgainstVK() {
        val coreReader = mock(CoreReaderVK::class.java)
        val responseParser = mock(ResponseParserVK::class.java)

        val hashtag = "cat"
        val someDates = listOf(
            Date(currentTime - 1000),
            Date(currentTime - hourInSeconds - 1000),
            Date(currentTime - hourInSeconds - 2000)
        )

        `when`(coreReader.tryToRead(eq(Empty), any(), anyLong(), anyLong())).thenReturn(New(json))
        `when`(coreReader.tryToRead(eq(New(json)), any(), anyLong(), anyLong())).thenReturn(New(json))
        `when`(coreReader.tryToRead(eq(Old(json)), any(), anyLong(), anyLong())).thenReturn(Old(json))
        `when`(responseParser.parseDates(json)).thenReturn(someDates)

        val actualResult = FrequencyCounter(coreReader, responseParser).count(hashtag)
        val expectedResult = List(24) { i -> if (i < 22) 0 else if (i == 22) 2 else 1 }

        assertEquals(expectedResult, actualResult)
    }
}
