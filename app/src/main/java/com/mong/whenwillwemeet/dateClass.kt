package com.mong.whenwillwemeet

import java.util.*

class dateClass {
    var day : Int = 0
    var month : Int = 0
    var year : Int = 0

    constructor(){

    }

    constructor(c : Calendar){
        day = c.get(Calendar.DAY_OF_MONTH)
        month = c.get(Calendar.MONTH)
        year = c.get(Calendar.YEAR)
    }
}