package club.androidexpress.waterdrink

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import club.androidexpress.waterdrink.NotificationPublisher.Companion.KEY_NOTIFICATION
import club.androidexpress.waterdrink.NotificationPublisher.Companion.KEY_NOTIFICATION_ID
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    var isActived: Boolean = false
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferences = getPreferences(Context.MODE_PRIVATE)

        isActived = preferences.getBoolean(KEY_NOTIFY, false)

        setupUi(isActived, preferences)

        time_picker.setIs24HourView(true)

        btn_notify.setOnClickListener { notifyButton() }

    }

    private fun notifyButton() {
        if (!isActived) {

            if (!intervalIsValid()) return

            val hour: Int = time_picker.currentHour
            val minute: Int = time_picker.currentMinute
            val interval: Int= Integer.parseInt(edit_text_number_interval.text.toString())

            updateStorage(true, interval, hour, minute)
            setupUi(true, preferences)

            setupNotification(true, interval, hour, minute)
            alert(R.string.notified)


            isActived = true

        } else {
            updateStorage(false, 0, 0, 0)
            setupUi(false, preferences)
            setupNotification(false, 0, 0, 0)
            alert(R.string.notified_pause)

            isActived = false
        }
    }

    private fun intervalIsValid(): Boolean {
        val sInterval: String = edit_text_number_interval.text.toString()

        if (sInterval.isEmpty()) {
            alert(R.string.validation)
            return false
        }

        if (sInterval == "0") {
            alert(R.string.zero_value)
            return false
        }

        return true
    }

    private fun updateStorage(added: Boolean, interval: Int, hour: Int, minute: Int) {
        val edit: SharedPreferences.Editor = preferences.edit()
        edit.putBoolean(KEY_NOTIFY, added)

        if (added) {
            edit.apply {
                putInt(KEY_INTERVAL, interval)
                putInt(KEY_HOUR, hour)
                putInt(KEY_MINUTE, minute)
            }
        } else {
            edit.apply {
                remove(KEY_INTERVAL)
                remove(KEY_HOUR)
                remove(KEY_MINUTE)
            }
        }

        edit.apply()
    }

    private fun setupNotification(added: Boolean, interval: Int, hour: Int, minute: Int) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationIntent = Intent(this, NotificationPublisher::class.java)


        if (added) {
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
            }

            notificationIntent.apply {
                putExtra(KEY_NOTIFICATION_ID, 1)
                putExtra(KEY_NOTIFICATION, "Hora de beber água")
            }

            val broadcast = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT)

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, interval * 60 * 1000.toLong(), broadcast)

        } else {
            val broadcast = PendingIntent.getBroadcast(this, 0, notificationIntent, 0)
            alarmManager.cancel(broadcast)
        }
    }

    private fun setupUi(activated: Boolean, storage: SharedPreferences) {
        if (activated) {
            btn_notify.setText(R.string.pause)
            btn_notify.setBackgroundResource(R.drawable.bg_button_background_light)

            val interval: Int = storage.getInt(KEY_INTERVAL, 0)
            val hour: Int = storage.getInt(KEY_HOUR, time_picker.currentHour)
            val minute: Int = storage.getInt(KEY_MINUTE, time_picker.currentMinute)

            edit_text_number_interval.setText(interval.toString())
            time_picker.currentHour = hour
            time_picker.currentMinute = minute

        } else {
            btn_notify.setText(R.string.bnt_notify_text)
            btn_notify.setBackgroundResource(R.drawable.bg_button_background)
        }

    }

    private fun alert(resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val KEY_NOTIFY = "activated";
        private const val KEY_INTERVAL = "interval"
        private const val KEY_HOUR = "hour";
        private const val KEY_MINUTE = "minute";
    }
}