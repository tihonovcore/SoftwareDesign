package reading

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import junit.framework.TestCase
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyLong
import readFiles
import toJsonVK
import any
import eq

class TestFullReader : TestCase() {
    @Test
    fun testReadNothing() {
        val coreReader = Mockito.mock(CoreReaderVK::class.java)
        val fullReader = FullReader(hashtag, startTime, endTime, coreReader)

        `when`(coreReader.tryToRead(any(), any(), anyLong(), anyLong())).thenReturn(Old(JsonObject()))

        assertEquals(false, fullReader.hasNext())
    }

    @Test
    fun testReadOnePage() {
        val coreReader = Mockito.mock(CoreReaderVK::class.java)
        val fullReader = FullReader(hashtag, startTime, endTime, coreReader)

        val json = readFiles("responseOnOnePageVK").toJsonVK().single()
        `when`(coreReader.tryToRead(eq(Empty), any(), anyLong(), anyLong())).thenReturn(New(json))
        `when`(coreReader.tryToRead(eq(New(json)), any(), anyLong(), anyLong())).thenReturn(New(json))
        `when`(coreReader.tryToRead(eq(Old(json)), any(), anyLong(), anyLong())).thenReturn(Old(json))

        val elements = mutableListOf<JsonElement>()
        while(fullReader.hasNext()) {
            elements += fullReader.read()
        }

        assertEquals(1, elements.size)
        assertEquals(json, elements.single())
    }

    @Test
    fun testReadFewPages() {
        val coreReader = Mockito.mock(CoreReaderVK::class.java)
        val fullReader = FullReader(hashtag, startTime, endTime, coreReader)

        val json = readFiles(
            "responseFewPagesVK_1",
            "responseFewPagesVK_2",
            "responseFewPagesVK_3"
        ).toJsonVK()

        `when`(coreReader.tryToRead(eq(Empty), any(), anyLong(), anyLong())).thenReturn(New(json[0]))
        `when`(coreReader.tryToRead(eq(New(json[0])), any(), anyLong(), anyLong())).thenReturn(New(json[0]))
        `when`(coreReader.tryToRead(eq(Old(json[0])), any(), anyLong(), anyLong())).thenReturn(New(json[1]))
        `when`(coreReader.tryToRead(eq(New(json[1])), any(), anyLong(), anyLong())).thenReturn(New(json[1]))
        `when`(coreReader.tryToRead(eq(Old(json[1])), any(), anyLong(), anyLong())).thenReturn(New(json[2]))
        `when`(coreReader.tryToRead(eq(New(json[2])), any(), anyLong(), anyLong())).thenReturn(New(json[2]))
        `when`(coreReader.tryToRead(eq(Old(json[2])), any(), anyLong(), anyLong())).thenReturn(Old(json[2]))

        val elements = mutableListOf<JsonElement>()
        while(fullReader.hasNext()) {
            elements += fullReader.read()
        }

        assertEquals(3, elements.size)
        assertEquals(json, elements)
    }

    companion object {
        const val hashtag = "cat"
        const val startTime: Long = 100
        const val endTime: Long = 200
    }
}
