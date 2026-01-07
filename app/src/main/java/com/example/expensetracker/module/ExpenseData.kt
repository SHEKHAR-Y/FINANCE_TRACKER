package com.example.expensetracker.module

data class DayExpense(
    val day: Int,   // 0 = Sunday ... 6 = Saturday
    val total: Int
)