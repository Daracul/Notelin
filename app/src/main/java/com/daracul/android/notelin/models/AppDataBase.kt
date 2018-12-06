package com.daracul.android.notelin.models

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = arrayOf(Note::class), version = 1)
abstract class AppDataBase : RoomDatabase(){
    abstract fun noteDao(): NoteDao
    companion object {
        private val DATABASE_NAME = "note.Db"

        fun getAppDatabase (context : Context):AppDataBase {
            return Room.databaseBuilder(context.applicationContext,
                    AppDataBase::class.java,
                    DATABASE_NAME).build()
        }
    }




}