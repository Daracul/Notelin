package com.daracul.android.notelin.mvp

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.daracul.android.notelin.Utils
import com.daracul.android.notelin.app.MyApp
import com.daracul.android.notelin.db.Db
import com.daracul.android.notelin.models.AppDataBase
import com.daracul.android.notelin.models.Note
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {
    private lateinit var db: Db
    val compositeDisposable = CompositeDisposable();

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        db = Db()
        subcribeToDataFromDb()
    }

    private fun subcribeToDataFromDb() {
        val disposable = db.subcribeToDataFromDb()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ Log.d("myLogs","SUBCRIPTION WORKS!");viewState.onNotesLoaded(it) },
                        { error -> handle(error)})
        compositeDisposable.add(disposable)
    }

    private fun handle(throwable: Throwable?) {
        viewState.showError(throwable)
    }

    fun addNote() {
        val randomInt = Random().nextInt(50)
        val note = Note("Title $randomInt", "text$randomInt", Date(), Date())
        val disposable = db.addNote(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        compositeDisposable.add(disposable)
    }

    fun loadDataFromDb() {
        val disposable = db.loadDataFromDb()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    for (note in it) {
                        Log.d("myLogs", "title: ${note.title} + text: ${note.text} + ${Utils.formatDateTimeAgo(note.createDate)}")
                    }
                    Log.d("myLogs", "____________________________________________________________________")
                }, { handle(it) })
        compositeDisposable.add(disposable)
    }

    fun updateDb(note:Note) {
        val disposable = db.updateDb(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, { handle(it) })
        compositeDisposable.add(disposable)
    }


    fun deleteAllFromDb() {
        val disposable: Disposable = db.deleteAllFromDb()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ viewState.showMessage("All notes has been deleted") },
                        { handle(it) })
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }


}