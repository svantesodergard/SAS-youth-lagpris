package com.sas.youthlagpriskalander.controller

import com.sas.youthlagpriskalander.model.Price
import com.sas.youthlagpriskalander.service.priceService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class FetchPrice(private val priceService: priceService) {
    @GetMapping("/fetchPrice")
    fun fetchPrice(@RequestParam from : String, @RequestParam to : String, @RequestParam monthIndex : String): Mono<List<Price>?> {
        return priceService.fetchPrice(from, to, monthIndex)
    }
}