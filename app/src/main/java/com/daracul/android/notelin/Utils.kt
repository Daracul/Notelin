package com.daracul.android.notelin

import java.text.SimpleDateFormat
import java.util.*

object Utils {
     fun formatDate(date: Date?):String{
        val simpleDateFormat = SimpleDateFormat("'time:'HH:mm 'date:'dd.MM.yyyy", Locale.getDefault())
        return simpleDateFormat.format(date)
    }
}