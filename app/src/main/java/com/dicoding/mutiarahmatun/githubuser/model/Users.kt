package com.dicoding.mutiarahmatun.githubuser.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName

@Parcelize
data class Users(
        var id: Int = 0,
        @SerializedName("login")
        var username: String,
        @SerializedName("name")
        var name: String?,
        @SerializedName("avatar_url")
        var avatar: String?,
        @SerializedName("followers")
        var followers: String?,
        @SerializedName("following")
        var following: String?,
        @SerializedName("company")
        var company: String?,
        @SerializedName("location")
        var location: String?,
        @SerializedName("public_repos")
        var repository: String?,
        var isFavorite: Boolean = false
) : Parcelable