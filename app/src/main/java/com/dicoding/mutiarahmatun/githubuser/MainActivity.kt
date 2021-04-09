package com.dicoding.mutiarahmatun.githubuser

import android.content.Intent
import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : AppCompatActivity(){

    private var title: String = "Github User's"

    private lateinit var adapter: UsersAdapter
    private lateinit var dataName: Array<String>
    private lateinit var dataUserName: Array<String>
    private lateinit var dataFollowers: Array<String>
    private lateinit var dataFollowing: Array<String>
    private lateinit var dataAvatar: TypedArray
    private lateinit var dataCompany: Array<String>
    private lateinit var dataLocation: Array<String>
    private lateinit var dataRepository: Array<String>
    private var users = arrayListOf<Users>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setActionBarTitle(title)

        val listView: ListView = findViewById(R.id.lv_list)
        adapter = UsersAdapter(this)
        listView.adapter = adapter

        prepare()
        addItem()

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

            val moveWitObjectIntent = Intent(this@MainActivity, DetailUsers::class.java)
            moveWitObjectIntent.putExtra(DetailUsers.EXTRA_USER, users[position])
            startActivity(moveWitObjectIntent)
        }
    }

    private fun prepare() {
        dataName = resources.getStringArray(R.array.name)
        dataUserName = resources.getStringArray(R.array.username)
        dataFollowers = resources.getStringArray(R.array.followers)
        dataFollowing = resources.getStringArray(R.array.following)
        dataAvatar = resources.obtainTypedArray(R.array.avatar)
        dataCompany = resources.getStringArray(R.array.company)
        dataLocation = resources.getStringArray(R.array.location)
        dataRepository = resources.getStringArray(R.array.repository)
    }

    private fun addItem() {
        for (position in dataName.indices) {
            val user = Users(
                dataUserName[position],
                dataName[position],
                dataAvatar.getResourceId(position, -1),
                    dataFollowers[position],
                    dataFollowing[position],
                    dataCompany[position],
                    dataLocation[position],
                    dataRepository[position]
            )
            users.add(user)
        }
        adapter.users = users
    }

    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }
}