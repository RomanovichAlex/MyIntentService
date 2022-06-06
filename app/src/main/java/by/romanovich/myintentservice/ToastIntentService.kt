package by.romanovich.myintentservice

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast


private const val TAG = "@@@"

class ToastIntentService : CustomIntentService("ToastIntentService") {

    companion object {
        private const val TEXT_EXTRA_KEY = "TEXT_EXTRA_KEY"

        fun startToastJob(context: Context, message: String) {
            val serviceIntent = Intent(context, ToastIntentService::class.java)
            serviceIntent.putExtra(TEXT_EXTRA_KEY, message)
            context.startService(serviceIntent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.d(TAG, "onHandleIntent() called with: intent = $intent")
        val message = intent?.getStringExtra(TEXT_EXTRA_KEY) ?: "EMPTY"
        val toastText = "$message ${Thread.currentThread().name}"
        Thread.sleep(3_000)
        //запускаем с главного потока
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
        }
    }

}