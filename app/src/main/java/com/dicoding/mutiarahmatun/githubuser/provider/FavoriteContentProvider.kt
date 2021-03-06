package com.dicoding.mutiarahmatun.githubuser.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.dicoding.mutiarahmatun.githubuser.db.DatabaseContract.AUTHORITY
import com.dicoding.mutiarahmatun.githubuser.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.dicoding.mutiarahmatun.githubuser.db.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.dicoding.mutiarahmatun.githubuser.db.FavoriteHelper
import com.loopj.android.http.AsyncHttpClient

class FavoriteContentProvider : ContentProvider() {

    companion object {
        private const val FAV = 1
        private const val FAV_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favoriteHelper: FavoriteHelper

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAV)

            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", FAV_ID)
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (FAV_ID) {
            sUriMatcher.match(uri) -> favoriteHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (FAV) {
            sUriMatcher.match(uri) -> favoriteHelper.insert(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun onCreate(): Boolean {
        try {
            favoriteHelper = FavoriteHelper.getInstance(context as Context)
            favoriteHelper.open()
        }catch (e : Exception){
            AsyncHttpClient.log.d("CONTENT PROVIDER - QUERY", "enter exception $e")
            e.printStackTrace()
        }
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        AsyncHttpClient.log.d("CONTENT PROVIDER - QUERY", "enter query ${sUriMatcher.match(uri)}")
        try {
            favoriteHelper = FavoriteHelper.getInstance(context as Context)
            favoriteHelper.open()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return when (sUriMatcher.match(uri)) {
            FAV -> favoriteHelper.queryAll()
            FAV_ID -> favoriteHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        val updated: Int = when (FAV_ID) {
            sUriMatcher.match(uri) -> favoriteHelper.update(uri.lastPathSegment.toString(),values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated
    }
}