package com.example.expensetracker.utlis

import kotlinx.serialization.Serializable

@Serializable
object AppEntry

@Serializable
object startUp

@Serializable
object dashboard

@Serializable
object analytics

@Serializable
object list

@Serializable
object addTransaction

@Serializable
data class expenseDetails(
    val id: Long
)

@Serializable
object updateBalance

