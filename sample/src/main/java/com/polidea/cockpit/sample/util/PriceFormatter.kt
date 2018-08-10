package com.polidea.cockpit.sample.util

import java.text.NumberFormat
import java.util.*


class PriceFormatter {
    fun formatPrice(price: Double): String {
        val numberFormat = NumberFormat.getCurrencyInstance(Locale.US)
        return numberFormat.format(price)
    }
}