package com.example.myspecial.myworkmanager

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.WorkManager
import com.example.myspecial.myworkmanager.ui.theme.MyWorkManagerTheme

import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import com.example.myspecial.myworkmanager.worker.DemoWorker
import java.util.concurrent.TimeUnit
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.impl.WorkManagerImpl


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyWorkManagerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )

                }
            }
        }
        doWork()
    }

    private fun doWork() {
        // here we can call either periodically(every two hour or 3 hours etc.. Example if you are running news app then every hour we need to get data from the server and update) or one time
        // this is the example of one time worker

        // set Constraints to check the network
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()


        // then we need to add constraints in request, this
        val request = OneTimeWorkRequestBuilder<DemoWorker>()
            .setConstraints(constraints)
            // the below code will try every 10,20,30,40 sec .... if this is fail
            // BackoffPolicy.Exponential, In this 10 20, 40 80 like this
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                5,
                TimeUnit.SECONDS
            )
            .build()


       // The minimum interval for PeriodicWorkRequest is 15 minutes.

       // If you try to use a shorter interval (e.g., 10 seconds), you'll get an IllegalArgumentException.
        //  There are 15 minutes gap in two request
//        val request = PeriodicWorkRequestBuilder<DemoWorker>(
//           15, // Minimum repeat interval
//            TimeUnit.MINUTES
//        )
//            .setConstraints(constraints)
//            .setBackoffCriteria(
//                BackoffPolicy.LINEAR,
//                10,
//                TimeUnit.SECONDS
//            )
//            .build()


        // then we need to add request in enqueue
        WorkManager.getInstance(this).enqueue(request)
        // We can use chain request, if we have multiple request then we can define with below code.
       // WorkManager.getInstance(this).beginWith(request).then(request).then(request).enqueue()

        WorkManager.getInstance(this)
            .getWorkInfoByIdLiveData(request.id)
            .observe(this) { workInfo ->
                if (workInfo != null) {
                    when (workInfo.state) {
                        WorkInfo.State.SUCCEEDED -> {
                           Log.i("Amarnathbaitha", workInfo.state.name)
                        }
                        WorkInfo.State.FAILED -> {
                            Log.i("Amarnathbaitha",workInfo.state.name)
                        }
                        WorkInfo.State.RUNNING -> {
                            Log.i("Amarnathbaitha",workInfo.state.name)
                        }
                        else -> {
                            Log.i("Amarnathbaitha", workInfo.state.name)
                            // Handle other states: ENQUEUED, BLOCKED, CANCELLED
                        }
                    }
                }
            }


    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyWorkManagerTheme {
        Greeting("Android")
    }
}