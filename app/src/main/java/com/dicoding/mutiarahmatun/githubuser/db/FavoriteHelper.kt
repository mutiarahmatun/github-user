package com.dicoding.mutiarahmatun.githubuser.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dicoding.mutiarahmatun.githubuser.db.DatabaseContract.FavoriteColumns.Companion.COLUMN_ID
import com.dicoding.mutiarahmatun.githubuser.db.DatabaseContract.FavoriteColumns.Companion.COLUMN_NAME_USERNAME
import com.dicoding.mutiarahmatun.githubuser.db.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import java.sql.SQLException

class FavoriteHelper(context: Context) {
    private var dataBaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME

        private var INSTANCE: FavoriteHelper? = null
        fun getInstance(context: Context): FavoriteHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavoriteHelper(context)
            }
    }

    init {
        dataBaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_ID ASC")
    }

    fun queryByUsername(username: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$COLUMN_NAME_USERNAME = ?",
            arrayOf(username),
            null,
            null,
            null,
            null)
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun update(username: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$COLUMN_NAME_USERNAME = ?", arrayOf(username))
    }

    fun deleteByUserName(username: String?): Int {
        return database.delete(DATABASE_TABLE, "$COLUMN_NAME_USERNAME = '$username'", null)
    }

}