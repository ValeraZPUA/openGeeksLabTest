package com.example.opengeekslabtest

import android.app.Application
import com.example.opengeekslabtest.di.AppComponent
import com.example.opengeekslabtest.di.DaggerAppComponent
import com.example.opengeekslabtest.di.NetworkModule

class App : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .networkModule(NetworkModule(Constants.BASE_URL))
            .build()
    }
}