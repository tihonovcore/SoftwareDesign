package com.todolist.dao

import com.todolist.model.Case
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

class CaseInFileDao : CaseDao {
    private val path = Paths.get("/home/tihonovcore/IdeaProjects/SoftwareDesign/todo/src/main/resources/database")

    override val cases by lazy {
        Files.readAllLines(path)
            .filter { it.isNotEmpty() }
            .mapIndexed { id, line ->
                val (descr, done) = line.split(",")
                Case(id, descr).apply { this.done = java.lang.Boolean.parseBoolean(done) }
            }
            .toMutableList()
    }

    override fun done(id: Int) {
        cases.find { it.id == id }!!.done = true

        Files.delete(path)
        Files.createFile(path)
        cases.forEach {
            Files.writeString(path, "\n${it.description},${it.done}", StandardOpenOption.APPEND)
        }
    }

    override fun addCase(case: Case) {
        Files.writeString(path, "\n${case.description},${case.done}", StandardOpenOption.APPEND)
    }
}
