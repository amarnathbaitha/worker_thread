package com.example.myspecial.myworkmanager.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

//We need to create a separate worker class for particular events.
class DemoWorker(context: Context,params: WorkerParameters): Worker(context,params) {

    private val tag = "Amarnathbaitha"
    override fun doWork(): Result {
        // Here we need to add long running task in this method
       performWorker()
        return  Result.success()
       // return  Result.failure()
        //return  Result.retry()
    }

    fun performWorker(){
        Thread.sleep(2000)
        Log.i(tag,"Task Completed")
    }
}