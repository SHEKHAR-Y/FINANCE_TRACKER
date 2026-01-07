package com.example.expensetracker.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.expensetracker.R
import com.example.expensetracker.data.offline.table2
import com.example.expensetracker.module.OnBoardingStep
import com.example.expensetracker.utlis.fromLocalDateTime
import com.example.expensetracker.viewModel.RoomViewModel
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StartUpScreen(
    roomViewModel: RoomViewModel = hiltViewModel(), onSuccess: () -> Unit, onBack: () -> Unit
) {
    // STEP
    var step by remember { mutableStateOf(OnBoardingStep.NAME) }

    // states
    var name by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }

    // focus manager
    val focusManager = LocalFocusManager.current

    // focus requester
    val nameFocus = remember { FocusRequester() }
    val budgetFocus = remember { FocusRequester() }

    // date and time
    val currentDateAndTime = LocalDateTime.now()
    val month = currentDateAndTime.month.name

    // is insert success
    val insertionSuccess by roomViewModel.insertFirstBudgetData.collectAsState()

    // side effects to run on composition complete
    LaunchedEffect(Unit) {
        nameFocus.requestFocus()
    }

    // ui
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "BASIC DETAILS", style = MaterialTheme.typography.titleSmall
                    )
                },
                modifier = Modifier.shadow(2.dp),
                navigationIcon = {

                },
            )
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 40.dp
                ), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (step) {
                OnBoardingStep.NAME -> {
                    NameStep(
                        name = name,
                        onNameChange = { name = it },
                        onNext = {
                            step = OnBoardingStep.BUDGET
                        }
                    )
                }

                OnBoardingStep.BUDGET -> {
                    BudgetStep(
                        Budget = budget,
                        onBudgetChange = { budget = it },
                        onNext = {
                            if (name.isNotEmpty() && budget.isNotEmpty()) {
                                try {
                                    val data = table2(
                                        Budget = budget.toInt(),
                                        CurrentBalance = budget.toInt(),
                                        LastUpdated = fromLocalDateTime(currentDateAndTime),
                                        LastResetMonth = month,
                                        UserName = name
                                    )
                                    roomViewModel.addBudgetData(data)
                                    if (insertionSuccess) {
                                        onSuccess()
                                    }
                                } catch (e: Exception) {
                                    println(e.message)
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun NameStep(
    name: String,
    onNameChange: (String) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            "what's your name ?",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, bottom = 10.dp)
        )
        OutlinedTextField(
            value = name,
            onValueChange = { onNameChange(it) },
            textStyle = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            ),
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {}),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                disabledContainerColor = MaterialTheme.colorScheme.background,
                focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                disabledBorderColor = MaterialTheme.colorScheme.onBackground,
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            "By clicking next you agree to share you name with us.",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            textAlign = TextAlign.Center
        )
        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(25),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onBackground,
                contentColor = MaterialTheme.colorScheme.background
            ),
            onClick = {
                // check
                if (name.isNotEmpty()) {
                    try {
                        onNext()
                    } catch (e: Exception) {
                        println(e.message)
                    }
                }
            }) {
            Text(
                "Next"
            )
        }
    }
}

@Composable
fun BudgetStep(
    Budget: String,
    onBudgetChange: (String) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            "Whats your monthly Budget ?",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, bottom = 10.dp)
        )
        OutlinedTextField(
            value = Budget,
            onValueChange = { onBudgetChange(it) },
            textStyle = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            ),
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {}),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                disabledContainerColor = MaterialTheme.colorScheme.background,
                focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                disabledBorderColor = MaterialTheme.colorScheme.onBackground,
            )
        )
        Text(
            "What does it mean :",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 10.dp),
        )

        Text("- Your Salary")
        Text("- Your Pocket Money")
        Text("- Or Even Your Combined Income")

        Text(
            "ex: your salary is 2 lakh, so your monthly budget is 2,00,000.00",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        )

        Spacer(modifier = Modifier.weight(1f))
        Text(
            "By clicking Submit you agree all terms & conditions",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            textAlign = TextAlign.Center
        )
        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(25),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onBackground,
                contentColor = MaterialTheme.colorScheme.background
            ),
            onClick = {
                // check
                if (Budget.isNotEmpty()) {
                    try {
                        onNext()
                    } catch (e: Exception) {
                        println(e.message)
                    }
                }
            }) {
            Text(
                "Submit"
            )
        }
    }
}