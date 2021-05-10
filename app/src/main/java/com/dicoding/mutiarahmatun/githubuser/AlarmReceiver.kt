package com.dicoding.mutiarahmatun.githubuser

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.dicoding.mutiarahmatun.githubuser.activity.MainActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val TYPE_DAILY = "Daily Reminder"
        const val EXTRA_MESSAGE = "message"
        private const val ID_DAILY = 101
        private const val TIME_DAILY = "HH:mm"
    }

    fun setDailyReminder(context: Context, time: String, message: String) {

        if (isDateInvalid(time, TIME_DAILY)) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)

        intent.putExtra(EXTRA_MESSAGE, message)

        val timeArray = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(
                context,
                ID_DAILY, intent, 0
        )
        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        )
        Toast.makeText(context, "Daily Reminder Is ON", Toast.LENGTH_SHORT).show()
    }

    fun isAlarmSet(context: Context): Boolean {
        val intent = Intent(context, AlarmReceiver::class.java)
        val requestCode = ID_DAILY

        return PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_NO_CREATE
        ) != null
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val requestCode = ID_DAILY
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        Toast.makeText(context, "Daily Reminder Is OFF", Toast.LENGTH_SHORT).show()
    }

    private fun showAlarmNotification(
            context: Context,
            title: String?,
            message: String,
            notificationId: Int
    ) {

        val channelId = "Channel_1"
        val channelName = "Daily Notification Alarm"

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
                context, 0,
                intent, PendingIntent.FLAG_ONE_SHOT
        )

        val notificationManagerCompat =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.icon_alarm)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setSound(alarmSound)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(channelId)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(notificationId, notification)
    }

    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra(EXTRA_MESSAGE)
        val title = TYPE_DAILY
        val notificationId = ID_DAILY
        showToast(context, title, message)
        if (message != null) {
            showAlarmNotification(context, title, message, notificationId)
        }
    }

    private fun showToast(context: Context, title: String, message: String?) {
        Toast.makeText(context, "$title : $message", Toast.LENGTH_LONG).show()
    }

    private fun isDateInvalid(date: String, format: String): Boolean {
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(date)
            false
        } catch (e: ParseException) {
            true
        }
    }
}