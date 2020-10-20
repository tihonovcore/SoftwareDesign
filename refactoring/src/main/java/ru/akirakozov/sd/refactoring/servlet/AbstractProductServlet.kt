package ru.akirakozov.sd.refactoring.servlet

import ru.akirakozov.sd.refactoring.SqlFacade
import javax.servlet.http.HttpServlet

abstract class AbstractProductServlet : HttpServlet() {
    private val testDatabaseUrl = "jdbc:sqlite:test.db" //TODO: move to properties
    private val sqlFacade = SqlFacade(testDatabaseUrl)

    fun readAll(sql: String): List<Pair<String, Int>> {
        val result = mutableListOf<Pair<String, Int>>()

        sqlFacade.databaseAction { statement ->
            val resultSet = statement.executeQuery(sql)

            while (resultSet.next()) {
                val name = resultSet.getString("name")
                val price = resultSet.getInt("price")

                result += Pair(name, price)
            }
        }

        return result
    }

    fun readInt(sql: String): Int? {
        var result: Int? = null
        sqlFacade.databaseAction { statement ->
            val resultSet = statement.executeQuery(sql)
            if (resultSet.next()) {
                result = resultSet.getInt(1)
            }
        }
        return result
    }
}
