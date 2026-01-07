package com.example.expensetracker.view.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.expensetracker.viewModel.RoomViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeeklySpendingBarGraph(
    roomViewModel: RoomViewModel = hiltViewModel(),
    barColor: Color = MaterialTheme.colorScheme.onBackground
){
    LaunchedEffect(Unit) {
        roomViewModel.getCurrentWeekSpendingData()
    }
    val currentWeekSpending by roomViewModel.weeklySpendingData.collectAsState()

    if(currentWeekSpending.isEmpty()) return



    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        val barCount = currentWeekSpending.size
        val maxValue = currentWeekSpending.maxOrNull()?.toFloat() ?: 1f

        val columnWidth = size.width / barCount
        val barWidth = columnWidth * 0.5f // 50% of column
        val barOffsetX = (columnWidth - barWidth) / 2


        currentWeekSpending.forEachIndexed { index, value ->
            val barHeight = (value / maxValue) * size.height

            val x = index * columnWidth + barOffsetX
            val y = size.height - barHeight

            drawRoundRect(
                color = barColor,
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(
                    x = barWidth * 0.25f,
                    y = barWidth * 0.25f
                )
            )

        }
    }
}

@Composable
fun PriceYAxis(
    maxValue: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(200.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "₹$maxValue",
            style = MaterialTheme.typography.labelSmall
        )
    }
}
