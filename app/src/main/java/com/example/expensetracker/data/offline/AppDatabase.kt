package com.example.expensetracker.data.offline

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [table1::class, table2::class], version = 3)
abstract class AppDatabase: RoomDatabase() {
    abstract fun dao(): Dao
}