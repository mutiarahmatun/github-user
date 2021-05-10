package com.dicoding.mutiarahmatun.githubuser.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mutiarahmatun.githubuser.R
import com.dicoding.mutiarahmatun.githubuser.helper.MappingHelper
import com.dicoding.mutiarahmatun.githubuser.model.Users
import com.dicoding.mutiarahmatun.githubuser.adapter.UserFavoriteAdapter
import com.dicoding.mutiarahmatun.githubuser.databinding.ActivityFavoriteBinding
import com.dicoding.mutiarahmatun.githubuser.db.FavoriteHelper
import com.google.android.material.snackbar.Snackbar
import com.loopj.android.http.AsyncHttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var favoriteBinding: ActivityFavoriteBinding
    lateinit var adapter: UserFavoriteAdapter
    private var dummyUser = Users(0,"Empty Favorite","", "", "","","","","")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(favoriteBinding.root)

        supportActionBar?.title = "Favorite User"
        favoriteBinding.rvFavoritesUser.layoutManager = LinearLayoutManager(this)
        favoriteBinding.rvFavoritesUser.setHasFixedSize(true)

        adapter = UserFavoriteAdapter(this)

        loadUser()
        favoriteBinding.rvFavoritesUser.adapter = adapter
        favoriteBinding.rvFavoritesUser.adapter?.notifyDataSetChanged()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onResume() {

        loadUser()
        super.onResume()
    }

    private fun loadUser() {
        favoriteBinding.progressbarFavorite.visibility = View.VISIBLE
        GlobalScope.launch(Dispatchers.Main) {
            val favoriteHelper = FavoriteHelper.getInstance(applicationContext)
            favoriteHelper.open()

            try {
                val deferredFavorites = async(Dispatchers.IO) {
                    val cursor = favoriteHelper.queryAll()
                    MappingHelper.cursorToArrayList(cursor)
                }

                val favorites = deferredFavorites.await()

                if (favorites.size > 0) {

                    adapter.listFavoritesUser = favorites
                    favoriteBinding.progressbarFavorite.visibility = View.INVISIBLE
                    favoriteBinding.rvFavoritesUser.adapter?.notifyDataSetChanged()

                } else {

                    var listTemp = ArrayList<Users>()
                    listTemp.add(dummyUser)

                    favoriteBinding.progressbarFavorite.visibility = View.INVISIBLE
                    adapter.listFavoritesUser = listTemp
                    favoriteBinding.rvFavoritesUser.adapter?.notifyDataSetChanged()
                    showSnackMessage(getString(R.string.empty_favorite))
                }
            }catch (e : Exception){
                AsyncHttpClient.log.d("${this@FavoriteActivity}", e.toString())
                e.printStackTrace()
            }
            finally {
                favoriteHelper.close()
            }
        }
    }

    private fun showSnackMessage(message: String) {
        Snackbar.make(favoriteBinding.rvFavoritesUser, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            when (requestCode) {
                FavoriteActivityHelper.RESULT_DELETE -> {
                    val position = data.getIntExtra(FavoriteActivityHelper.EXTRA_POSITION, 0)
                    adapter.removeItem(position)
                    showSnackMessage(getString(R.string.success_delete))
                }
            }
        }
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