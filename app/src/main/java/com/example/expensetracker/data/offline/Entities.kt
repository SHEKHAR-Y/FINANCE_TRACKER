package com.example.expensetracker.data.offline

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Expenses")
data class table1(
    @PrimaryKey(autoGenerate = true) val id : Long = 0L,
    val Amount: Int,
    val Note: String,
    val type: String,
    val dateTime: Long?
)

@Entity(tableName = "Balance")
data class table2(
    @PrimaryKey val id: Int = 1,
    val Budget: Int,
    val CurrentBalance: Int,
    val LastUpdated: Long?,
    val LastResetMonth: String?,
    val UserName: String
)
