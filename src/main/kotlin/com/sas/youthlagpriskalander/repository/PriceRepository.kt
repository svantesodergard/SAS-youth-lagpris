package com.sas.youthlagpriskalander.repository

import com.sas.youthlagpriskalander.model.Price
import org.springframework.data.jpa.repository.JpaRepository

interface PriceRepository : JpaRepository<Price, Long> {
}