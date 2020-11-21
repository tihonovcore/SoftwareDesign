package com.todolist.config

import com.todolist.dao.CaseInFileDao
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class FileDaoContextConfiguration {
    @Bean
    open fun caseDao() = CaseInFileDao()
}
