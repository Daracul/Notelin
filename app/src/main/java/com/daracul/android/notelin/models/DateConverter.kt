package com.daracul.android.notelin.models

import android.arch.persistence.room.TypeConverter
import java.util.*

class DateConverter {

    @TypeConverter
    fun toDate (timeInLongs:Long?):Date?{
        return if (timeInLongs == null) null else Date(timeInLongs)
    }

    @TypeConverter
    fun toTimeInLongs(date:Date):Long?{
        return date?.time
    }
}