import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import javax.servlet.http.HttpServlet

private const val testDatabaseUrl = "jdbc:sqlite:test.db"

internal fun <T> databaseAction(action: (Statement) -> T): T {
    DriverManager.getConnection(testDatabaseUrl).use { c ->
        c.createStatement().use { s ->
            return action(s)
        }
    }
}

internal fun prepareDatabase() {
    val createSql =
        """create table if not exists Product (
        |    Id integer primary key autoincrement not null,
        |    Name text not null,
        |    Price int not null
        |)""".trimMargin()

    val clearSql = "delete from Product where true"

    databaseAction { statement -> statement.executeUpdate(createSql) }
    databaseAction { statement -> statement.executeUpdate(clearSql) }
}

internal fun prepareDatabase(fillSql: String) {
    prepareDatabase()
    databaseAction { statement -> statement.executeUpdate(fillSql) }
}

internal data class ServletElements(
    val servlet: HttpServlet,
    val pathSpec: String
)

internal fun testTemplate(
    servletElements: List<ServletElements>,
    requests: List<String>,
    expectedResponses: List<String>
) {
    val server = Server(8081)
    val context = ServletContextHandler(ServletContextHandler.SESSIONS)
    context.contextPath = "/"
    server.handler = context

    servletElements.forEach { (servlet, pathSpec) ->
        context.addServlet(ServletHolder(servlet), pathSpec)
    }

    server.start()

    val actualResponses = mutableListOf<String>()
    try {
        for (request in requests) {
            BufferedReader(InputStreamReader(URL(request).openStream())).use {
                actualResponses += it.readText()
            }
        }
    } finally {
        server.stop()
    }

    assertEquals(expectedResponses, actualResponses)
}

fun checkDatabaseConsistency(expectedValues: List<Pair<String, Int>>) {
    val sql = "select Name, Price from Product"
    databaseAction { statement ->
        val resultSet = statement.executeQuery(sql)

        val actualValues = mutableListOf<Pair<String, Int>>()
        while (resultSet.next()) {
            val name = resultSet.getString("Name")
            val price = resultSet.getInt("Price")

            actualValues += Pair(name, price)
        }

        assertTrue(expectedValues.containsAll(actualValues))
        assertTrue(actualValues.containsAll(expectedValues))
    }
}
