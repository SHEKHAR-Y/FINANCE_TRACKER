package com.example.expensetracker.data.workManager

import androidx.work.Constraints
import androidx.work.NetworkType

// build constraints
val constraints = Constraints.Builder()
    .setRequiredNetworkType(NetworkType.CONNECTED)
    .build()
