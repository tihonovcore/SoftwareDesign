package com.todolist.dao

import com.todolist.model.Case

interface CaseDao {
    val cases: MutableList<Case>

    fun done(id: Int)
    fun addCase(case: Case)
}
