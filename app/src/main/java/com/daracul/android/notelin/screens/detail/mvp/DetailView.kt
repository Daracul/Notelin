package com.daracul.android.notelin.screens.detail.mvp

import com.arellomobile.mvp.MvpView
import com.daracul.android.notelin.models.Note

interface DetailView:MvpView {
    fun setData(note: Note)
    fun showToast(text:String)
    fun setActionBar(text:String)
    fun finishActivity()
}