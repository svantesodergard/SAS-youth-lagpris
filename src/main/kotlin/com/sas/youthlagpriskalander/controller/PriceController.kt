package com.sas.youthlagpriskalander.controller

import com.sas.youthlagpriskalander.model.Price
import com.sas.youthlagpriskalander.model.PriceResponseDto
import com.sas.youthlagpriskalander.repository.PriceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

@RestController
class PriceController (private val priceRepository: PriceRepository) {

    @GetMapping("/lowestPrices")
    fun getLowestPricesForMonth(
        @RequestParam month : Int,
        @RequestParam year : Int,
        @RequestParam departure : String
    ) : List<PriceResponseDto> {

        val startDate = LocalDate.of(year, month, 1)
        val endDate = LocalDate.of(year, month, startDate.lengthOfMonth())
        return priceRepository.findByDateBetween(startDate, endDate)
            .filter { it.departure == departure }.groupBy { it.destination }
                .map { it.value.minBy { price -> price.lowestPrice } }.map { it.toDto() }

            //.map { it.key to it.value.minBy { p -> p.lowestPrice } }.toMap()
    }

    @GetMapping("/lowestPricesByMonth")
    fun getLowestPricesByMonth(
        @RequestParam startMonth : Int,
        @RequestParam startYear : Int,
        @RequestParam monthCount : Int,
        @RequestParam departure : String
    ) : Map<String, List<PriceResponseDto>> {

        if (monthCount > 12) {
            throw IllegalArgumentException("Prisuppgifter tillgängliga max tolv (12) månader framåt")
        }

        return (startMonth until startMonth + monthCount).associate { month ->
            val year = if (month <= 12) startYear else startYear + 1 //Öka år för spann överskridande årsskifte
            val monthString = Month.of(month).getDisplayName(TextStyle.FULL, Locale("sv"))
                .replaceFirstChar { it.uppercase() }
            monthString to this.getLowestPricesForMonth(month % 12, year, departure)
        }

    }

    @GetMapping("getAllMonths")
    fun getAllMonths() = priceRepository.findDistinctMonths()
        .map { Month.of(it).getDisplayName(TextStyle.FULL, Locale("sv"))
            .replaceFirstChar { it.uppercase() } }
}
