package com.dicoding.mutiarahmatun.githubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.StringRes
import com.dicoding.mutiarahmatun.githubuser.databinding.ActivityTabLayoutBinding
import com.google.android.material.tabs.TabLayoutMediator

class TabLayout : AppCompatActivity() {
    private lateinit var tabLayoutBinding: ActivityTabLayoutBinding
    private var title: String = "Detail's User"

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tag_profile,
            R.string.tag_followers,
            R.string.tag_following
        )
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tabLayoutBinding = ActivityTabLayoutBinding.inflate(layoutInflater)
        setContentView(tabLayoutBinding.root)

        val users = intent.getParcelableExtra<Users>(EXTRA_USER) as Users

        val adapterSection = AdapterSection(this, users)
        tabLayoutBinding.viewPager.adapter = adapterSection

        //menghubungkan ViewPager2 dengan TabLayout dengan menggunakan TabLayoutMediator
        TabLayoutMediator(tabLayoutBinding.tabs, tabLayoutBinding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        title = "Detail of ${users.name?: users.username}"
        setActionBarTitle(title)

        // calling the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

}