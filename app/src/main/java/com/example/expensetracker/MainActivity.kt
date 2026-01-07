package com.example.expensetracker

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ListAlt
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.ListAlt
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.Receipt
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.dataStore
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.expensetracker.data.offline.table1
import com.example.expensetracker.data.offline.table2
import com.example.expensetracker.module.ResultState
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme
import com.example.expensetracker.utlis.AppEntry
import com.example.expensetracker.utlis.addTransaction
import com.example.expensetracker.utlis.analytics
import com.example.expensetracker.utlis.dashboard
import com.example.expensetracker.utlis.expenseDetails
import com.example.expensetracker.utlis.list
import com.example.expensetracker.utlis.startUp
import com.example.expensetracker.utlis.updateBalance
import com.example.expensetracker.view.AddTransaction
import com.example.expensetracker.view.Analytics
import com.example.expensetracker.view.DashBoard
import com.example.expensetracker.view.ExpenseDetail
import com.example.expensetracker.view.StartUpScreen
import com.example.expensetracker.view.TransactionList
import com.example.expensetracker.view.UpdateBalance
import com.example.expensetracker.viewModel.RoomViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpenseTrackerTheme {
                Navigation()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(
    roomViewModel: RoomViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    LaunchedEffect(Unit) {
        roomViewModel.getBudgetData()
    }
    val startDestination = AppEntry

    val showBottomBar = currentRoute in listOf(
        dashboard::class.qualifiedName,
        list::class.qualifiedName,
        analytics::class.qualifiedName
    )


    Scaffold(
        bottomBar = {
            if(showBottomBar){
                BottomNavBar(navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController,
            startDestination = startDestination,
            modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            composable<AppEntry> {
                val budgetState by roomViewModel.budgetData.collectAsState()

                LaunchedEffect(budgetState) {
                    when (budgetState) {
                        is ResultState.Loading -> {
                            // Does nothing
                        }

                        is ResultState.Success -> {
                            val data = (budgetState as ResultState.Success<table2?>).data
                            if (data != null) {
                                navController.navigate(dashboard) {
                                    popUpTo<AppEntry> { inclusive = true }
                                }
                            } else {
                                navController.navigate(startUp) {
                                    popUpTo<AppEntry> { inclusive = true }
                                }
                            }
                        }

                        is ResultState.Failure -> {
                            navController.navigate(startUp) {
                                popUpTo<AppEntry> { inclusive = true }
                            }
                        }
                    }
                }
            }

            // startup screen
            composable<startUp> {
                StartUpScreen(
                    onSuccess = {
                        navController.navigate(dashboard) {
                            popUpTo(startUp) {
                                inclusive = true
                            }
                        }
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            // dashboard
            composable<dashboard> {
                DashBoard(
                    onClick = {
                        navController.navigate(addTransaction)
                    },
                    onCLickUpdateBalance = {
                        navController.navigate(updateBalance)
                    },
                    onTransactionClick = { id ->
                        navController.navigate(expenseDetails(id))
                    }
                )
            }

            // transaction list
            composable<list> {
                TransactionList(
                    onBack = {
                        navController.popBackStack()
                    },
                    onTransactionItemClick = { id ->
                        navController.navigate(expenseDetails(id))
                    }
                )
            }

            // analytics
            composable<analytics> {
                Analytics(
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            // add transaction
            composable<addTransaction> {
                AddTransaction(
                    onBack = {
                        navController.popBackStack()
                    },
                )
            }

            // transaction details
            composable<expenseDetails> { backStackEntry ->
                val args = backStackEntry.toRoute<expenseDetails>()

                ExpenseDetail(
                    id = args.id,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            // update balance
            composable<updateBalance> {
                UpdateBalance(
                    onBack = {
                        navController.popBackStack()
                    },

                    )
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavController) {
    NavigationBar (
        containerColor = MaterialTheme.colorScheme.surface,
    ){
        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate(dashboard) {
                    popUpTo(dashboard) { inclusive = false }
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Rounded.Dashboard, null) },
            label = { Text("Dashboard") }
        )

        NavigationBarItem(
            selected = false,
            onClick = {
                val transactions = null
                navController.navigate(list)
            },
            icon = { Icon(Icons.AutoMirrored.Rounded.ListAlt, null) },
            label = { Text("Transactions") }
        )

        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate(analytics)
            },
            icon = { Icon(Icons.Outlined.Add, null) },
            label = { Text("Analytics") }
        )
    }
}
