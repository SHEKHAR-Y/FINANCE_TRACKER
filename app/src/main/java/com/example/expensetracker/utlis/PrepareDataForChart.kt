package com.example.expensetracker.utlis

import com.example.expensetracker.module.DayExpense

fun mapDayExpenseToWeeklyList(
    data: List<DayExpense>?
): List<Int> {
    val result = IntArray(7) { 0 } // Sun → Sat

    data?.forEach { expense ->
        if (expense.day in 0..6) {
            result[expense.day] = expense.total
        }
    }

    return result.toList()
}
