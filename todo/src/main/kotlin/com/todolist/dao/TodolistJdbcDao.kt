package com.todolist.dao

import com.todolist.model.Case
import com.todolist.model.TodoList
import com.todolist.sql.SqlFacade

private const val databaseUrl = "jdbc:sqlite:todolist.db"

class TodolistJdbcDao : TodolistDao {
    override val lists = readLists()
    override var currentList = lists.first()

    override fun addList(name: String) {
        val maxListId = lists.map { it.id }.maxOrNull() ?: -1
        val id = maxListId + 1

        lists += TodoList(id, name, mutableListOf())

        SqlFacade(databaseUrl).databaseAction { statement ->
            statement.executeUpdate("insert into Todolist (id, name) values ($id, '$name');")
        }
    }

    override fun deleteList(id: Int) {
        lists.removeIf { it.id == id }

        SqlFacade(databaseUrl).databaseAction { statement ->
            statement.executeUpdate("delete from Cases where listid = $id")
            statement.executeUpdate("delete from Todolist where id = $id")
        }
    }

    override fun chooseList(id: Int) {
        currentList = lists.find { it.id == id }!!
    }

    override fun done(id: Int) {
        currentList.cases.find { it.id == id }!!.done = true

        SqlFacade(databaseUrl).databaseAction { statement ->
            statement.executeUpdate("delete from Cases where caseid = $id and listid = ${currentList.id}")
        }
    }

    override fun addCase(case: Case) {
        currentList.cases += case

        SqlFacade(databaseUrl).databaseAction { statement ->
            statement.executeUpdate(
                """insert into Cases (caseid, listid, description)
                  |  values (${case.id}, ${currentList.id}, '${case.description}');
                """.trimMargin())
        }
    }

    override fun freeCaseId(): Int {
        val maxId = currentList.cases.map { it.id }.maxOrNull() ?: -1
        return maxId + 1
    }

    private fun readLists(): MutableList<TodoList> {
        data class ListBox(val id: Int, val name: String)

        val lists = mutableListOf<ListBox>()
        SqlFacade(databaseUrl).databaseAction { statement ->
            val result = statement.executeQuery("select * from Todolist;")

            while (result.next()) {
                val id = result.getInt("id")
                val name = result.getString("name")

                lists += ListBox(id, name)
            }
        }

        return lists.map { list ->
            val cases = mutableListOf<Case>()
            SqlFacade(databaseUrl).databaseAction { statement ->
                val result = statement.executeQuery("select * from Cases where listid = ${list.id}")

                while (result.next()) {
                    val id = result.getInt("caseid")
                    val description = result.getString("description")

                    cases += Case(id, description)
                }
            }

            TodoList(list.id, list.name, cases)
        }.toMutableList()
    }
}
