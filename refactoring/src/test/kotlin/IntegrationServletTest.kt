import org.junit.Test
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet
import ru.akirakozov.sd.refactoring.servlet.QueryServlet

class IntegrationServletTest : AbstractServletTest() {
    override val servletElements = listOf(
        GetProductsServlet() to "/get-products",
        AddProductServlet() to "/add-product",
        QueryServlet() to "/query"
    )

    @Test
    fun testSimple() {
        val fillSql =
            """insert into Product (Name, Price) values 
              |    ("rick", 123);
            """.trimMargin()
        prepareDatabase(fillSql)

        testTemplate(
            requests = listOf(
                "http://localhost:8081/query?command=max",
                "http://localhost:8081/add-product?name=and&price=234",
                "http://localhost:8081/query?command=max",
                "http://localhost:8081/query?command=sum",
                "http://localhost:8081/add-product?name=morty&price=345",
                "http://localhost:8081/query?command=sum",
                "http://localhost:8081/query?command=count",
                "http://localhost:8081/get-products"
            ),
            expectedResponses = listOf(
                """<html><body>
                |<h1>Product with max price: </h1>
                |rick	123</br>
                |</body></html>
                |
                """.trimMargin(),
                "OK\n",
                """<html><body>
                |<h1>Product with max price: </h1>
                |and	234</br>
                |</body></html>
                |
                """.trimMargin(),
                """<html><body>
                |Summary price: 
                |357
                |</body></html>
                |
                """.trimMargin(),
                "OK\n",
                """<html><body>
                |Summary price: 
                |702
                |</body></html>
                |
                """.trimMargin(),
                """<html><body>
                |Number of products: 
                |3
                |</body></html>
                |
                """.trimMargin(),
                """<html><body>
                |rick	123</br>
                |and	234</br>
                |morty	345</br>
                |</body></html>
                |
                """.trimMargin()
            )
        )

        checkDatabaseConsistency(listOf(
            "rick" to 123,
            "and" to 234,
            "morty" to 345
        ))
    }
}