package com.todolist.dao

import com.todolist.model.Case
import com.todolist.model.TodoList
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

class TodolistInFileDao : TodolistDao {
    private val path = Paths.get("/home/tihonovcore/IdeaProjects/SoftwareDesign/todo/src/main/resources/database")

    override val lists = readFile()

    override var currentList = lists.first()

    override fun addList(name: String) {
        TODO("Not yet implemented")
    }

    override fun deleteList(id: Int) {
        TODO("Not yet implemented")
    }

    override fun chooseList(id: Int) {
        TODO("Not yet implemented")
    }

    override fun done(id: Int) {
        currentList.cases.find { it.id == id }!!.done = true

        flush()
    }

    override fun addCase(case: Case) {
        currentList.cases += case

        flush()
    }

    override fun freeId(): Int {
        val maxId = currentList.cases.map { it.id }.maxOrNull() ?: -1
        return maxId + 1
    }

    private fun flush() {
        Files.delete(path)
        Files.createFile(path)
        lists.forEach {
            Files.writeString(path, "${it.id}@${it.name}@", StandardOpenOption.APPEND)
            it.cases.forEach {
                Files.writeString(path, "${it.description},${it.done} ### ", StandardOpenOption.APPEND)
            }
            Files.writeString(path, "\n", StandardOpenOption.APPEND)
        }
    }

    private fun readFile(): MutableList<TodoList> {
        return Files.readAllLines(path)
            .filter { it.isNotEmpty() }
            .map { line ->
                val (listId, name, suffix) = line.split("@")
                val cases = suffix
                    .split(" ### ")
                    .filter { it.isNotEmpty() }
                    .mapIndexed { id, case ->
                        val (descr, done) = case.split(",")
                        Case(id, descr).apply { this.done = java.lang.Boolean.parseBoolean(done) }
                    }
                    .toMutableList()

                TodoList(cases, name, Integer.parseInt(listId))
            }
            .toMutableList()
    }
}
