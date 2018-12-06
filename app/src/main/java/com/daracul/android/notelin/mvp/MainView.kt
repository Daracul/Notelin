package com.daracul.android.notelin.mvp

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.daracul.android.notelin.models.Note

@StateStrategyType(value = AddToEndStrategy::class)
interface MainView : MvpView {
    fun onNotesLoaded (notes:List<Note>)

    fun showError (throwable: Throwable?)

    fun showMessage(text:String)

}