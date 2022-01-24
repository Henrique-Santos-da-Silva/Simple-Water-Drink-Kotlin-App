//package club.androidexpress.waterdrink
//
//import android.app.AlarmManager
//import android.app.PendingIntent
//import android.content.Context
//import android.content.Intent
//import android.content.SharedPreferences
//import android.widget.Toast
//import androidx.core.content.ContextCompat
//import kotlinx.android.synthetic.main.activity_main.*
//import java.util.*
//
//
//// With Kotlin Extensions
//time_picker.setIs24HourView(true)
//
//// Shared Preferences -> Salvando dados através de chave-valor
//val preferences: SharedPreferences = getPreferences(Context.MODE_PRIVATE)
//isActived = preferences.getBoolean(KEY_NOTIFY, false)
//
//if (isActived) {
//    btn_notify.setText(R.string.pause)
//    btn_notify.backgroundTintList = ContextCompat.getColorStateList(this, R.color.blue_light_400)
//
//    val interval: Int = preferences.getInt(MainActivity.KEY_INTERVAL, 0)
//    val hour: Int = preferences.getInt(MainActivity.KEY_HOUR, time_picker.currentHour)
//    val minute: Int = preferences.getInt(MainActivity.KEY_MINUTE, time_picker.currentMinute)
//
//    edit_text_number_interval.setText(interval.toString())
//    time_picker.currentHour = hour
//    time_picker.currentMinute = minute
//}
//
//btn_notify.setOnClickListener {
//    val interval: Int
//    val sInterval: String = edit_text_number_interval.text.toString()
//
//    if (sInterval.isEmpty()) {
//        Toast.makeText(this, "Digite o intervalo", Toast.LENGTH_SHORT).show()
//        return@setOnClickListener
//    }
//
//    val hour: Int = time_picker.currentHour
//    val minute: Int = time_picker.currentMinute
//    interval= sInterval.toInt()
//
//    if (!isActived) {
//        btn_notify.setText(R.string.pause)
//        btn_notify.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_400))
//
//        with(preferences.edit()) {
//            putBoolean(MainActivity.KEY_NOTIFY, true)
//            putInt(MainActivity.KEY_INTERVAL, interval)
//            putInt(MainActivity.KEY_HOUR, hour)
//            putInt(MainActivity.KEY_MINUTE, minute)
//            apply()
//        }
//
//        val notificationIntent = Intent(this, NotificationPublisher::class.java).apply {
//            putExtra(NotificationPublisher.KEY_NOTIFICATION_ID, 1)
//            putExtra(NotificationPublisher.KEY_NOTIFICATION, "Hora de beber água")
//        }
//
//        val calendar = Calendar.getInstance().apply {
//            set(Calendar.HOUR_OF_DAY, hour)
//            set(Calendar.MINUTE, minute)
//        }
//
//        val broadcast = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT)
//
//        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, interval * 1000.toLong(), broadcast)
//
//
//        isActived = true
//    } else {
//        btn_notify.setText(R.string.bnt_notify_text)
//        btn_notify.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_700))
//
//        with(preferences.edit()) {
//            putBoolean(MainActivity.KEY_NOTIFY, false)
//            remove(MainActivity.KEY_INTERVAL)
//            remove(MainActivity.KEY_HOUR)
//            remove(MainActivity.KEY_MINUTE)
//            apply()
//        }
//
//        val notificationIntent = Intent(this, NotificationPublisher::class.java)
//
//        val broadcast = PendingIntent.getBroadcast(this, 0, notificationIntent, 0)
//        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        alarmManager.cancel(broadcast)
//
//        isActived = false
//    }
//}