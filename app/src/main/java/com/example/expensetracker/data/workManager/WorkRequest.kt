package com.example.expensetracker.data.workManager

import androidx.work.OneTimeWorkRequestBuilder

// build work request
val workRequest = OneTimeWorkRequestBuilder<LocationWorker>()
    .setConstraints(constraints)
    .build()
