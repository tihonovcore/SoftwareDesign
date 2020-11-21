package com.todolist.sql

import java.sql.DriverManager
import java.sql.Statement

class SqlFacade(private val testDatabaseUrl: String) {
    fun databaseAction(action: (Statement) -> Unit) {
        DriverManager.getConnection(testDatabaseUrl).use { c ->
            c.createStatement().use(action)
        }
    }
}
