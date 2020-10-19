package unit

import AbstractServletTest
import checkDatabaseConsistency
import org.junit.Assert.assertThrows
import org.junit.Test
import prepareDatabase
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet
import testTemplate
import java.io.IOException

class AddProductTest : AbstractServletTest() {
    override val servletElements = listOf(
        AddProductServlet() to "/add-product"
    )

    @Test
    fun testAddOneProduct() {
        prepareDatabase()

        testTemplate(
            requests = listOf("http://localhost:8081/add-product?name=wolfMask&price=367"),
            expectedResponses = listOf("OK\n")
        )

        checkDatabaseConsistency(listOf(
            "wolfMask" to 367
        ))
    }

    @Test
    fun testAddFewProducts() {
        prepareDatabase()

        testTemplate(
            requests = listOf(
                "http://localhost:8081/add-product?name=pikachu&price=14",
                "http://localhost:8081/add-product?name=iphone12mini&price=992",
                "http://localhost:8081/add-product?name=wolfMask&price=100002"
            ),
            expectedResponses = listOf("OK\n", "OK\n", "OK\n")
        )

        checkDatabaseConsistency(listOf(
            "wolfMask" to 100_002,
            "iphone12mini" to 992,
            "pikachu" to 14
        ))
    }

    @Test
    fun testAddFewProductsWithMatchedNames() {
        prepareDatabase()

        testTemplate(
            requests = listOf(
                "http://localhost:8081/add-product?name=starship&price=37",
                "http://localhost:8081/add-product?name=falcon9&price=17",
                "http://localhost:8081/add-product?name=starship&price=227",
                "http://localhost:8081/add-product?name=falcon9&price=44567"
            ),
            expectedResponses = listOf("OK\n", "OK\n", "OK\n", "OK\n")
        )

        checkDatabaseConsistency(listOf(
            "starship" to 37,
            "falcon9" to 17,
            "starship" to 227,
            "falcon9" to 44_567
        ))
    }

    @Test
    fun testWrongRequest() {
        prepareDatabase()

        assertThrows(IOException::class.java) {
            testTemplate(
                requests = listOf("http://localhost:8081/add-product?nameeeeee=woalfMask&purice=367"),
                expectedResponses = listOf("OK\n")
            )
        }

        checkDatabaseConsistency(emptyList())
    }
}
