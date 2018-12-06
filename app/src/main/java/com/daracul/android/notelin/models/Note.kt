package com.daracul.android.notelin.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import java.io.Serializable
import java.util.*

@Entity(tableName = "note")
@TypeConverters(DateConverter::class)
data class Note(var title: String?,
                var text: String?,
                @ColumnInfo(name = "created_at") var createDate: Date?,
                @ColumnInfo(name = "changed_at") var changeDate: Date?) :Serializable{
    @PrimaryKey(autoGenerate = true) var id: Int = 0


    fun getInfo():String = "Name:\n$title\nCreated:\n$createDate\nChanged:\n$changeDate"

}