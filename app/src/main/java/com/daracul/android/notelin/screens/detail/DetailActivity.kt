package com.daracul.android.notelin.screens.detail

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.arellomobile.mvp.MvpAppCompatActivity
import android.content.Intent
import android.app.Activity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.daracul.android.notelin.R
import com.daracul.android.notelin.Utils
import com.daracul.android.notelin.db.Db
import com.daracul.android.notelin.models.Note
import com.daracul.android.notelin.screens.detail.mvp.DetailPresenter
import com.daracul.android.notelin.screens.detail.mvp.DetailView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*


class DetailActivity:MvpAppCompatActivity(),DetailView {
    private lateinit var titleEditText:EditText
    private lateinit var dateTextView:TextView
    private lateinit var descriptionEditText:EditText
    @InjectPresenter
    lateinit var presenter:DetailPresenter

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
        if (intent.hasExtra(ID_KEY)){
            presenter.setNote(intent.getSerializableExtra(ID_KEY) as Note)
        } else presenter.isNewNote = true

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_add_detail -> presenter.doDbChanges(titleEditText.text.toString(),
                    descriptionEditText.text.toString())
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setupUI() {
        titleEditText = findViewById(R.id.etTitle)
        dateTextView = findViewById(R.id.tvNoteDate)
        descriptionEditText = findViewById(R.id.etText)
    }

    override fun setData(note: Note) {
        titleEditText.setText(note.title)
        dateTextView.setText(Utils.formatDateTimeAgo(note.createDate))
        descriptionEditText.setText(note.text)
    }

    override fun showToast(text: String) {
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
    }

    override fun setActionBar(text: String) {
        supportActionBar?.setTitle(text);
    }

    override fun finishActivity() {
        finish()
    }
}