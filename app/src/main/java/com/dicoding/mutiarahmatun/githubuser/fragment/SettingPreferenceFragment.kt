package com.dicoding.mutiarahmatun.githubuser.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle

import androidx.preference.PreferenceFragmentCompat
import android.provider.Settings
import androidx.preference.Preference
import com.dicoding.mutiarahmatun.githubuser.AlarmReceiver
import com.dicoding.mutiarahmatun.githubuser.R

class SettingPreferenceFragment : PreferenceFragmentCompat(),
        SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var reminder: String
    private lateinit var changeLanguage: String
    private lateinit var changeSettingPreference: Preference

    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreatePreferences(bundle: Bundle?, s: String?) {

        addPreferencesFromResource(R.xml.preferences)
        init()

        changeSettingPreference.setOnPreferenceClickListener {
            val changeLangIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(changeLangIntent)
            true
        }

        alarmReceiver = AlarmReceiver()
    }

    private fun init() {
        reminder = resources.getString(R.string.key_reminder)
        changeLanguage = resources.getString(R.string.key_change_language)
        changeSettingPreference = findPreference<Preference>(changeLanguage) as Preference
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }


    override fun onSharedPreferenceChanged(preferences: SharedPreferences?, key: String?) {
        val pref: Preference? = findPreference(key!!)
        when (key) {
            reminder -> {
                when (preferences?.getBoolean(reminder, false)) {
                    true -> {
                        pref?.summary = getString(R.string.reminder_is_set)
                        alarmReceiver.setDailyReminder(
                                requireContext(),
                                "09.00", "Find new favorite user on Github"
                        )
                    }
                    else -> {
                        pref?.summary = getString(R.string.reminder_not_set)
                        alarmReceiver.cancelAlarm(requireContext())
                    }
                }
            }
        }
    }
}