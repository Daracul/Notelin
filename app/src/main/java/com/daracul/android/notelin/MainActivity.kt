package com.daracul.android.notelin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import com.daracul.android.notelin.models.AppDataBase
import com.daracul.android.notelin.models.Note
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: NotesAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var db : AppDataBase
    val compositeDisposable = CompositeDisposable();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val note = Note("title1", "text1", Date(), Date())
        db = AppDataBase.getAppDatabase(this);
        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        viewAdapter = NotesAdapter(this)
        viewManager = LinearLayoutManager(this)
        recyclerView.layoutManager = viewManager
        recyclerView.adapter = viewAdapter
        viewAdapter.onNoteClickListener = object: NotesAdapter.OnNoteClickListener{
            override fun onNoteClick(note: Note) = showToast(note)
        }
        subcribeToDataFromDb(db)



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            R.id.action_add -> addNote(db)
            R.id.action_delete -> deleteAllFromDb(db)
            R.id.action_show_log -> loadDataFromDb(db)
        }
        return super.onOptionsItemSelected(item)
    }

    fun showToast(note: Note) {
        Toast.makeText(this@MainActivity, "${note.title}", Toast.LENGTH_SHORT).show()
    }

    private fun addNote(db: AppDataBase) {
        val randomInt = Random().nextInt(50)
        val note = Note("Title $randomInt","text$randomInt", Date(), Date())
        val disposable = Single.fromCallable({db.noteDao().insertNote(note)})
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally({Log.d("myLogs","Note $randomInt added")})
                .subscribe()
        compositeDisposable.add(disposable)
    }

    private fun loadDataFromDb(db: AppDataBase) {
        val disposable = Single.fromCallable({db.noteDao().getAll()})
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({for (note in it){
                    Log.d("myLogs","title: ${note.title} + text: ${note.text} + ${Utils.formatDateTimeAgo(this,note.createDate)}")
                }
                    Log.d("myLogs","____________________________________________________________________")
                },
                        {Log.d("myLogs","Sheet happens "+it.message)})
        compositeDisposable.add(disposable)
    }

    private fun subcribeToDataFromDb(db: AppDataBase) {
        val disposable = db.noteDao().getAllObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({for (note in it){
                    Log.d("myLogs","title: ${note.title} + text: ${note.text} + ${Utils.formatDateTimeAgo(this,note.createDate)}")
                }
                    Log.d("myLogs","____________________________________________________________________")
                    viewAdapter.swapData(it)
                },
                        {Log.d("myLogs","Sheet happens "+it.message)})
        compositeDisposable.add(disposable)
    }

    private fun deleteAllFromDb(db: AppDataBase) {
        val disposable: Disposable = Single.fromCallable { db.noteDao().deleteAllNotes() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ Log.d("myLogs", "All news deleted") },
                        { throwable: Throwable? -> Log.d("myLogs", "suka ${throwable?.message}") })
        compositeDisposable.add(disposable)
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }


}
