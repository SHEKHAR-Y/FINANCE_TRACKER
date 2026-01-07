package com.example.expensetracker.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowCircleLeft
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.expensetracker.R
import com.example.expensetracker.viewModel.RoomViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionList(
    onBack: () -> Unit,
    onTransactionItemClick: (Long) -> Unit,
    roomViewModel: RoomViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        roomViewModel.getTransactions()
    }

    // all transactions
    val transactions by roomViewModel.transactions.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Transaction List",
                        style = MaterialTheme.typography.titleSmall
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onBack()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.back_arrow), null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                modifier = Modifier
                    .shadow(2.dp)
            )
        }
    ) { paddingValues ->
        ElevatedCard(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding() + 20.dp,
                start = 20.dp,
                end = 20.dp,
                bottom = 20.dp
            )
        ) {
            LazyColumn(
                modifier = Modifier.padding(10.dp)
            )
            {
                item {
                    if (transactions.isEmpty()) {
                        Text(
                            "No Transaction Yet",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            style = TextStyle(
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
                items(transactions) { item ->
                    key(item.id) {
                        ListItem(
                            price = item.Amount,
                            note = item.Note,
                            category = item.type,
                            dateTime = item.dateTime,
                            onClick = {
                                onTransactionItemClick(item.id)
                            }
                        )
                    }
                }
            }
        }

    }
}