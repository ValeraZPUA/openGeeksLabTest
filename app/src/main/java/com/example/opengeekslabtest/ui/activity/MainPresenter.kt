package com.example.opengeekslabtest.ui.activity

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.opengeekslabtest.App
import com.example.opengeekslabtest.Constants
import com.example.opengeekslabtest.model.CheckedResponsesStore
import com.example.opengeekslabtest.network.Requests
import com.example.opengeekslabtest.network.responses.LinkCheckedResponse
import com.example.opengeekslabtest.network.responses.SayHelloResponse
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {

    @Inject
    lateinit var requests: Requests

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var checkedResponsesStore: CheckedResponsesStore

    init {
        App.appComponent.inject(this)
    }

    val TAG = "tag22"

    fun findAliveUrl(urlsArray: Array<String>) {
        checkedResponsesStore.setUrlsToCheckQuantity(urlsArray.size)

        for(element in urlsArray) {
            requests
                .checkUrl(element)
                .enqueue(object : Callback<LinkCheckedResponse> {
                    override fun onResponse(call: Call<LinkCheckedResponse>, response: Response<LinkCheckedResponse>) {
                        if (response.isSuccessful) {
                            addCheckedResponse(response.body()!!)
                        } else {
                            addCheckedResponse(LinkCheckedResponse())
                        }
                    }

                    override fun onFailure(call: Call<LinkCheckedResponse>, t: Throwable) {
                        Log.e(TAG, "onFailure, findAliveUrl: ${t.message}")
                        addCheckedResponse(LinkCheckedResponse())
                    }
                })
        }
    }

    private fun addCheckedResponse(linkCheckedResponse: LinkCheckedResponse) {
        checkedResponsesStore.addCheckedResponse(linkCheckedResponse)

        if (checkedResponsesStore.getUrlsToCheckQuantity() == checkedResponsesStore.getResponseListSize()) {
            val fastestUrl = checkedResponsesStore.getFastestUrl()
            if (fastestUrl != Constants.ALL_URLS_ARE_DEAD) {
                RetrofitUrlManager.getInstance().putDomain(Constants.KEY_BASE_URL, fastestUrl)
                sayHello()
            } else {
                viewState.showNoAliveUrlsFoundToast()
            }
            checkedResponsesStore.clearStore()
            viewState.hideLoader()
        }
    }


    private fun sayHello() {
        requests
            .sayHello()
            .enqueue(object : Callback<SayHelloResponse> {
                override fun onResponse(call: Call<SayHelloResponse>, response: Response<SayHelloResponse>) {
                    if (response.isSuccessful) {
                        viewState.setHelloMessage(response.body()!!.text)
                    } else {
                        viewState.showErrorToast()
                    }
                }

                override fun onFailure(call: Call<SayHelloResponse>, t: Throwable) {
                    viewState.showErrorToast()
                    viewState.hideLoader()
                }
            })
    }
}