package reading

import com.xebialabs.restito.builder.stub.StubHttp.whenHttp
import com.xebialabs.restito.semantics.Action.stringContent
import com.xebialabs.restito.semantics.Condition.*
import com.xebialabs.restito.server.StubServer
import junit.framework.TestCase
import org.glassfish.grizzly.http.Method
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Paths

class TestCodeReaderVK : TestCase() {
    val port = 32543
    val coreReader = CoreReaderVK("http://localhost:$port/method")

    @Test
    fun testOnePage() {
        val stubServer = StubServer(port).run()
        val json = readFiles("responseOnOnePageVK").single()
        whenHttp(stubServer).match(method(Method.GET), startsWithUri("/method")).then(stringContent(json))

        val result = coreReader.tryToRead(Empty, "cat", 0, 2)
        stubServer.stop()

        val expected = listOf<Long>(1602244353, 1602244200, 1602243001)
        val dates = (result as New).json.asJsonObject["items"].asJsonArray.map { it.asJsonObject["date"].asLong }
        assertEquals(expected, dates)
    }

    @Test
    fun testFewPages() {
        val stubServer = StubServer(port).run()
        val json = readFiles("responseFewPagesVK_1", "responseFewPagesVK_2", "responseFewPagesVK_3")
        whenHttp(stubServer).match(custom({ call -> "start_time" in call.request.parameterNames })).then(stringContent(json[0]))
        whenHttp(stubServer).match(custom({ call -> "page2" == call.request.parameterMap["start_from"]?.single() })).then(stringContent(json[1]))
        whenHttp(stubServer).match(custom({ call -> "page3" == call.request.parameterMap["start_from"]?.single() })).then(stringContent(json[2]))

        val result = mutableListOf<ReadValue>()
        result += coreReader.tryToRead(Empty, "cat", 0, 2)
        result += coreReader.tryToRead(result.last().makeOld(), "cat", 0, 2)
        result += coreReader.tryToRead(result.last().makeOld(), "cat", 0, 2)
        stubServer.stop()

        val expected = listOf<Long>(1602244353, 1602244200, 1602243001, 1602244354, 1602244201, 1602243002, 1602244355, 1602244204, 1602243031)
        val dates = result.flatMap { (it as New).json.asJsonObject["items"].asJsonArray }.map { it.asJsonObject["date"].asLong }
        assertEquals(expected, dates)
    }

    fun ReadValue.makeOld(): ReadValue {
        require(this is New)
        return Old(json)
    }

    private fun readFiles(vararg names: String): List<String> {
        val result = mutableListOf<String>()
        for (name in names) {
            val path = System.getProperty("user.dir") + "/src/test/resources/$name"
            result += Files.readString(Paths.get(path))
        }
        return result
    }
}
