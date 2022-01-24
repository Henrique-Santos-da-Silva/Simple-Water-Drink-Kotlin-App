package club.androidexpress.waterdrink

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class NotificationPublisher: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val actionIntent = Intent(context.applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, actionIntent, 0)

        val message = intent.getStringExtra(KEY_NOTIFICATION)
        val id = intent.getIntExtra(KEY_NOTIFICATION_ID, 0)

        val notification: Notification = getNotification(message, context, pendingIntent)


        notificationManager.notify(id, notification)
    }

    private fun getNotification(content: String?, context: Context, intent: PendingIntent): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context)
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentText(content)
            .setContentIntent(intent)
            .setTicker("Alerta")
            .setAutoCancel(false)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setSmallIcon(R.drawable.cup_water)

        return builder.build()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT).apply {
            enableVibration(true)
            vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 300, 200, 400)
        }

        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val CHANNEL_ID = "DEFAULT_ID_CHANNEL"
        private const val CHANNEL_NAME = "DEFAULT_NAME_CHANNEL"

        const val KEY_NOTIFICATION = "key_notification"
        const val KEY_NOTIFICATION_ID = "key_notification_id"

    }
}