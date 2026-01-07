package com.example.expensetracker.utlis

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import kotlin.time.ExperimentalTime
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.WeekFields
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@TypeConverter
fun fromLocalDateTime(dateTime: LocalDateTime?): Long? {
    return dateTime
        ?.atZone(ZoneId.systemDefault())
        ?.toInstant()
        ?.toEpochMilli()
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalTime::class)
@TypeConverter
fun toLocalDateTime(timestamp: Long?): LocalDateTime? {
    return timestamp?.let {
        Instant.ofEpochMilli(it)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun CurrentWeekTimeStamps(): Pair<Long, Long>{
    val zone = ZoneId.systemDefault()
    val today = LocalDate.now(zone)

    val startOfWeek = today.with(WeekFields.of(Locale.getDefault()).dayOfWeek(),1)
    val endOfWeek = startOfWeek.plusDays(6)

    val startMillis = startOfWeek.atStartOfDay(zone).toInstant().toEpochMilli()
    val endMillis = endOfWeek.atTime(23, 23, 59).atZone(zone).toInstant().toEpochMilli()

    return startMillis to endMillis
}