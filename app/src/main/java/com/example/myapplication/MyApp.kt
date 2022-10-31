package com.example.myapplication

import android.app.Application
import com.example.myapplication.di.ApplicationComponent
import com.example.myapplication.di.DaggerApplicationComponent

class MyApp : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.factory().create(this)
    }
}