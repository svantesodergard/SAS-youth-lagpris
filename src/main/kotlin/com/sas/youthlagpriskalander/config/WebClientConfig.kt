package com.sas.youthlagpriskalander.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebClientConfig {

    @Bean
    fun webClient(): WebClient {
        return WebClient.create("https://www.sas.se/v2/cms-www-api/flights/calendar/prices/")
    }

}
