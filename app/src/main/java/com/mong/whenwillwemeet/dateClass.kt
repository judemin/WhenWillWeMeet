package com.mong.whenwillwemeet

import java.time.Month
import java.util.*

class dateClass {
    var day : Int = 0
    var month : Int = 0
    var year : Int = 0
    var hour : Int = 0;
    var min : Int = 0;

    constructor() {}

    constructor(c : Calendar){
        day = c.get(Calendar.DAY_OF_MONTH)
        month = c.get(Calendar.MONTH)
        year = c.get(Calendar.YEAR)
        hour = c.get(Calendar.HOUR)
        min = c.get(Calendar.MINUTE)
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
}