package com.example.opengeekslabtest.di

import com.example.opengeekslabtest.ui.activity.MainPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class
    ]
)

interface AppComponent {
    fun inject(presenter: MainPresenter)
}