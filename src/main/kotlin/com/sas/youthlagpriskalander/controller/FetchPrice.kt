package com.sas.youthlagpriskalander.controller

import com.sas.youthlagpriskalander.model.Price
import com.sas.youthlagpriskalander.service.PriceService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class FetchPrice(private val priceService: PriceService) {
    @GetMapping("/fetchPrice")
    fun fetchPrice(@RequestParam from : String, @RequestParam to : String, @RequestParam monthIndex : String): String {
        priceService.updatePrices()
        return "Success!"
    }
}