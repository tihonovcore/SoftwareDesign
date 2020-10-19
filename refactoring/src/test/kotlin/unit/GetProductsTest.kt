package unit

import AbstractServletTest
import checkDatabaseConsistency
import org.junit.Test
import prepareDatabase
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet
import testTemplate

class GetProductsTest : AbstractServletTest() {
    override val servletElements = listOf(
        GetProductsServlet() to "/get-products"
    )

    @Test
    fun testGetEmptyList() {
        prepareDatabase()

        testTemplate(
            requests = listOf("http://localhost:8081/get-products"),
            expectedResponses = listOf("<html><body>\n</body></html>\n")
        )

        checkDatabaseConsistency(emptyList())
    }

    @Test
    fun testGetNonEmptyList() {
        val fillSql =
            """insert into Product (Name, Price) values
              |    ("Tea", 500),
              |    ("Coffee", 9500),
              |    ("Flat", 5900000),
              |    ("Jedi", 7200000);
            """.trimMargin()
        prepareDatabase(fillSql)

        testTemplate(
            requests = listOf("http://localhost:8081/get-products"),
            expectedResponses = listOf(
                """<html><body>
                  |Tea	500</br>
                  |Coffee	9500</br>
                  |Flat	5900000</br>
                  |Jedi	7200000</br>
                  |</body></html>
                  |
                """.trimMargin()
            )
        )

        checkDatabaseConsistency(listOf(
            "Tea" to 500,
            "Coffee" to 9500,
            "Jedi" to 7_200_000,
            "Flat" to 5_900_000
        ))
    }
}
