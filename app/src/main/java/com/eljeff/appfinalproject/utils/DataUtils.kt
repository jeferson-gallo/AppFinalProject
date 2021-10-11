package com.eljeff.appfinalproject.utils

import java.text.SimpleDateFormat
import java.util.*

fun getCurrentDateTime(): String {
    val sdf = SimpleDateFormat("yyyyMMddhhmmssSS")
    //Log.d("Current date", currentDate)
    return sdf.format(Date())
}
