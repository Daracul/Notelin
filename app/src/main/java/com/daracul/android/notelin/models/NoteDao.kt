package com.daracul.android.notelin.models

import android.arch.persistence.room.*
import io.reactivex.Flowable
import io.reactivex.Observable

@Dao
interface NoteDao {

    @Query("SELECT * FROM note")
    fun  getAll() : List<Note>

    @Query("SELECT * FROM note ORDER BY created_at DESC")
    fun  getAllObservable() : Flowable<List<Note>>

    @Query("SELECT * FROM note WHERE id = :id")
    fun loadNoteById(id:Int):Note

    @Insert
    fun insertNote(note:Note)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateNote(note: Note)

    @Delete
    fun deleteNote(note: Note)

    @Query("DELETE FROM note")
    fun deleteAllNotes()

}