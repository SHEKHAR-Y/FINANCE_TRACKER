package com.example.expensetracker.data.repo

import com.example.expensetracker.data.offline.Dao
import com.example.expensetracker.data.offline.table1
import com.example.expensetracker.data.offline.table2
import com.example.expensetracker.module.DayExpense
import com.example.expensetracker.module.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RoomRepo @Inject constructor(
    private val dao: Dao
) {
    // get all the transactions
    suspend fun getAllTransactions(): List<table1>{
        return dao.getAllTransactions()
    }

    // get single transaction from id
    suspend fun getSingleTransactionDetails(id: Long): table1?{
        return try {
            dao.getSingleTransactionDetail(id)
        } catch (e: Exception){
            null
        }
    }

    // insert transactions
    suspend fun insertTransaction(data: table1): Long{
        return dao.insertTransactions(data)
    }

    // add budget first time
    suspend fun addBudget(budget: table2): Boolean{
        return try {
            dao.addBudget(budget)
            true
        } catch (e: Exception){
            println(e.message)
            false
        }
    }

    // get the budget data(username, budget, current balance, last updated, last reset month)
    fun getBudgetData() = flow {
        try {
            emit(ResultState.Loading)
            val data = dao.getBalance()
            emit(ResultState.Success(data))
        } catch (e: Exception){
            println(e.message)
            emit(ResultState.Failure(e.message ?: "error occurred"))
        }
    }

    // update budget after transaction
    suspend fun updateBudgetData(amount: Int, lastUpdated: Long){
        dao.updateBalance(amount, lastUpdated)
    }

     // get the data of current week
     suspend fun getCurrentWeekData(start: Long, end: Long): List<DayExpense>?{
         return try {
             dao.weeklyExpenseByDay(start, end)
         } catch (e: Exception){
             println(e.message)
             null
         }
     }
}