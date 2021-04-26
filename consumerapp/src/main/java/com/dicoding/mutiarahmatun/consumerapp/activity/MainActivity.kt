package com.dicoding.mutiarahmatun.consumerapp.activity

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mutiarahmatun.consumerapp.adapter.UserFavoriteAdapter
import com.dicoding.mutiarahmatun.consumerapp.databinding.ActivityMainBinding
import com.dicoding.mutiarahmatun.consumerapp.helper.MappingHelper
import com.dicoding.mutiarahmatun.consumerapp.model.Users
import com.dicoding.mutiarahmatun.githubuser.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(){

    private lateinit var activityBinding: ActivityMainBinding
    lateinit var adapter: UserFavoriteAdapter
    private var dummyUser = Users(0,"Empty Favorite","", "", "","","","","")

    companion object {
        private const val EXTRA_STATE = "extra_state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)

        supportActionBar?.title = "Favorite"
        activityBinding.rvUsers.layoutManager = LinearLayoutManager(this)
        activityBinding.rvUsers.setHasFixedSize(true)

        adapter = UserFavoriteAdapter(this)
        activityBinding.rvUsers.adapter = adapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadUser()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadUser()
        } else {
            savedInstanceState.getParcelableArrayList<Users>(EXTRA_STATE)?.also { adapter.listFavoritesUser = it }
        }

    }

    private fun loadUser() {
        activityBinding.progressBar.visibility = View.VISIBLE
        GlobalScope.launch(Dispatchers.Main) {

            try {
                val deferredFavorites = async(Dispatchers.IO) {
                    val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                    MappingHelper.cursorToArrayList(cursor)
                }

                val favorites = deferredFavorites.await()

                if (favorites.size > 0) {
                    adapter.listFavoritesUser = favorites
                } else {

                    var listTemp = ArrayList<Users>()
                    listTemp.add(dummyUser)

                    adapter.listFavoritesUser = listTemp

                    showSnackMessage("Tidak terdapat data saat ini")
                }

                activityBinding.progressBar.visibility = View.INVISIBLE
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
    }

    private fun showSnackMessage(message: String) {
        Snackbar.make(activityBinding.rvUsers, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavoritesUser)
    }
}