package com.daracul.android.notelin

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.daracul.android.notelin.app.MyApp
import com.daracul.android.notelin.models.AppDataBase
import com.daracul.android.notelin.models.Note
import com.daracul.android.notelin.mvp.MainPresenter
import com.daracul.android.notelin.mvp.MainView
import java.util.*

class MainActivity : MvpAppCompatActivity(), MainView {

    @InjectPresenter
    lateinit var presenter: MainPresenter
    private lateinit var recyclerView: RecyclerView
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
            override fun onNoteClick(note: Note) = showToast(note)
        }
    }

    private fun setupUI() {
        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val fab:FloatingActionButton = findViewById(R.id.fab_button)
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

    fun showToast(note: Note) {
        Toast.makeText(this@MainActivity, "${note.title}", Toast.LENGTH_SHORT).show()
    }



    override fun onNotesLoaded(notes: List<Note>) {
        viewAdapter.swapData(notes)
    }

    override fun updateView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSearchResult(notes: List<Note>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAllNotesDeleted() {

    }

    override fun onNoteDeleted() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showNoteInfoDialog(noteInfo: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideNoteInfoDialog() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showNoteDeleteDialog(notePosition: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideNoteDeleteDialog() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showNoteContextDialog(notePosition: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideNoteContextDialog() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
