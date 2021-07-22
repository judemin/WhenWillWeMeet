package com.mong.whenwillwemeet

import java.time.Month
import java.util.*

class dateClass {
    var day : Int = 0
    var month : Int = 0
    var year : Int = 0

    constructor() {}

    constructor(year : Int, month: Int, day : Int){
        this.day = day
        this.month = month
        this.year = year
    }

    constructor(c : Calendar){
        day = c.get(Calendar.DAY_OF_MONTH)
        month = c.get(Calendar.MONTH)
        year = c.get(Calendar.YEAR)
    }
}