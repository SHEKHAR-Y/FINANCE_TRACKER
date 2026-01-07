package com.example.expensetracker.view

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowCircleLeft
import androidx.compose.material.icons.twotone.CurrencyRupee
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.expensetracker.viewModel.DataStoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateBalance(
    onBack: () -> Unit,
    dataStoreViewModel: DataStoreViewModel = hiltViewModel()
) {
    var balance by remember { mutableStateOf("") }

    // scaffold for top app bar
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Update Balance",
                        style = MaterialTheme.typography.titleSmall
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onBack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.TwoTone.ArrowCircleLeft,
                            contentDescription = null
                        )
                    }
                },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding(), start = 20.dp, end = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        )
        {
            // amount to be updated
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(25))
            ){
                TextField(
                    value = balance,
                    onValueChange = { change ->
                        balance = change
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.TwoTone.CurrencyRupee,
                            contentDescription = null
                        )
                    }
                )
            }
            Button(
                onClick = {
                    try {
                        val updatedBalance = balance.toInt()
                        dataStoreViewModel.changeBalance(updatedBalance)
                    } catch (e: Exception) {
                        println("cannot update : balance must be a valid integer")
                    }
                }) {
                Text("Update Balance")
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    onBack()
                }
            ) {
                Text("Back")
            }
        }
    }

}