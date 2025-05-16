package com.sas.youthlagpriskalander.service

import com.sas.youthlagpriskalander.model.Price
import com.sas.youthlagpriskalander.repository.PriceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class PriceService(@Autowired private val webClient: WebClient, private val priceRepository: PriceRepository) {


    @Value("\${price.origins}")
    lateinit var origins : List<String>

    @Value("\${price.destinations}")
    lateinit var destinations  : List<String>

    @Value("\${price.months}")
    lateinit var months : List<String>

    @Value("\${price.year}")
    lateinit var year : String

    val dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd")

    @Scheduled(cron = "0 0 0 * * *")
    fun updatePrices() {

        origins.forEach { from -> destinations.forEach { to ->
            months.forEach { month ->
                val monthIndex = "2025${month}"
                val prices = this.fetchPrice(from, to, monthIndex).block()

                if (!prices.isNullOrEmpty()) {
                    priceRepository.saveAll(prices)
                }
            }
        } }
    }

    fun fetchPrice(from : String, to : String, monthIndex : String, ): Mono<List<Price>?> {
        val url = ""
        return webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path(url)
                    .queryParam("market", "se-sv")
                    .queryParam("from", from)
                    .queryParam("to", to)
                    .queryParam("month", "${monthIndex},${monthIndex}")
                    .queryParam("flow", "revenue")
                    .queryParam("type", "youths")
                    .queryParam("cepId", "YOUNG")
                    .queryParam("product", "")
                    .build()
            }
            .retrieve()
            .bodyToMono(ApiResponse::class.java)
            .map { response ->
                val outboundPrices = response.outbound.map { (date, info) ->
                    Price(
                        departure = from,
                        destination = to,
                        date = LocalDate.parse(date, dateFormat),
                        lowestPrice = info.totalPrice,
                    )
                }

                val inboundPrices = response.inbound.map { (date, info) ->
                    Price(
                        departure = to,
                        destination = from,
                        date = LocalDate.parse(date, dateFormat),
                        lowestPrice = info.totalPrice,
                    )
                }

                outboundPrices + inboundPrices
            }
    }

}