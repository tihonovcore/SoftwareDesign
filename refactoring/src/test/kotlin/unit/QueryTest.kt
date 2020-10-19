package unit

import AbstractServletTest
import checkDatabaseConsistency
import org.junit.Test
import prepareDatabase
import ru.akirakozov.sd.refactoring.servlet.QueryServlet
import testTemplate

class QueryTest : AbstractServletTest() {
    override val servletElements = listOf(
        QueryServlet() to "/query"
    )

    @Test
    fun testMax() {
        val fillSql =
            """insert into Product (Name, Price) values 
              |    ("Coca", 123), 
              |    ("Cola", 125);
            """.trimMargin()
        prepareDatabase(fillSql)

        testTemplate(
            requests = listOf(
                "http://localhost:8081/query?command=max"
            ),
            expectedResponses = listOf(
                """<html><body>
                  |<h1>Product with max price: </h1>
                  |Cola${'\t'}125</br>
                  |</body></html>
                  |
                """.trimMargin()
            )
        )

        checkDatabaseConsistency(listOf(
            "Coca" to 123,
            "Cola" to 125
        ))
    }

    @Test
    fun testMin() {
        val fillSql =
            """insert into Product (Name, Price) values 
              |    ("Coca", 123),
              |    ("Tea" ,  40),
              |    ("Cola", 125);
            """.trimMargin()
        prepareDatabase(fillSql)

        testTemplate(
            requests = listOf(
                "http://localhost:8081/query?command=min"
            ),
            expectedResponses = listOf(
                """<html><body>
                  |<h1>Product with min price: </h1>
                  |Tea	40</br>
                  |</body></html>
                  |
                """.trimMargin()
            )
        )

        checkDatabaseConsistency(listOf(
            "Coca" to 123,
            "Cola" to 125,
            "Tea" to 40
        ))
    }

    @Test
    fun testSum() {
        val fillSql =
            """insert into Product (Name, Price) values 
              |    ("Coca", 123),
              |    ("Wolf", 90000),
              |    ("Cola", 125);
            """.trimMargin()
        prepareDatabase(fillSql)

        testTemplate(
            requests = listOf(
                "http://localhost:8081/query?command=sum"
            ),
            expectedResponses = listOf(
                """<html><body>
                  |Summary price: 
                  |90248
                  |</body></html>
                  |
                """.trimMargin()
            )
        )

        checkDatabaseConsistency(listOf(
            "Coca" to 123,
            "Cola" to 125,
            "Wolf" to 90_000
        ))
    }

    @Test
    fun testCount() {
        val fillSql =
            """insert into Product (Name, Price) values 
              |    ("Coca", 123),
              |    ("Cheese", 300),
              |    ("Lemon", 304),
              |    ("Covid-test", 34),
              |    ("Cola", 125);
            """.trimMargin()
        prepareDatabase(fillSql)

        testTemplate(
            requests = listOf(
                "http://localhost:8081/query?command=count"
            ),
            expectedResponses = listOf(
                """<html><body>
                  |Number of products: 
                  |5
                  |</body></html>
                  |
                """.trimMargin()
            )
        )

        checkDatabaseConsistency(listOf(
            "Coca" to 123,
            "Cheese" to 300,
            "Lemon" to 304,
            "Covid-test" to 34,
            "Cola" to 125
        ))
    }
}
