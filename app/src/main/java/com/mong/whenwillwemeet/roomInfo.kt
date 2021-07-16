package com.mong.whenwillwemeet

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class RoomInfo() : Parcelable{
    var _roomID : String = ""
    var _admin : String  = ""
    var _password : String = ""
    var _startDate : Calendar = Calendar.getInstance()
    var _endDate : Calendar = Calendar.getInstance()
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

    companion object CREATOR : Parcelable.Creator<RoomInfo> {
        override fun createFromParcel(parcel: Parcel): RoomInfo {
            return RoomInfo(parcel)
        }

        override fun newArray(size: Int): Array<RoomInfo?> {
            return arrayOfNulls(size)
        }
    }
}