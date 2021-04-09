package com.dicoding.mutiarahmatun.githubuser

import android.os.Parcel
import android.os.Parcelable

data class Users(
        var username: String?,
        var name: String?,
        var avatar: Int,
        var follower: String?,
        var following: String?,
        var company: String?,
        var location: String?,
        var repository: String?,
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeString(name)
        parcel.writeInt(avatar)
        parcel.writeString(follower)
        parcel.writeString(following)
        parcel.writeString(company)
        parcel.writeString(location)
        parcel.writeString(repository)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Users> {
        override fun createFromParcel(parcel: Parcel): Users {
            return Users(parcel)
        }

        override fun newArray(size: Int): Array<Users?> {
            return arrayOfNulls(size)
        }
    }
}