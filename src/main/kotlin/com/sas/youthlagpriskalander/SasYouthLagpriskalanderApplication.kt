package com.sas.youthlagpriskalander

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class SasYouthLagpriskalanderApplication

fun main(args: Array<String>) {
    runApplication<SasYouthLagpriskalanderApplication>(*args)
}
