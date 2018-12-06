package com.daracul.android.notelin

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.EditText
import android.widget.TextView
import com.arellomobile.mvp.MvpAppCompatActivity
import android.content.Intent
import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.arellomobile.mvp.presenter.InjectPresenter
import com.daracul.android.notelin.app.MyApp
import com.daracul.android.notelin.db.Db
import com.daracul.android.notelin.models.AppDataBase
import com.daracul.android.notelin.models.Note
import com.daracul.android.notelin.mvp.MainPresenter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*


class DetailActivity:MvpAppCompatActivity() {
    private lateinit var titleEditText:EditText
    private lateinit var dateTextView:TextView
    private lateinit var descriptionEditText:EditText
    private lateinit var note:Note
    private lateinit var db: Db
    private val compositeDisposable = CompositeDisposable()


    companion object {
        private val ID_KEY = "id:key"
         fun start(activity: Activity, note:Note? = null) {
            val noteListIntent = Intent(activity, DetailActivity::class.java)
             if (note!=null){
                 noteListIntent.putExtra(ID_KEY, note)
             }
             activity.startActivity(noteListIntent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setupUI()
        db =Db()
        if (intent.hasExtra(ID_KEY)){
            supportActionBar?.setTitle("Edit");
            note = intent.getSerializableExtra(ID_KEY) as Note
            setDate(note)
        } else supportActionBar?.setTitle("Create note")
    }

    private fun setDate(note: Note) {
        titleEditText.setText(note.title)
        dateTextView.setText(Utils.formatDateTimeAgo(note.createDate))
        descriptionEditText.setText(note.text)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_add_detail -> addToDatabase()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addToDatabase() {
        val title = titleEditText.text.toString()
        val text = descriptionEditText.text.toString()
        if (intent.hasExtra(ID_KEY)){
            note.text = text
            note.title = title
            val disposable = db.updateDb(note)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({finish()}, {  })
            compositeDisposable.add(disposable)
        } else{
            note = Note(title,text, Date(),Date())
            val disposable = db.addNote(note)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({finish()}, {  })
            compositeDisposable.add(disposable)
        }

    }


    private fun setupUI() {
        titleEditText = findViewById(R.id.etTitle)
        dateTextView = findViewById(R.id.tvNoteDate)
        descriptionEditText = findViewById(R.id.etText)
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }


}