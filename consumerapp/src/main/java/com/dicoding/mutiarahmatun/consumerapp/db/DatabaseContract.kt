package com.dicoding.mutiarahmatun.githubuser.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.dicoding.mutiarahmatun.githubuser"
    const val SCHEME = "content"

    internal class FavoriteColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite_user"
            const val COLUMN_ID = "_id"
            const val COLUMN_NAME_USERNAME = "username"
            const val COLUMN_NAME = "name"
            const val COLUMN_NAME_AVATAR_URL = "avatar_url"
            const val COLUMN_NAME_FOLLOWERS = "followers"
            const val COLUMN_NAME_FOLLOWING = "following"
            const val COLUMN_NAME_COMPANY = "company"
            const val COLUMN_NAME_LOCATION = "location"
            const val COLUMN_NAME_REPOSITORY = "public_repos"
            const val COLUMN_FAVORITE = "is_favorite"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                    .authority(AUTHORITY)
                    .appendPath(TABLE_NAME)
                    .build()

        }
    }
}