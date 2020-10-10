import junit.framework.TestCase
import org.junit.Test
import org.mockito.Mockito.*
import parsing.Date
import parsing.ResponseParserVK
import reading.*

class TestFrequencyCounter : TestCase() {
    private val json = readFiles("responseOnOnePageVK").toJsonVK().single()

    private val currentTime = System.currentTimeMillis() / 1000
    private val hourInSeconds = 60 * 60

    @Test
    fun testCountAgainstVK() {
        val coreReader = mock(CoreReaderVK::class.java)
        val responseParser = mock(ResponseParserVK::class.java)

        val hashtag = "cat"
        val someDates = listOf<Date>(
            currentTime - 1000,
            currentTime - hourInSeconds - 1000,
            currentTime - hourInSeconds - 2000
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
