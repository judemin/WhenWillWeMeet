package com.mong.whenwillwemeet

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class roomInfo() : Parcelable{
    var _roomID : String = ""
    var _admin : String  = ""
    var _password : String = ""
    lateinit  var _startDate : dateClass
    lateinit var _endDate : dateClass
    var _location : String = ""
    var _canMulti : Boolean = true
    var _notice : String = ""

    constructor(parcel: Parcel) : this() {
        _roomID = parcel.readString().toString()
        _admin = parcel.readString().toString()
        _password = parcel.readString().toString()
        _location = parcel.readString().toString()
        _canMulti = parcel.readByte() != 0.toByte()
        _notice = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_roomID)
        parcel.writeString(_admin)
        parcel.writeString(_password)
        parcel.writeString(_location)
        parcel.writeByte(if (_canMulti) 1 else 0)
        parcel.writeString(_notice)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<roomInfo> {
        override fun createFromParcel(parcel: Parcel): roomInfo {
            return roomInfo(parcel)
        }

        override fun newArray(size: Int): Array<roomInfo?> {
            return arrayOfNulls(size)
        }
    }
}