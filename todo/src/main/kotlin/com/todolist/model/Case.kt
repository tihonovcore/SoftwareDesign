package com.todolist.model

class Case(
    val id: Int,
    val description: String,
) {
    var done: Boolean = false

    override fun toString(): String {
        return "$description is $done"
    }
}
