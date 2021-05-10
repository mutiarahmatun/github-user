package com.dicoding.mutiarahmatun.githubuser.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dicoding.mutiarahmatun.githubuser.db.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "dbfavoriteuser"

        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_FAVORITE_USER = "CREATE TABLE $TABLE_NAME" +
                " (${DatabaseContract.FavoriteColumns.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContract.FavoriteColumns.COLUMN_NAME_USERNAME} TEXT NOT NULL," +
                " ${DatabaseContract.FavoriteColumns.COLUMN_NAME} TEXT," +
                " ${DatabaseContract.FavoriteColumns.COLUMN_NAME_AVATAR_URL} TEXT NOT NULL," +
                " ${DatabaseContract.FavoriteColumns.COLUMN_NAME_FOLLOWERS} TEXT," +
                " ${DatabaseContract.FavoriteColumns.COLUMN_NAME_FOLLOWING} TEXT," +
                " ${DatabaseContract.FavoriteColumns.COLUMN_NAME_COMPANY} TEXT," +
                " ${DatabaseContract.FavoriteColumns.COLUMN_NAME_LOCATION} TEXT," +
                " ${DatabaseContract.FavoriteColumns.COLUMN_NAME_REPOSITORY} TEXT)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_FAVORITE_USER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}