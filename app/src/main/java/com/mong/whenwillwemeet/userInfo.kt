package com.mong.whenwillwemeet

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class userInfo() : Parcelable {
    var pid : String = "ABCDEFGHI"
    var name : String = "tmp"
    var selectedDates : MutableMap<String, dateClass> = mutableMapOf()

    constructor(parcel: Parcel) : this() {
        pid = parcel.readString().toString()
        name = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(pid)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<userInfo> {
        override fun createFromParcel(parcel: Parcel): userInfo {
            return userInfo(parcel)
        }

        override fun newArray(size: Int): Array<userInfo?> {
            return arrayOfNulls(size)
        }
    }
}