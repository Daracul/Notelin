package com.daracul.android.notelin.mvp

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.daracul.android.notelin.Utils
import com.daracul.android.notelin.app.MyApp
import com.daracul.android.notelin.models.AppDataBase
import com.daracul.android.notelin.models.Note
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

@InjectViewState
class MainPresenter: MvpPresenter<MainView>() {
    private lateinit var db : AppDataBase
    val compositeDisposable = CompositeDisposable();

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        db = AppDataBase.getAppDatabase(MyApp.context)
        subcribeToDataFromDb()
    }

    private fun subcribeToDataFromDb() {
        val disposable = db.noteDao().getAllObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({for (note in it){
                    Log.d("myLogs","title: ${note.title} + text: ${note.text} + ${Utils.formatDateTimeAgo(MyApp.context,note.createDate)}")
                }
                    Log.d("myLogs","____________________________________________________________________")
                    viewState.onNotesLoaded(it)
                },
                        { Log.d("myLogs","Sheet happens "+it.message)})
        compositeDisposable.add(disposable)
    }

     fun addNote() {
        val randomInt = Random().nextInt(50)
        val note = Note("Title $randomInt","text$randomInt", Date(), Date())
        val disposable = Single.fromCallable({db.noteDao().insertNote(note)})
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally({Log.d("myLogs","Note $randomInt added")})
                .subscribe()
        compositeDisposable.add(disposable)
    }

     fun loadDataFromDb() {
        val disposable = Single.fromCallable({db.noteDao().getAll()})
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({for (note in it){
                    Log.d("myLogs","title: ${note.title} + text: ${note.text} + ${Utils.formatDateTimeAgo(MyApp.context,note.createDate)}")
                }
                    Log.d("myLogs","____________________________________________________________________")
                },
                        {Log.d("myLogs","Sheet happens "+it.message)})
        compositeDisposable.add(disposable)
    }



     fun deleteAllFromDb() {
        val disposable: Disposable = Single.fromCallable { db.noteDao().deleteAllNotes() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ Log.d("myLogs", "All news deleted") },
                        { throwable: Throwable? -> Log.d("myLogs", "suka ${throwable?.message}") })
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }



}