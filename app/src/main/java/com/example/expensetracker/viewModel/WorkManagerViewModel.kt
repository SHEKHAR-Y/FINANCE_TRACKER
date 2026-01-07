package com.example.expensetracker.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.work.WorkManager
import com.example.expensetracker.data.workManager.workRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WorkManagerViewModel @Inject constructor(
    private val context: Context
): ViewModel() {

    fun enqueueWorkRequest(){
        WorkManager.getInstance(context)
            .enqueue(workRequest)
    }
}