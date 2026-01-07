package com.example.expensetracker.viewModel

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.offline.table1
import com.example.expensetracker.data.offline.table2
import com.example.expensetracker.data.repo.RoomRepo
import com.example.expensetracker.module.DayExpense
import com.example.expensetracker.module.ResultState
import com.example.expensetracker.utlis.CurrentWeekTimeStamps
import com.example.expensetracker.utlis.mapDayExpenseToWeeklyList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val roomRepo: RoomRepo
): ViewModel() {

    // get transactions
    private var _transactions = MutableStateFlow<List<table1>>(emptyList())
    val transactions: StateFlow<List<table1>> = _transactions.asStateFlow()
    fun getTransactions(){
        viewModelScope.launch {
            val result = roomRepo.getAllTransactions()
            println(result)
            _transactions.value = result
        }
    }

    // get specific transaction detail
    private var _singleTransaction = MutableStateFlow<table1?>(null)
    val singleTransaction = _singleTransaction.asStateFlow()

    fun getDetailsOfSpecificTransaction(id: Long){
        viewModelScope.launch {
            val result = roomRepo.getSingleTransactionDetails(id)
            _singleTransaction.value = result
        }
    }

    // add transactions
    fun addTransaction(context: Context, data: table1){
        viewModelScope.launch {
            val result = roomRepo.insertTransaction(data)
            if(result > 0){
                println("TRANSACTION INSERTED IN RECORD")
                Toast.makeText(context,"Expense Added", Toast.LENGTH_LONG).show()
                updateBudgetDataAfterTransaction(data.Amount, data.dateTime ?: 0L)
            } else {
                Toast.makeText(context,"Expense Not Added", Toast.LENGTH_LONG).show()
            }
        }
    }

    // add budget data for first time
    private var _insertFirstBudgetData = MutableStateFlow<Boolean>(false)
    val insertFirstBudgetData = _insertFirstBudgetData.asStateFlow()
    fun addBudgetData(budget: table2){
        viewModelScope.launch {
            val result = roomRepo.addBudget(budget)
            _insertFirstBudgetData.value = result
        }
    }

    // get budget data
    private var _budgetData = MutableStateFlow<ResultState<table2?>>(ResultState.Loading)
    val budgetData = _budgetData.asStateFlow()
    fun getBudgetData(){
        viewModelScope.launch {
            roomRepo.getBudgetData().collect { result ->
               _budgetData.value = result
            }
        }
    }

    // update budget data
    fun updateBudgetDataAfterTransaction(amount: Int, lastUpdated: Long){
        viewModelScope.launch {
            roomRepo.updateBudgetData(amount, lastUpdated)
        }
    }

    // get current week spending data
    private var _weeklySpendingData = MutableStateFlow<List<Int>>(emptyList())
    val weeklySpendingData = _weeklySpendingData.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentWeekSpendingData(){
        val (startTime, endTime) = CurrentWeekTimeStamps()
        viewModelScope.launch {
            val result = roomRepo.getCurrentWeekData(startTime, endTime)
            _weeklySpendingData.value = mapDayExpenseToWeeklyList(result)
        }
    }
}