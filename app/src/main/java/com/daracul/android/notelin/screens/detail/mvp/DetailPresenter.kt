package com.daracul.android.notelin.screens.detail.mvp

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.daracul.android.notelin.db.Db
import com.daracul.android.notelin.models.Note
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

@InjectViewState
class DetailPresenter (var isNewNote: Boolean = false): MvpPresenter<DetailView>() {
    private lateinit var note: Note
    private lateinit var db: Db
    private val compositeDisposable = CompositeDisposable()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        db = Db()
        var titleText:String = "Create note"
        if (!isNewNote){
            viewState.setData(note)
            titleText = "Edit note"
        }
        viewState.setActionBar(titleText)
        Log.d("myLogs","Is it new note?  $isNewNote")
    }

    fun setNote(note: Note) {
        this.note = note;
    }
    
    private fun updateDatabase() {
        Log.d("myLogs","Updating db ... \n${note.title}")
        val disposable = db.updateDb(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ viewState.showToast("Note updates") }, { })
        compositeDisposable.add(disposable)
    }

    private fun addToDatabase() {
        val disposable = db.addNote(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ viewState.showToast("Note created") }, { })
        compositeDisposable.add(disposable)
    }

    fun doDbChanges(title:String,text:String){
        if (isNewNote) {
            note = Note(title,text, Date(), Date())
            addToDatabase()
        } else {
            note.title=title
            note.text=text
            updateDatabase()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

}