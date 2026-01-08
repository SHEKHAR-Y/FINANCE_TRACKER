package com.example.expensetracker.data.offline

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expensetracker.module.DayExpense

@Dao
interface Dao {

    // get all the expenses
    @Query("SELECT * FROM Expenses ORDER BY dateTime DESC")
    suspend fun getAllTransactions(): List<table1>

    // get single transaction with id
    @Query("SELECT * FROM Expenses WHERE id = :id ")
    suspend fun getSingleTransactionDetail(id: Long): table1

    // insert transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(data: table1): Long

    // add Budget for first time
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBudget(budget: table2): Long

    // fetch the budget(budget, current balance, last updated, last reset month)
    @Query("SELECT * FROM balance WHERE id = 1")
    suspend fun getBalance(): table2?

    // update budget when transaction occur
    @Query("UPDATE balance SET CurrentBalance = CurrentBalance - :amount, LastUpdated = :lastUpdate")
    suspend fun updateBalance(amount: Int, lastUpdate: Long)

    // update the budget when new month start
    @Query("""
        UPDATE Balance
        SET 
            CurrentBalance = Budget,
            LastUpdated = :lastUpdated,
            LastResetMonth = :lastResetMonth
        WHERE id = 1
    """)
    suspend fun resetBalanceToBudget(
        lastUpdated: Long,
        lastResetMonth: String
    )

    // get the data of current week
    @Query(
        """
    SELECT strftime('%w', dateTime / 1000, 'unixepoch', 'localtime') AS day,
           SUM(amount) as total
    FROM Expenses
    WHERE dateTime BETWEEN :start AND :end
    GROUP BY day
"""
    )
    suspend fun weeklyExpenseByDay(
        start: Long,
        end: Long
    ): List<DayExpense>
}