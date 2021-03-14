package tech.shali.boliboliapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling


@SpringBootApplication
@ConfigurationPropertiesScan
@EnableScheduling
class BoliboliApiApplication

fun main(args: Array<String>) {
    runApplication<BoliboliApiApplication>(*args)
}
