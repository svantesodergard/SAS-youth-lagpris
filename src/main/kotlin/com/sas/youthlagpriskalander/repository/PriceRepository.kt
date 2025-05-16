package com.sas.youthlagpriskalander.repository

import com.sas.youthlagpriskalander.model.Price
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate

interface PriceRepository : JpaRepository<Price, Long> {
    fun findByDateBetween(startDate: LocalDate, endDate: LocalDate): List<Price>
    fun findByDeparture(departure: String): List<Price>
}