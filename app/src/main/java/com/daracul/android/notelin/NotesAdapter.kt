package com.daracul.android.notelin

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.daracul.android.notelin.models.Note

class NotesAdapter(val context:Context) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {
    var notesList:List<Note> = mutableListOf();

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onBindViewHolder(viewHolder: NoteViewHolder, position: Int) {
        viewHolder.bind(notesList.get(position))
    }

    public fun swapData(data:List<Note>){
        this.notesList=data
        notifyDataSetChanged()
    }

    inner class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val titleTextView:TextView
        private val textTextView:TextView
        private val dateTextView:TextView
        init {
            titleTextView = itemView.findViewById(R.id.title_tv)
            textTextView = itemView.findViewById(R.id.text_tv)
            dateTextView = itemView.findViewById(R.id.date_tv)
        }
        fun bind(note: Note) {
            titleTextView.setText(note.title)
            textTextView.setText(note.text)
            dateTextView.setText(Utils.formatDate(note.createDate))
        }

    }
}