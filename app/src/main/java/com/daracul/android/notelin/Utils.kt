package com.daracul.android.notelin

import android.content.Context
import android.text.format.DateUtils
import com.daracul.android.notelin.app.MyApp
import java.text.SimpleDateFormat
import java.util.*

object Utils {
     fun formatDate(date: Date?):String{
        val simpleDateFormat = SimpleDateFormat("'time:'HH:mm 'date:'dd.MM.yyyy", Locale.getDefault())
        return simpleDateFormat.format(date)
    }

    fun formatDateTimeAgo (date: Date?):CharSequence{
        return DateUtils.getRelativeDateTimeString(
                MyApp.context,
                date!!.time,
                DateUtils.HOUR_IN_MILLIS,
                2*DateUtils.DAY_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_RELATIVE);

    }
}