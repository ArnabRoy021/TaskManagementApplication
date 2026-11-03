package com.example.taskmanagementapplication.ui

import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.Locale

fun isValidDate(date: String): Boolean {
    val trimmedDate = date.trim()

    // Check the required YYYY-MM-DD format first.
    if (!Regex("""\d{4}-\d{2}-\d{2}""").matches(trimmedDate)) {
        return false
    }

    // Year 0000 is not accepted.
    val year = trimmedDate.substring(0, 4).toInt()
    if (year < 1) {
        return false
    }

    // Strict parsing rejects impossible calendar dates.
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    formatter.isLenient = false

    val position = ParsePosition(0)
    val parsedDate = formatter.parse(trimmedDate, position)

    // Ensure the entire input was successfully parsed.
    return parsedDate != null && position.index == trimmedDate.length
}
