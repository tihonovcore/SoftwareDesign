import com.xebialabs.restito.builder.stub.StubHttp.whenHttp
import com.xebialabs.restito.semantics.*
import com.xebialabs.restito.semantics.Action.*
import com.xebialabs.restito.server.StubServer
import junit.framework.TestCase
import org.glassfish.grizzly.http.util.HttpStatus
import org.junit.Test

class TestActors : TestCase() {
    private val port = 32543

    private val google = Condition.custom { call -> call.uri.startsWith("/google") }
    private val yandex = Condition.custom { call -> call.uri.startsWith("/yandex") }
    private val bing = Condition.custom { call -> call.uri.startsWith("/bing") }

    private val json = """{ answers: ["1#", "2#", "3#", "4#", "5#"] }"""
    private val gJson = json.replace('#', 'g')
    private val yJson = json.replace('#', 'y')
    private val bJson = json.replace('#', 'b')

    @Test
    fun testResponse() {
        val stubServer = StubServer(port).run()
        whenHttp(stubServer).match(google).then(stringContent(gJson))
        whenHttp(stubServer).match(yandex).then(stringContent(yJson))
        whenHttp(stubServer).match(bing).then(stringContent(bJson))

        val actual = aggregate("Я_ЕМУ_СКАЗАЛА_НЕ_ТОПТАТЬ")
        val expected = setOf(
            "1g", "2g", "3g", "4g", "5g",
            "1y", "2y", "3y", "4y", "5y",
            "1b", "2b", "3b", "4b", "5b"
        )

        stubServer.stop()
        assertEquals(expected, actual)
    }

    @Test
    fun testLongResponse() {
        val stubServer = StubServer(port).run()
        whenHttp(stubServer).match(google).then(stringContent(gJson))
        whenHttp(stubServer).match(yandex).then(stringContent(yJson))
        whenHttp(stubServer).match(bing).then(composite(delay(1500), stringContent(bJson)))

        val actual = aggregate("ТЫ_СМОТРИ_ЗА_БУТЫЛКОЙ_ТЯНЕТСЯ")
        val expected = setOf(
                "1g", "2g", "3g", "4g", "5g",
                "1y", "2y", "3y", "4y", "5y"
        )

        stubServer.stop()
        assertEquals(expected, actual)
    }

    @Test
    fun testErrorResponse() {
        val stubServer = StubServer(port).run()
        whenHttp(stubServer).match(google).then(stringContent(gJson))
        whenHttp(stubServer).match(yandex).then(stringContent(yJson))
        whenHttp(stubServer).match(bing).then(status(HttpStatus.BAD_GATEWAY_502))

        val actual = aggregate("ТЫ_ЧЕ_ДОЛБИШЬ")
        val expected = setOf(
            "1g", "2g", "3g", "4g", "5g",
            "1y", "2y", "3y", "4y", "5y"
        )

        stubServer.stop()
        assertEquals(expected, actual)
    }
}
