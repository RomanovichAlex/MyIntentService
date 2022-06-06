package by.romanovich.myintentservice


import android.R
import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.Toast


private const val TAG = "@@@"

class ToastService : Service() {

    companion object {
        private const val TEXT_EXTRA_KEY = "TEXT_EXTRA_KEY"

        fun getLaunchIntent(context: Context, message: String): Intent {
            val serviceIntent = Intent(context, ToastService::class.java)
            serviceIntent.putExtra(TEXT_EXTRA_KEY, message)
            return serviceIntent
        }


        /*fun startToastJob(context: Context, message: String) {
            val serviceIntent = Intent(context, ToastIntentService::class.java)
            serviceIntent.putExtra(TEXT_EXTRA_KEY, message)
            context.startService(serviceIntent)
        }*/
    }


    private var callback: (Boolean) -> Unit = {}
    private val binder = ToastServiceBinder()


    override fun onCreate() {
        super.onCreate()
        //val notificationManager = NotificationManagerCompat.from(this)
        /*val channelId = "channelId"
        //создали канал
        notificationManager.createNotificationChannel(
            NotificationChannelCompat.Builder(channelId, NotificationManagerCompat.IMPORTANCE_HIGH).build()
        )*/
        val notification = Notification.Builder(this)//, channelId)
            .setContentTitle("OLOLOL")
            .setContentText("BBBBBB")
            .setSmallIcon(R.drawable.ic_lock_lock)
            .setProgress(10, 5, true) // display indeterminate progress
            .build()
        startForeground(0, notification)
        Log.d(TAG, "onCreate() called")
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    //метод где делается вся работа, флаги - говорят о поведении после прекращения работы,
    // интент это с чем запустили,
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(
            TAG,
            "onStartCommand() called with: intent = $intent, flags = $flags, startId = $startId"
        )
        //если нулл то это сообщение - Empty
        val message = intent.extras?.getString(TEXT_EXTRA_KEY) ?: "Empty"
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        Thread {
            Thread.sleep(1000)
            Log.d(TAG, "stopSelf() startId = $startId")
            callback(true)
            //метод который может остановить сам себя stopSelf()
            //благодаря startId выполняются все потоки до последнего, потом выключается, без startId выполнился бы первый поток потом отключился потом выполнились остольные потоки
            stopSelf(startId)
        }.start()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "onBind() called with: intent = $intent")
        return binder
    }

    //метод для отвязки сервиса
    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "onUnbind() called with: intent = $intent")
        return super.onUnbind(intent)
    }


    fun setCallback(function: (Boolean) -> Unit) {
        callback = function
    }


    inner class ToastServiceBinder : Binder() {
        //val message = "Сработало!!!"
        val service = this@ToastService
    }
}
