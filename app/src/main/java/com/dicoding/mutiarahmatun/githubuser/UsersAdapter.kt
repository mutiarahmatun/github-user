package com.dicoding.mutiarahmatun.githubuser

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import de.hdodenhof.circleimageview.CircleImageView

class UsersAdapter internal constructor(private val context: Context) : BaseAdapter() {
    internal var users = arrayListOf<Users>()

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        var itemView = view
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_users, viewGroup, false)
        }
        val viewHolder = ViewHolder(itemView as View)
        val users = getItem(position) as Users
        viewHolder.bind(users)
        return itemView
    }

    override fun getItem(i: Int): Any {
        return users[i]
    }
    override fun getItemId(i: Int): Long {
        return i.toLong()
    }
    override fun getCount(): Int {
        return users.size
    }

    private inner class ViewHolder internal constructor(view: View) {
        private val txtName: TextView = view.findViewById(R.id.txt_name)
        private val txtDescription: TextView = view.findViewById(R.id.txt_description)
        private val following: TextView = view.findViewById(R.id.following)
        private val followers: TextView = view.findViewById(R.id.followers)
        private val imgPhoto: CircleImageView = view.findViewById(R.id.img_photo)
        internal fun bind(users: Users) {
            txtName.text = users.name
            txtDescription.text = users.username
            imgPhoto.setImageResource(users.avatar)
            following.text = users.following + " following"
            followers.text = users.follower + " followers"
        }
    }
}