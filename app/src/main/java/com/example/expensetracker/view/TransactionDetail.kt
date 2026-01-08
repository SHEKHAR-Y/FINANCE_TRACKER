package com.example.expensetracker.view

import android.os.Build
import android.widget.Space
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AccountBalanceWallet
import androidx.compose.material.icons.twotone.ArrowCircleLeft
import androidx.compose.material.icons.twotone.Money
import androidx.compose.material.icons.twotone.Wallet
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.expensetracker.R
import com.example.expensetracker.utlis.formatIndianCurrency
import com.example.expensetracker.utlis.toLocalDateTime
import com.example.expensetracker.viewModel.RoomViewModel
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDetail(
    id: Long,
    roomViewModel: RoomViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        roomViewModel.getDetailsOfSpecificTransaction(id)
    }

    val transactionDetail by roomViewModel.singleTransaction.collectAsState()

    val dateTime = transactionDetail?.dateTime.let {
        toLocalDateTime(it)
    }
    val formattedDate = dateTime?.format(
        DateTimeFormatter.ofPattern("dd MMMM yyyy")
    )?.uppercase()

    // ui
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Transaction Details",
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
                            painter = painterResource(R.drawable.left_arrow),
                            null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                modifier = Modifier
                    .shadow(2.dp)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = paddingValues.calculateTopPadding(), start = 10.dp, end = 10.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    "AMOUNT",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                )
                Spacer(Modifier.height(10.dp))

                Text(
                    "₹${formatIndianCurrency(transactionDetail?.Amount.toString())}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp
                    )
                )
                Spacer(Modifier.height(10.dp))

                Text(
                    formattedDate ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                )
            }

            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(3.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp) // ← Internal card padding
                ) {
                    Text(
                        "TRANSACTION SUMMARY",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                    )
                    HorizontalDivider(Modifier.padding(vertical = 20.dp))

                    Row {
                        Text(
                            "EXPENSE TYPE",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(Modifier.weight(1f))
                        transactionDetail?.type?.let {
                            Text(
                                it,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
//                    HorizontalDivider(Modifier.padding(vertical = 20.dp))

                    Row {
                        Text(
                            "TRANSACTION DATE",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            "$formattedDate",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
//                    HorizontalDivider(Modifier.padding(vertical = 20.dp))

                    Row {
                        Text(
                            "ADDITIONAL NOTES",
                            modifier = Modifier.padding(end = 10.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(Modifier.weight(1f))
                        transactionDetail?.Note?.let {
                            Text(
                                it,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                    HorizontalDivider(Modifier.padding(vertical = 20.dp))

                    Row {
                        Text(
                            "STATUS",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            "Completed",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFF4CAF50),
                            modifier = Modifier
                                .border(1.dp, Color(0xFF4CAF50), RoundedCornerShape(25))
                                .padding(5.dp)
                        )
                    }
                }
            }

        }
    }
}
