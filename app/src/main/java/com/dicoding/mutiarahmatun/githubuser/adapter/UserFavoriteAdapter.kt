package com.dicoding.mutiarahmatun.githubuser.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.mutiarahmatun.githubuser.CustomOnClickListener
import com.dicoding.mutiarahmatun.githubuser.R
import com.dicoding.mutiarahmatun.githubuser.activity.FavoriteActivityHelper
import com.dicoding.mutiarahmatun.githubuser.model.Users
import com.dicoding.mutiarahmatun.githubuser.activity.TabLayout
import com.dicoding.mutiarahmatun.githubuser.databinding.ItemUsersBinding

class UserFavoriteAdapter(private val activity: Activity) : RecyclerView.Adapter<UserFavoriteAdapter.ListViewHolder>() {

    companion object {

        private const val NULL_FOLLOWERS = "This user has 0 of Follower"
        private const val NULL_FOLLOWINGS = "This user has 0 of Following"
    }

    var listFavoritesUser = ArrayList<Users>()
    set(listFavorites) {
        if (listFavorites.size > 0) {
            this.listFavoritesUser.clear()
        }
        this.listFavoritesUser.addAll(listFavorites)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val viewLayoutInflater = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_users, viewGroup, false)
        return ListViewHolder(viewLayoutInflater)
    }

    override fun getItemCount(): Int {
        return listFavoritesUser.size
    }


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val usersBinding = ItemUsersBinding.bind(itemView)

        fun bind(users: Users) {
            with(usersBinding){
                Glide.with(itemView.context)
                        .load(users.avatar)
                        .apply(RequestOptions().override(55, 55))
                        .into(imgPhoto)

                if(users.username == "Empty Favorite") {
                    txtName.text = users.name
                    txtUsername.text = users.username
                }
                else {
                    txtName.text = users.username
                    if (users.username == NULL_FOLLOWERS || users.username == NULL_FOLLOWINGS) {
                        txtUsername.text = ""
                    } else {
                        txtUsername.text = StringBuilder("@${users.username}")
                    }

                }

                itemView.setOnClickListener(CustomOnClickListener(adapterPosition, object : CustomOnClickListener.OnItemClickCallback {
                    override fun onItemClicked(view: View, position: Int) {
                        val intent = Intent(activity, FavoriteActivityHelper::class.java)
                        intent.putExtra(FavoriteActivityHelper.EXTRA_POSITION, position)
                        intent.putExtra(FavoriteActivityHelper.EXTRA_FAVORITE, users)
                        activity.startActivityForResult(intent, FavoriteActivityHelper.REQUEST_UPDATE)
                    }
                }))
            }
        }
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listFavoritesUser[position])
    }

    fun removeItem(position: Int) {
        this.listFavoritesUser.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listFavoritesUser.size)
    }

    fun addItem(users: Users) {
        this.listFavoritesUser.add(users)
        notifyItemInserted(this.listFavoritesUser.size - 1)
    }
}