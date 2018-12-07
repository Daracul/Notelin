package com.daracul.android.notelin.screens.list

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.daracul.android.notelin.R
import com.daracul.android.notelin.models.Note
import com.daracul.android.notelin.screens.detail.DetailActivity
import com.daracul.android.notelin.screens.list.mvp.MainPresenter
import com.daracul.android.notelin.screens.list.mvp.MainView

class MainActivity : MvpAppCompatActivity(), MainView {

    @InjectPresenter
    lateinit var presenter: MainPresenter
    private lateinit var recyclerView: RecyclerView
    lateinit var fab:FloatingActionButton
    private lateinit var viewAdapter: NotesAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupUI()
        setupUX()

    }

    fun setupUX() {
        viewAdapter.onNoteClickListener = object : NotesAdapter.OnNoteClickListener {
            override fun onNoteClick(note: Note) = DetailActivity.start(this@MainActivity,note)
        }
        fab.setOnClickListener { run { DetailActivity.start(this@MainActivity) } }
    }

    private fun setupUI() {
        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        fab = findViewById(R.id.fab_button)
        viewAdapter = NotesAdapter(this)
        viewManager = LinearLayoutManager(this)
        recyclerView.layoutManager = viewManager
        recyclerView.adapter = viewAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            R.id.action_add -> presenter.addNote()
            R.id.action_delete -> presenter.deleteAllFromDb()
            R.id.action_show_log -> presenter.loadDataFromDb()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onNotesLoaded(notes: List<Note>) {
        viewAdapter.swapData(notes)
    }

    override fun showError(throwable: Throwable?) {
        Log.d("myLogs","${throwable?.javaClass?.simpleName} ${throwable?.message}")
    }

    override fun showMessage(text: String) {
        Toast.makeText(this,text, Toast.LENGTH_SHORT).show()
    }
}
