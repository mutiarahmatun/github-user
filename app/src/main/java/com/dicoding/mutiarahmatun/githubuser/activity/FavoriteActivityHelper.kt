package com.dicoding.mutiarahmatun.githubuser.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.mutiarahmatun.githubuser.adapter.AdapterSection
import com.dicoding.mutiarahmatun.githubuser.databinding.ActivityTabLayoutBinding
import com.dicoding.mutiarahmatun.githubuser.model.Users
import com.google.android.material.tabs.TabLayoutMediator

class FavoriteActivityHelper : AppCompatActivity() {

    private var title: String = "User Favorite Detail"
    private var users: Users? = null
    private var position: Int = 0
    private lateinit var activityTabLayoutBinding: ActivityTabLayoutBinding
    private var dummyFavorite = Users(0,"Empty Favorite","", "", "","","","","")

    companion object {
        const val EXTRA_FAVORITE = "extra_favorite"
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_UPDATE = 200
        const val RESULT_DELETE = 301
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityTabLayoutBinding = ActivityTabLayoutBinding.inflate(layoutInflater)
        setContentView(activityTabLayoutBinding.root)

        users = intent.getParcelableExtra(EXTRA_FAVORITE)
        if (users != null) {
            position = intent.getIntExtra(EXTRA_POSITION, 0)
        } else {
            users = dummyFavorite
        }

        val adapterSection = AdapterSection(this, users!!)
        activityTabLayoutBinding.viewPager.adapter = adapterSection

        TabLayoutMediator(activityTabLayoutBinding.tabs, activityTabLayoutBinding.viewPager) { tab, position ->
            tab.text = resources.getString(TabLayout.TAB_TITLE[position])
        }.attach()
        supportActionBar?.elevation = 0f

        setActionBarTitle(title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(menuItem)
    }
}