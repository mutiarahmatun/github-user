package com.dicoding.mutiarahmatun.consumerapp.helper

import android.database.Cursor
import com.dicoding.mutiarahmatun.consumerapp.model.Users
import com.dicoding.mutiarahmatun.githubuser.db.DatabaseContract

object MappingHelper {

    fun cursorToArrayList(userCursor: Cursor?): ArrayList<Users> {
        val userList = ArrayList<Users>()

        userCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.COLUMN_ID))
                val username =
                    getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.COLUMN_NAME_USERNAME))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.COLUMN_NAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.COLUMN_NAME_AVATAR_URL))
                val followers = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.COLUMN_NAME_FOLLOWERS))
                val following = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.COLUMN_NAME_FOLLOWING))
                val company = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.COLUMN_NAME_COMPANY))
                val location = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.COLUMN_NAME_LOCATION))
                val repository = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.COLUMN_NAME_REPOSITORY))
                userList.add(Users(id, username, name, avatar, followers, following, company, location, repository))
            }
        }
        return userList
    }

}