package com.sas.youthlagpriskalander.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Entity
@Table(
    name = "price",
    uniqueConstraints = [UniqueConstraint(columnNames = ["departure", "destination", "date"])]
)
class Price (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    val departure : String,
    val destination : String,
    val date : LocalDate,
    val lowestPrice : Int,
) {
    fun toDto() : PriceResponseDto {
        val dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd")
        val dateString = this.date.format(dateFormat)
        return PriceResponseDto(id, departure, destination, date, lowestPrice,
        "https://www.sas.se/book/flights/?search=OW_${departure}-${destination}-${dateString}_a0c0i0y1&&view=upsell&bookingFlow=revenue&cepId=YOUNG&sortBy=rec"
    )}
}