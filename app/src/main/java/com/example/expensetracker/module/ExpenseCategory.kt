package com.example.expensetracker.module

enum class ExpenseCategory(
    val title: String,
    val description: String
) {
    Housing(
        "Housing",
        "Rent/Mortgage, taxes, insurance, repairs"
    ),
    Utilities(
        "Utilities",
        "Electricity, water, gas, mobile, internet"
    ),
    Transportation(
        "Transportation",
        "Fuel, EMI, public transport, parking, maintenance"
    ),
    Food(
        "Food",
        "Groceries + Dining Out"
    ),
    Savings(
        "Savings",
        "Emergency fund, investments, long-term goals"
    ),
    Entertainment(
        "Entertainment",
        "Movies, subscriptions, concerts, hobbies"
    ),
    Shopping(
        "Shopping",
        "Clothes, electronics, home items"
    );
}
