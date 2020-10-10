package reading

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import junit.framework.TestCase
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyLong
import java.nio.file.Files
import java.nio.file.Paths

class TestFullReader : TestCase() {
    private val coreReader = Mockito.mock(CoreReaderVK::class.java)

    private fun <T> any(): T = Mockito.any<T>()
    private fun <T> eq(obj: T): T = Mockito.eq<T>(obj)

    @Test
    fun testReadNothing() {
        val fullReader = FullReader("cat", 100, 200, coreReader)

        `when`(coreReader.tryToRead(any(), any(), anyLong(), anyLong())).thenReturn(Old(JsonObject()))

        assertEquals(false, fullReader.hasNext())
    }

    @Test
    fun testReadOnePage() {
        val fullReader = FullReader("cat", 100, 200, coreReader)

        val json = readJson("responseOnOnePageVK").single()
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
        val fullReader = FullReader("cat", 100, 200, coreReader)

        val json = readJson("responseFewPagesVK_1", "responseFewPagesVK_2", "responseFewPagesVK_3")
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
}

fun readJson(vararg fileNames: String): List<JsonElement> {
    val filePath = System.getProperty("user.dir") + "/src/test/resources/"

    val result = mutableListOf<JsonElement>()
    for (fileName in fileNames) {
        val response = Files.readString(Paths.get(filePath + fileName))
        val json = JsonParser.parseString(response).asJsonObject["response"] //todo: move `.asJsonObject["response"]` to parser

        result += json
    }

    return result
}
