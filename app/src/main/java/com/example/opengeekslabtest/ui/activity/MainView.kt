package com.example.opengeekslabtest.ui.activity

import com.arellomobile.mvp.MvpView

interface MainView : MvpView {
    fun hideLoader()
    fun setHelloMessage(helloMessage: String)
    fun showErrorToast()
    fun showNoAliveUrlsFoundToast()
}
