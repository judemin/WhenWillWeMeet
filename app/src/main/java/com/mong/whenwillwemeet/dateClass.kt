package com.mong.whenwillwemeet

import com.google.gson.Gson
import java.time.Month
import java.util.*

class dateClass {

    var day : Int = 0
    var dayOfWeek : Int = 0
    var month : Int = 0 // 사용할 때 month에 1 더해야 함
    var year : Int = 0
    var hour : Int = 0
    var min : Int = 0
    var selectedNum : Int = 0

    constructor() {}

    constructor(c : Calendar){
        day = c.get(Calendar.DAY_OF_MONTH)
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK)
        month = c.get(Calendar.MONTH)
        year = c.get(Calendar.YEAR)
        hour = c.get(Calendar.HOUR)
        min = c.get(Calendar.MINUTE)
        selectedNum = 0
    }

    fun isSameDateClass(src : dateClass, dest : dateClass) : Boolean{
        if(src.year != dest.year)
            return false
        if(src.month != dest.month)
            return false
        if(src.day != dest.day)
            return false
        return true
    }

    fun deepCopy():dateClass {
        val JSON = Gson().toJson(this)
        return Gson().fromJson(JSON, dateClass::class.java)
    }

    fun makeKey():String{
        return "${this.year}${this.month}${this.day}"
    }

    companion object {
        val dayofweek = arrayListOf<String>("일","월","화","수","목","금","토")
    }
}