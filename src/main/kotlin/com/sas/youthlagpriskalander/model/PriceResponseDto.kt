package com.sas.youthlagpriskalander.model

import java.time.LocalDate

data class PriceResponseDto(
    val id: Long?,
    val departure : String,
    val destination : String,
    val date : LocalDate,
    val lowestPrice : Int,

    val bookingLink : String
)
