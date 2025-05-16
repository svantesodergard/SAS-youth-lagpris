package com.sas.youthlagpriskalander.service

import com.sas.youthlagpriskalander.model.Price
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class priceService(@Autowired private val webClient: WebClient) {

    public fun fetchPrice(from : String, to : String, monthIndex : String, ): Mono<List<Price>?> {
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
                        from = from,
                        to = to,
                        date = date,
                        lowestPrice = info.totalPrice,
                    )
                }

                val inboundPrices = response.inbound.map { (date, info) ->
                    Price(
                        from = to,
                        to = from,
                        date = date,
                        lowestPrice = info.totalPrice,
                    )
                }

                outboundPrices + inboundPrices
            }
    }

}