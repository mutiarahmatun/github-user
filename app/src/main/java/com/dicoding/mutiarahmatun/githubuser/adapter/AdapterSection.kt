package com.dicoding.mutiarahmatun.githubuser.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.mutiarahmatun.githubuser.fragment.DetailUserFragment
import com.dicoding.mutiarahmatun.githubuser.fragment.FollowersFragment
import com.dicoding.mutiarahmatun.githubuser.model.Users

class AdapterSection(activity: AppCompatActivity, user: Users) : FragmentStateAdapter(activity) {

    private var userTemp : Users = user

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = DetailUserFragment(userTemp)
            1 -> fragment = FollowersFragment.newInstance(1, "${userTemp.username}")
            2 -> fragment = FollowersFragment.newInstance(2, "${userTemp.username}")
        }
        return fragment as Fragment
    }
}