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
class PriceService(
    @Autowired private val webClient: WebClient,
    private val priceRepository: PriceRepository,
    @Value("\${price.monthCount}") var monthCount : Int
) {


    @Value("\${price.origins}")
    lateinit var departures : List<String>

    @Value("\${price.destinations}")
    lateinit var destinations  : List<String>




    val dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd")

    @Scheduled(cron = "0 0 0 * * *")
    fun updatePrices() {
        val currentMonth = LocalDate.now().monthValue
        val currentYear = LocalDate.now().year

        val monthIndices = (currentMonth until currentMonth + monthCount).map { month ->
            val year = if (month > 12) currentYear + 1 else currentYear
            "$year${String.format("%02d", month % 12)}"
        }

        val allLowestPrices = mutableListOf<Price>()

        departures.forEach { departure -> destinations.forEach { destination ->
            monthIndices.forEach { monthIndex ->
                val prices = this.fetchPrice(departure, destination, monthIndex).block()
                if (!prices.isNullOrEmpty()) {

                    val lowestPricesPerMonth = prices.groupBy { Triple(it.date.month, it.departure, it.destination) }
                        .map { it.value.minBy { price -> price.lowestPrice } }

                    allLowestPrices.addAll(lowestPricesPerMonth)
                }
            }
        } }

        priceRepository.deleteAll()
        priceRepository.saveAll(allLowestPrices)
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