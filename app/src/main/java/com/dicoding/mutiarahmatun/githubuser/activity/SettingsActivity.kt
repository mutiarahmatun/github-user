package com.dicoding.mutiarahmatun.githubuser.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.mutiarahmatun.githubuser.AlarmReceiver
import com.dicoding.mutiarahmatun.githubuser.R
import com.dicoding.mutiarahmatun.githubuser.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private lateinit var alarmReceiver: AlarmReceiver
    private var isAlarm = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        alarmReceiver = AlarmReceiver()

        if(alarmReceiver.isAlarmSet(this)){
            isAlarm = true
        }

        binding.switchReminder.isChecked = isAlarm
        binding.switchReminder.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val repeatTime = "09.00"
                val repeatMessage = getString(R.string.daily_message)
                alarmReceiver.setDailyReminder(this, repeatTime, repeatMessage)

            } else {
                alarmReceiver.cancelAlarm(this)
            }
        }

        binding.changeLanguageSettings.setOnClickListener {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }

        binding.buttonBackSettings.setOnClickListener { finish() }

    }
}