package com.todolist

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class TodoApplication

fun main(args: Array<String>) {
    runApplication<TodoApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}
