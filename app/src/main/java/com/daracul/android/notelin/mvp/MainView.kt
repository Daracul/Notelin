package com.daracul.android.notelin.mvp

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.daracul.android.notelin.models.Note

@StateStrategyType(value = AddToEndStrategy::class)
interface MainView : MvpView {
    fun onNotesLoaded (notes:List<Note>)

    fun updateView()

    fun onSearchResult (notes: List<Note>)

    fun onAllNotesDeleted()

    fun onNoteDeleted()

    fun showNoteInfoDialog (noteInfo : String)

    fun hideNoteInfoDialog()

    fun showNoteDeleteDialog(notePosition : Int)

    fun hideNoteDeleteDialog()

    fun showNoteContextDialog (notePosition: Int)

    fun hideNoteContextDialog()

}