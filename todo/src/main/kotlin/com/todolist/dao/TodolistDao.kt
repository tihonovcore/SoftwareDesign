package com.todolist.dao

import com.todolist.model.Case
import com.todolist.model.TodoList

interface TodolistDao {
    val lists: MutableList<TodoList>
    var currentList: TodoList

    fun addList()
    fun deleteList(id: Int)
    fun chooseList(id: Int)

    fun done(id: Int)
    fun addCase(case: Case)
}
