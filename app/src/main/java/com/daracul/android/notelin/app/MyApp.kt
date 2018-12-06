package com.daracul.android.notelin.app

import android.app.Application


class MyApp : Application() {
    companion object {
        lateinit var context: MyApp
    }

    override fun onCreate() {
        super.onCreate()
        context = this;
    }
}

