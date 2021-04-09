package com.dicoding.mutiarahmatun.githubuser

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailUsers : AppCompatActivity() {
    private var title: String = "User Detail"
    companion object {
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val tvImage: ImageView = findViewById(R.id.img_avatar_received)
        val tvUsername: TextView = findViewById(R.id.tv_username_received)
        val tvName: TextView = findViewById(R.id.tv_name_received)
        val tvLocation: TextView = findViewById(R.id.tv_location_received)
        val tvCompany: TextView = findViewById(R.id.tv_company_received)
        val tvRepo: TextView = findViewById(R.id.tv_repo_received)
        val tvFollowers: TextView = findViewById(R.id.tv_followers_received)
        val tvFollowing: TextView = findViewById(R.id.tv_following_received)
        val tvRepoTag: TextView = findViewById(R.id.repository)
        val tvFollowerTag: TextView = findViewById(R.id.followers)
        val tvFollowingTag: TextView = findViewById(R.id.following)

        val user = intent.getParcelableExtra<Users>(EXTRA_USER) as Users

        tvImage.setImageResource(user.avatar)
        tvUsername.text = user.username
        tvName.text = user.name
        tvLocation.text = " " +user.location
        tvCompany.text = " " + user.company
        tvRepo.text = user.repository
        tvFollowers.text = user.follower
        tvFollowing.text = user.following
        tvRepoTag.text = getString(R.string.tag_repo)
        tvFollowerTag.text = getString(R.string.tag_followers)
        tvFollowingTag.text = getString(R.string.tag_following)

        title = "Detail of ${user.name.toString()}"
        setActionBarTitle(title)

    }

    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }
}