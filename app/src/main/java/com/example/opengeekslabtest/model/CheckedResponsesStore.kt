package com.example.opengeekslabtest.model

import android.util.Log
import com.example.opengeekslabtest.Constants
import com.example.opengeekslabtest.network.responses.LinkCheckedResponse
import javax.inject.Inject

class CheckedResponsesStore @Inject constructor() {

    //private var isFirstAliveUrlFound = false
    private var urlsToCheckQuantity = 0
    private var responsesList: ArrayList<LinkCheckedResponse> = arrayListOf()

    fun setUrlsToCheckQuantity(currentQuantity: Int) {
        urlsToCheckQuantity = currentQuantity
    }

    fun getUrlsToCheckQuantity(): Int {
        return urlsToCheckQuantity
    }

    /*fun setIsFirstAliveUrlFoundTrue() {
        isFirstAliveUrlFound = true
    }

    fun getIsFirstAliveUrlFoundTrue(): Boolean {
        return isFirstAliveUrlFound
    }*/

    fun addCheckedResponse(checkedResponse: LinkCheckedResponse) {
        responsesList.add(checkedResponse)
    }

    fun getFastestUrl(): String {
        responsesList.sortBy { it.ping }
        responsesList.forEach {
            Log.d("tag22", "getFastestUrl: ${it.ping}")
        }

        Log.d("tag22", "first, ping: ${responsesList.first().ping}")
        Log.d("tag22", "first, id: ${responsesList.first().id}")

        return if (responsesList.first().id == null) {
            Constants.ALL_URLS_ARE_DEAD
        } else {
            "${Constants.BASE_URL}${responsesList.first().region}/${responsesList.first().id}"
        }
    }

    fun clearStore() {
        responsesList.clear()
        urlsToCheckQuantity = 0
        //isFirstAliveUrlFound = false
    }

    fun getResponseListSize(): Int {
        return responsesList.size
    }
}