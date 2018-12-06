package com.daracul.android.notelin.db

import android.util.Log
import com.daracul.android.notelin.Utils
import com.daracul.android.notelin.app.MyApp
import com.daracul.android.notelin.models.AppDataBase
import com.daracul.android.notelin.models.Note
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class Db {
    private val db : AppDataBase = AppDataBase.getAppDatabase(MyApp.context);

    fun subcribeToDataFromDb():Flowable<List<Note>> {
        return db.noteDao().getAllObservable()
    }

    fun addNote(note:Note):Completable {
        return Completable.fromAction({ db.noteDao().insertNote(note) })
    }

    fun updateDb(note:Note):Completable {
        return Completable.fromAction({ db.noteDao().updateNote(note) })
    }

    fun deleteAllFromDb() :Completable {
        return Completable.fromAction { db.noteDao().deleteAllNotes() };
    }

    fun loadDataFromDb():Single<List<Note>> {
        return Single.fromCallable({ db.noteDao().getAll() })

    }
}