package com.mong.whenwillwemeet

import com.google.gson.Gson

class msgClass {
    var msg : String = ""
    var sender : String = ""
    var senderCode : String = ""
    lateinit var sendDate : dateClass

    var isMine : Boolean = false // not for Date Transfer

    fun deepCopy():msgClass {
        val JSON = Gson().toJson(this)
        return Gson().fromJson(JSON, msgClass::class.java)
    }
}