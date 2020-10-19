package ru.akirakozov.sd.refactoring

import java.sql.DriverManager
import java.sql.Statement
import java.util.function.Consumer

class SqlFacade(private val testDatabaseUrl: String) {
    fun databaseAction(action: Consumer<Statement>) {
        DriverManager.getConnection(testDatabaseUrl).use { c ->
            c.createStatement().use { s ->
                action.accept(s)
            }
        }
    }
}
