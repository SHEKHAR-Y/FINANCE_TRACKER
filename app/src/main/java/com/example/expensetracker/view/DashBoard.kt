package com.example.expensetracker.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.expensetracker.R
import com.example.expensetracker.data.offline.table2
import com.example.expensetracker.module.ResultState
import com.example.expensetracker.utlis.formatIndianCurrency
import com.example.expensetracker.utlis.fromLocalDateTime
import com.example.expensetracker.utlis.toLocalDateTime
import com.example.expensetracker.view.dashboard.WeeklySpendingBarGraph
import com.example.expensetracker.viewModel.DataStoreViewModel
import com.example.expensetracker.viewModel.RoomViewModel
import kotlinx.coroutines.CoroutineScope
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import kotlin.coroutines.CoroutineContext


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashBoard(
    roomViewModel: RoomViewModel = hiltViewModel(),
    dataStoreViewModel: DataStoreViewModel = hiltViewModel(),
    onClick: () -> Unit,
    onCLickUpdateBalance: () -> Unit,
    onTransactionClick: (Long) -> Unit
) {
    LaunchedEffect(Unit) {
        roomViewModel.getTransactions()
        roomViewModel.getBudgetData()
        roomViewModel.getCurrentWeekSpendingData()
    }

    val weeklySpending by roomViewModel.weeklySpendingData.collectAsState()
    println(weeklySpending)

    val balance by dataStoreViewModel._balance.collectAsState()
    val transactions by roomViewModel.transactions.collectAsState()

    // budget data from room
    val budgetDataState = roomViewModel.budgetData.collectAsState().value

    val budgetDate = when(budgetDataState){
        is ResultState.Success -> {
            budgetDataState.data
        }
        else -> null
    }

    // calculate last updated time and date
    val lastUpdatedTime = toLocalDateTime(budgetDate?.LastUpdated)
    val formattedDateAndTime = lastUpdatedTime?.format(
        DateTimeFormatter.ofPattern("dd MMMM yyyy")
    )
    val context = LocalContext.current

    // calculate days left in next month
    val currentDateTime = LocalDateTime.now()
    val yearMonth = YearMonth.from(currentDateTime)

    val totalDaysInMonth = yearMonth.lengthOfMonth()
    val daysLeft = totalDaysInMonth - currentDateTime.dayOfMonth

    // check for the current month
    val currentMonth = currentDateTime.month.name
    val lastResetMonth = budgetDate?.LastResetMonth

    if(lastResetMonth != null){
        if(currentMonth != lastResetMonth){
            roomViewModel.resetBudget(fromLocalDateTime(currentDateTime) ?: System.currentTimeMillis(), currentMonth)
        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
            )
    ) {
        LazyColumn(modifier = Modifier) {
            item {
                // card on top of dashboard
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier
                        .padding(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(15.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        // title for Monthly budget
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                ) {
                                    append("MONTHLY BUDGET")
                                }
                                withStyle(
                                    style = SpanStyle(
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                ) {
                                    append(" • ₹ ${formatIndianCurrency(budgetDate?.Budget.toString())}.00")

                                }
                            }
                        )

                        // icon and budget left
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(15.dp),
                            verticalAlignment = Alignment.CenterVertically

                        ) {
                            Icon(
                                painter = painterResource(
                                    R.drawable.rupee
                                ),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(44.dp)
                            )
                            Column {
                                Text(
                                    "${formatIndianCurrency(budgetDate?.CurrentBalance.toString())}.00",
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Text(
                                    "BUDGET LEFT ON: $formattedDateAndTime",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        // text show how many days and budget left
                        Text(
                            "${formatIndianCurrency(budgetDate?.CurrentBalance.toString())}.00 RS, $daysLeft DAYS LEFT",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                        // progress bar
                        BudgetLeftIndicator(budgetDate)
                        // button to navigate to add expense screen
                        Button(
                            onClick = {
                                onClick()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.onBackground
                            ),
                            shape = RoundedCornerShape(25),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                "Add Expense",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }

            item {
                ElevatedCard (
                    modifier = Modifier
                        .padding(vertical = 10.dp, horizontal = 20.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, start = 20.dp, end = 20.dp),
                    ){

                        WeeklySpendingBarGraph()
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 5.dp),
                    ) {
                        listOf("SUN","MON","TUE","WED","THU","FRI","SAT").forEach {
                            Text(
                                text = it,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))

                }
            }

            item {
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier
                        .padding(vertical = 10.dp, horizontal = 20.dp),

                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {

                        // THIS IS YOUR TITLE INSIDE THE CARD
                        Text(
                            text = "RECENT TRANSACTIONS",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.secondary
                            ),
                            modifier = Modifier.padding(bottom = 15.dp)
                        )

                        // NOW LIST YOUR ITEMS
                        transactions.forEach { item ->
                            ListItem(
                                price = item.Amount,
                                note = item.Note,
                                category = item.type,
                                dateTime = item.dateTime,
                                onClick = {
                                    onTransactionClick(item.id)
                                }
                            )
                        }
                        if(transactions.isEmpty()){
                            Text(
                                "No Transaction Record",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 30.dp),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListItem(
    price: Int,
    note: String,
    category: String,
    dateTime: Long?,
    onClick: () -> Unit
) {
    val formattedTime = dateTime?.let {
        toLocalDateTime(it)
            ?.format(
                DateTimeFormatter.ofPattern("dd MMMM yyyy")
            )
            ?.uppercase()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp)
            .clickable(
                enabled = true,
                onClick = {
                    onClick()
                }
            ),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Icon(
            painter = painterResource(R.drawable.rupee),
            null,
            modifier = Modifier
                .size(36.dp)
        )
        // column for expense category , date and time
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                category.uppercase(),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontSize = 14.sp
                )
            )
            Text(
                "$formattedTime",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Text(
            "₹ ${formatIndianCurrency(price.toString())}.00",
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
fun BudgetLeftIndicator(
    budgetDate: table2?
) {
    val budget = budgetDate?.Budget?.toFloat() ?: 1f
    val currentBalance = budgetDate?.CurrentBalance?.toFloat() ?: 0f

    // Ensure progress stays between 0f and 1f
    val progress = (currentBalance / budget).coerceIn(0f, 1f)

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                formatIndianCurrency(budgetDate?.Budget.toString()) + " RS",
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                formatIndianCurrency(budgetDate?.CurrentBalance.toString()) + " RS LEFT",
                style = MaterialTheme.typography.labelSmall
            )
        }
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onBackground,
            trackColor = MaterialTheme.colorScheme.secondary,
            gapSize = 2.dp
        )
    }
}



// function to reset the budget monthly
@Composable
fun ResetBudget(){

}