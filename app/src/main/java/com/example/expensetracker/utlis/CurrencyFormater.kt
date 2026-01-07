package com.example.expensetracker.utlis

import java.text.NumberFormat
import java.util.Locale

fun formatIndianCurrency(amount: String): String {
    val value = amount.toLongOrNull() ?: return amount
    val formatter = NumberFormat.getInstance(Locale("en", "IN"))
    return formatter.format(value)
}
