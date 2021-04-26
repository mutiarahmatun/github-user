package com.dicoding.mutiarahmatun.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.mutiarahmatun.githubuser.model.Users
import com.dicoding.mutiarahmatun.githubuser.databinding.ItemUsersBinding

class UsersAdapter (private val listUsers: MutableList<Users>) : RecyclerView.Adapter<UsersAdapter.ListViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    companion object {

        private const val NULL_FOLLOWERS = "This user has 0 of Follower"
        private const val NULL_FOLLOWINGS = "This user has 0 of Following"
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val viewBinding = ItemUsersBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(viewBinding)
    }

    override fun getItemCount(): Int {
        return listUsers.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUsers[position])
    }

    inner class ListViewHolder(private val viewBinding: ItemUsersBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(users: Users) {
            with(viewBinding){
                Glide.with(itemView.context)
                        .load(users.avatar)
                        .apply(RequestOptions().override(55, 55))
                        .into(imgPhoto)

                if(users.username == "Please try with another username") {
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

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(users) }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Users)
    }
}