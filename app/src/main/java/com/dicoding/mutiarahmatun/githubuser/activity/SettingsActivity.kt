package com.dicoding.mutiarahmatun.githubuser.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.mutiarahmatun.githubuser.R
import com.dicoding.mutiarahmatun.githubuser.fragment.SettingPreferenceFragment


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportFragmentManager.beginTransaction().add(R.id.action_settings, SettingPreferenceFragment()).commit()
    }
}