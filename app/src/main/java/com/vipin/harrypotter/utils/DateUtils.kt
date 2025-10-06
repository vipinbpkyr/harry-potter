package com.vipin.harrypotter.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatDate(dateString: String?): String {
    if (dateString.isNullOrBlank()) {
        return "N/A"
    }
    return try {
        val inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH)
        LocalDate.parse(dateString, inputFormatter).format(outputFormatter)
    } catch (e: Exception) {
        "N/A"
    }
}
