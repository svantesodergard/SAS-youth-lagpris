package com.sas.youthlagpriskalander.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class Price (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    val departure : String,
    val destination : String,
    val date : String,
    val lowestPrice : Int,
)