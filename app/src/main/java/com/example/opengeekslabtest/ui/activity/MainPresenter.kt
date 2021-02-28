package com.example.opengeekslabtest.ui.activity

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
import javax.inject.Inject

@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {

    @Inject
    lateinit var requests: Requests

    @Inject
    lateinit var checkedResponsesStore: CheckedResponsesStore

    private lateinit var aliveUrlCall: Call<LinkCheckedResponse>
    private val callbackList = arrayListOf<Call<LinkCheckedResponse>>()

    init {
        App.appComponent.inject(this)
    }

    fun findAliveUrl(urlsArray: Array<String>) {
        checkedResponsesStore.setUrlsToCheckQuantity(urlsArray.size)
        checkedResponsesStore.addInitialUrlsToLinkList(urlsArray)

        for (element in urlsArray) {
            checkedResponsesStore
            aliveUrlCall = requests
                .checkUrl(element).apply {
                    callbackList.add(this)
                    enqueue(object : Callback<LinkCheckedResponse> {
                        override fun onResponse(call: Call<LinkCheckedResponse>, response: Response<LinkCheckedResponse>) {
                            if (response.isSuccessful) {
                                addCheckedResponse(response.body()!!)
                            } else {
                                addCheckedResponse(LinkCheckedResponse())
                            }
                        }

                        override fun onFailure(call: Call<LinkCheckedResponse>, t: Throwable) {
                            addCheckedResponse(LinkCheckedResponse())
                        }
                    })
                }
        }
    }

    fun cancelFindAliveUrlRequest() {
        callbackList.forEach {
            it.cancel()
        }
        callbackList.clear()
        checkedResponsesStore.clearStore()
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

            if (!checkedResponsesStore.isNeighboringServerUrlsChecked()) {
                findAliveUrl(checkedResponsesStore.getNeighboringServerUrls().toTypedArray())
                checkedResponsesStore.clearResponsesList()
            } else {
                checkedResponsesStore.clearStore()
            }
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