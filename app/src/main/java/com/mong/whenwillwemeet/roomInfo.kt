package com.mong.whenwillwemeet

import java.util.*

class RoomInfo() {
    var _roomID : String = ""
    var _admin : String  = ""
    var _password : String = ""
    var _startDate : Calendar = Calendar.getInstance()
    var _endDate : Calendar = Calendar.getInstance()
    var _location : String = ""
    var _canMulti : Boolean = true
    var _notice : String = ""
}