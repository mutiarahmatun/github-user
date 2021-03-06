package com.dicoding.mutiarahmatun.githubuser.fragment

import android.content.ContentValues
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.mutiarahmatun.githubuser.helper.MappingHelper
import com.dicoding.mutiarahmatun.githubuser.R
import com.dicoding.mutiarahmatun.githubuser.model.Users
import com.dicoding.mutiarahmatun.githubuser.databinding.FragmentDetailUserBinding
import com.dicoding.mutiarahmatun.githubuser.db.DatabaseContract
import com.dicoding.mutiarahmatun.githubuser.db.FavoriteHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DetailUserFragment (detailsUser: Users) : Fragment(R.layout.fragment_detail_user) {

    private var _detailBinding: FragmentDetailUserBinding? = null
    private val detailBinding: FragmentDetailUserBinding get() = requireNotNull(_detailBinding)
    private var users: Users = detailsUser

    private lateinit var favoriteHelper: FavoriteHelper
    var usernameTemp = detailsUser.username


    private fun setStatusFavorite(status: Boolean) {
        GlobalScope.launch(Dispatchers.Main) {
            val thisContext = requireContext()
            favoriteHelper = FavoriteHelper.getInstance(thisContext)
            favoriteHelper.open()

            try {
                if (status) {
                    detailBinding.favoriteButton.setImageResource(R.drawable.icon_favorite_filled)

                    var dataTemp = dataFavoriteUser()
                    val insertResult = favoriteHelper.insert(dataTemp)
                    if (insertResult > 0) {
                        Toast.makeText(thisContext, "Success add to Favorite", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(thisContext, "Failed add to Favorite", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    detailBinding.favoriteButton.setImageResource(R.drawable.icon_favorite_unfilled)

                    val deleteResult = favoriteHelper.deleteByUserName(usernameTemp).toLong()
                    if (deleteResult > 0) {
                        Toast.makeText(thisContext, "Success delete from Favorite", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(thisContext, "Failed delete from Favorite", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e : Exception){

                e.printStackTrace()

            } finally {
                favoriteHelper.close()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _detailBinding = FragmentDetailUserBinding.inflate(inflater, container, false)

        return detailBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _detailBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(this)
            .load(users.avatar)
            .apply(RequestOptions().override(550, 550))
            .into(detailBinding.imgAvatarReceived)
        detailBinding.tvUsernameReceived.text = StringBuilder("@${users.username}")
        detailBinding.tvNameReceived.text = users.name?: users.username
        detailBinding.tvLocationReceived.text = users.location?: "unknown location"
        detailBinding.tvCompanyReceived.text = users.company?: "unknown company"
        detailBinding.tvRepoReceived.text = users.repository
        detailBinding.tvFollowersReceived.text = users.followers
        detailBinding.tvFollowingReceived.text = users.following
        detailBinding.repository.text = getString(R.string.tag_repo)
        detailBinding.followers.text = getString(R.string.tag_followers)
        detailBinding.following.text = getString(R.string.tag_following)

        var statusFavorite =users.isFavorite

        GlobalScope.launch(Dispatchers.Main) {
            val thisContext = requireContext()
            favoriteHelper = FavoriteHelper.getInstance(thisContext)
            favoriteHelper.open()

            try {
                val deferredFavorite = async(Dispatchers.IO) {
                    val cursor = favoriteHelper.queryByUsername(usernameTemp)
                    MappingHelper.cursorToArrayList(cursor)
                }

                val favorite = deferredFavorite.await()
                if (favorite.size > 0) {
                    users.isFavorite = true
                    statusFavorite = true
                    detailBinding.favoriteButton.setImageResource(R.drawable.icon_favorite_filled)
                }
            }catch (e : Exception){
                e.printStackTrace()
            }finally {
                favoriteHelper.close()
            }
        }

        detailBinding.favoriteButton.setOnClickListener{
            statusFavorite = !statusFavorite
            setStatusFavorite(statusFavorite)
            users.isFavorite = statusFavorite
        }
    }

    private fun dataFavoriteUser() : ContentValues {
        val data = ContentValues()
        data.put(DatabaseContract.FavoriteColumns.COLUMN_NAME_USERNAME, users.username)
        data.put(DatabaseContract.FavoriteColumns.COLUMN_NAME, users.name)
        data.put(DatabaseContract.FavoriteColumns.COLUMN_NAME_AVATAR_URL, users.avatar)
        data.put(DatabaseContract.FavoriteColumns.COLUMN_NAME_FOLLOWERS, users.followers)
        data.put(DatabaseContract.FavoriteColumns.COLUMN_NAME_FOLLOWING, users.following)
        data.put(DatabaseContract.FavoriteColumns.COLUMN_NAME_COMPANY, users.company)
        data.put(DatabaseContract.FavoriteColumns.COLUMN_NAME_LOCATION, users.location)
        data.put(DatabaseContract.FavoriteColumns.COLUMN_NAME_REPOSITORY, users.repository)

        return data
    }

}