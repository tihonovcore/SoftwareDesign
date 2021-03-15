package application.config

import application.dao.EventDao
import application.dao.InMemoryEventDao
import application.dao.InMemoryReportDao
import application.dao.ReportDao
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class InMemoryDaoContextConfiguration {
    @Bean
    fun eventDao(): EventDao = InMemoryEventDao()

    @Bean
    fun reportDao(): ReportDao = InMemoryReportDao()
}
