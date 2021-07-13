package com.mong.whenwillwemeet

import java.util.*

class RoomInfo() {
    var _roomID : String = ""
    var _admin : String  = ""
    var _password : String = ""
    lateinit var _startDate : Calendar
    lateinit var _endDate : Calendar
    var _location : String = ""
    var _canMulti : Boolean = true
    var _notice : String = ""
}