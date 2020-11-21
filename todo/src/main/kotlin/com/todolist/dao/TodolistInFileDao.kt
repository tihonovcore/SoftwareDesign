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

    override fun addList() {
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

    private fun flush() {
        Files.delete(path)
        Files.createFile(path)
        lists.forEach {
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
                val cases = line
                    .split(" ### ")
                    .filter { it.isNotEmpty() }
                    .mapIndexed { id, case ->
                        val (descr, done) = case.split(",")
                        Case(id, descr).apply { this.done = java.lang.Boolean.parseBoolean(done) }
                    }
                    .toMutableList()

                TodoList(cases)
            }
            .toMutableList()
    }
}
