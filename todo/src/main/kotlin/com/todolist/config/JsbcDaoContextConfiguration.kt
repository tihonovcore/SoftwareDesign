package com.todolist.config

import com.todolist.dao.TodolistJdbcDao
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource

@Configuration
open class JdbcDaoContextConfiguration {
    @Bean
    open fun productJdbcDao() = TodolistJdbcDao()
}
