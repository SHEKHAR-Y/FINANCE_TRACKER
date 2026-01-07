package com.example.expensetracker.data.workManager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

//  worker that actually perform task
class LocationWorker(
    context: Context,
    params: WorkerParameters
): Worker(context, params) {
    override fun doWork(): Result {
        // do work below
        work()
        // return result mandatory
        return Result.success()
    }
}

fun work(){
    println("System is working even in background")
}