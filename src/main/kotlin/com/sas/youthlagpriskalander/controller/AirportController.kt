package com.sas.youthlagpriskalander.controller

import com.sas.youthlagpriskalander.repository.PriceRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AirportController(private val priceRepository: PriceRepository) {

    @GetMapping("/getAllAirports")
    fun getAllAirports() = priceRepository.findDistinctDeparture()
}