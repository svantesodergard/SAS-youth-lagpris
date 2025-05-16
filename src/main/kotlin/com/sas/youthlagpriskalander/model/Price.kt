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

@Entity
@Table(
    name = "price",
    uniqueConstraints = [UniqueConstraint(columnNames = ["departure", "destination", "date"])]
)
data class Price (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    val departure : String,
    val destination : String,
    val date : LocalDate,
    val lowestPrice : Int,
)