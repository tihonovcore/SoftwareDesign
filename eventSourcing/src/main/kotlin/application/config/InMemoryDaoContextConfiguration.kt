package application.config

import application.dao.InMemoryEventDao
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class InMemoryDaoContextConfiguration {
    @Bean
    fun eventDao() = InMemoryEventDao()
}
