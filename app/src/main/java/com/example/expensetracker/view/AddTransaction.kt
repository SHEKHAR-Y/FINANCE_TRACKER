package com.example.expensetracker.view

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.expensetracker.data.offline.table1
import com.example.expensetracker.module.ExpenseCategory
import com.example.expensetracker.utlis.fromLocalDateTime
import com.example.expensetracker.viewModel.RoomViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.example.expensetracker.R

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransaction(
    roomViewModel: RoomViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    // focus
    val amountFocus = remember { FocusRequester() }
    val categoryFocus = remember { FocusRequester() }
    val notesFocus = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    // fields
    var note by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf(ExpenseCategory.Food) }


    // date time formatting
    val date = LocalDate.now()
    val context: Context = LocalContext.current
    val currentDate = date.format(
        DateTimeFormatter.ofPattern("dd MMMM yyyy")
    ).uppercase()
    val currentDateTime = LocalDateTime.now()
    val currentInsertDateTime = System.currentTimeMillis()

    // after composition done
    LaunchedEffect(Unit) {
        amountFocus.requestFocus()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Add Expense",
                        style = MaterialTheme.typography.titleSmall
                    )
                },
                // navigate to prev screen
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onBack()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.left_arrow),
                            contentDescription = "backIcon",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                modifier = Modifier
                    .shadow(2.dp),
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding(), start = 20.dp, end = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            ElevatedCard(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(3.dp),
                colors = CardDefaults.cardColors(
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(10)
            ) {
                // Drop Down To select the category of Expense
                CategoryDropdown(
                    selectedCategory,
                    onSelectCategory = {
                        selectedCategory = it
                    }
                )
                Spacer(Modifier.height(20.dp))

                Text(
                    "ENTER AMOUNT",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                )
                Spacer(Modifier.height(15.dp))

                // amount
                TextField(
                    value = price,
                    onValueChange = { change ->
                        price = change
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(amountFocus),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            notesFocus.requestFocus()
                        }
                    ),
                    colors = TextFieldDefaults.colors(
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    textStyle = TextStyle(
                        fontSize = 40.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp,
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(Modifier.height(15.dp))

                Text(
                    currentDate,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                )
                Spacer(Modifier.height(40.dp))

                // notes
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    OutlinedTextField(
                        value = note,
                        onValueChange = { change ->
                            note = change
                        },
                        modifier = Modifier
                            .width(150.dp)
                            .focusRequester(notesFocus),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Gray,
                            unfocusedContainerColor = Color.LightGray,
                            disabledContainerColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus(force = true)
                            }
                        ),
                        shape = RoundedCornerShape(20.dp),
                        placeholder = {
                            Text(
                                "Add Notes",
                                modifier = Modifier.fillMaxWidth(),
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                fontSize = 15.sp
                            )
                        },
                        textStyle = TextStyle(
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    )
                }
                Spacer(Modifier.height(20.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                )
                {
                    Button(
                        onClick = {
                            if (note.isNotEmpty() && price.isNotEmpty()) {
                                val data = table1(
                                    Amount = price.toInt(),
                                    Note = note,
                                    type = selectedCategory.title,
                                    dateTime = fromLocalDateTime(currentDateTime)
                                )
                                roomViewModel.addTransaction(context, data)
                                println(data)
                            } else {
                                Toast.makeText(context, "Fill All", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black
                        ),
                        shape = RoundedCornerShape(30),
                        modifier = Modifier
                            .height(70.dp)
                    ) {
                        Text(
                            "Add",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(
    selectedCategory: ExpenseCategory,
    onSelectCategory: (ExpenseCategory) -> Unit
) {

    var dropDownExpanded by remember { mutableStateOf(false) }
//    var selectedCategory by rememberSaveable { mutableStateOf(ExpenseCategory.Food) }

    ExposedDropdownMenuBox(
        expanded = dropDownExpanded,
        onExpandedChange = { dropDownExpanded = !dropDownExpanded },
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        TextField(
            value = "${selectedCategory.title}\n${selectedCategory.description}", // or selectedCategory.getTitle()
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropDownExpanded)
            },
            modifier = Modifier
                .menuAnchor()  // CRUCIAL: anchors menu to this TextField
                .padding(5.dp)
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color(0xFFD3D3D3),
                focusedContainerColor = Color(0xFFD3D3D3),
            ),
            textStyle = TextStyle(
                textAlign = TextAlign.Center
            )
        )

        ExposedDropdownMenu(
            expanded = dropDownExpanded,
            onDismissRequest = { dropDownExpanded = false },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
        ) {
            ExpenseCategory.entries.forEach { category ->
                DropdownMenuItem(
                    text = {
                        Column {
                            Text(
                                text = category.title,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = category.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    onClick = {
//                        selectedCategory = category
                        onSelectCategory(category)
                        dropDownExpanded = false
                    }
                )
            }
        }
    }
}
