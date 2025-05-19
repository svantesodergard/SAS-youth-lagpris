package com.sas.youthlagpriskalander.repository

import com.sas.youthlagpriskalander.model.Price
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

interface PriceRepository : JpaRepository<Price, Long> {
    fun findByDateBetween(startDate: LocalDate, endDate: LocalDate): List<Price>

    @Query("SELECT DISTINCT p.departure FROM Price p")
    fun findDistinctDeparture(): List<String>
}