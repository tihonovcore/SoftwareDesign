package com.todolist.config

import com.todolist.dao.TodolistInFileDao
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class FileDaoContextConfiguration {
    @Bean
    open fun todolistDao() = TodolistInFileDao()
}
