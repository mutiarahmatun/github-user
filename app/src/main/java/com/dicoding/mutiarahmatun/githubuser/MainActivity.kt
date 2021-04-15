package com.dicoding.mutiarahmatun.githubuser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mutiarahmatun.githubuser.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainActivity : AppCompatActivity(){

    private var title: String = "Github User's"

    private var tempSearch = "mutiarahmatun"

    private lateinit var activityBinding: ActivityMainBinding
    private var users = mutableListOf<Users>()

    private var userAdapter = UsersAdapter(users)
    var dummyUser = Users("Please try with another username","Sorry, this username could not been find", "", "","","","","")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setActionBarTitle(title)

        activityBinding.rvUsers.setHasFixedSize(true)
        activityBinding.rvUsers.adapter = userAdapter
        activityBinding.rvUsers.layoutManager = LinearLayoutManager(this)
        activityBinding.progressBar.visibility = View.VISIBLE
        getUsers()

        userAdapter.setOnItemClickCallback(object : UsersAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Users) {
                getDetailsUser(data)
            }
        })
    }

    private fun getUsers() {

        activityBinding.progressBar.visibility = View.VISIBLE

        val client = AsyncHttpClient()

        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/search/users?q=$tempSearch"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {

                // Jika koneksi berhasil
                activityBinding.progressBar.visibility = View.INVISIBLE

                // Parsing JSON
                val result = String(responseBody)
                getListUsers(result)

            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                // Jika koneksi gagal
                activityBinding.progressBar.visibility = View.INVISIBLE

                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getListUsers(response: String) {
        val listUser = ArrayList<Users>()

        try{
            val responseObject = JSONObject(response)
            val dataArray = responseObject.getJSONArray("items")

            val gson = Gson()
            for(i in 0 until dataArray.length()){
                val dataObject = dataArray.getJSONObject(i)
                val data = gson.fromJson(dataObject.toString(), Users::class.java)
                listUser.add(data)
            }

            if (listUser.size == 0){
                users.clear()
                users.add(dummyUser)
            }
            else if(users.size == 0 )users.addAll(listUser)
            else{
                users.clear()
                users.addAll(listUser)
            }
            showRecyclerList(users)
        } catch (e: Exception) {
            Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun showRecyclerList(users: MutableList<Users>) {
        userAdapter = UsersAdapter(users)
        activityBinding.rvUsers.adapter?.notifyDataSetChanged();

    }

    private fun getDetailsUser(detailUsers: Users) {

        val clientDetail = AsyncHttpClient()
        clientDetail.addHeader("Authorization", "token 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")
        clientDetail.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/${detailUsers.username}"

        clientDetail.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                // Parsing JSON
                val resultDetail = String(responseBody)

                val gson = Gson()
                val dataObject = JSONObject(resultDetail)
                val newUser = gson.fromJson(dataObject.toString(), Users::class.java)
                showSelectedUser(newUser)
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                // Jika koneksi gagal
                activityBinding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showSelectedUser(users: Users) {
        val moveWithObjectIntent = Intent(this@MainActivity, TabLayout::class.java)
        moveWithObjectIntent.putExtra(TabLayout.EXTRA_USER, users)
        startActivity(moveWithObjectIntent)
    }

    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val manageSearch = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val viewSearch = menu.findItem(R.id.search).actionView as SearchView

        viewSearch.setSearchableInfo(manageSearch.getSearchableInfo(componentName))
        viewSearch.queryHint = resources.getString(R.string.hint)
        viewSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            //method ini ketika search selesai atau OK
            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                tempSearch = query
                getUsers()
                return true
            }

            // method ini untuk merespon tiap perubahan huruf pada searchView
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_settings) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }
}