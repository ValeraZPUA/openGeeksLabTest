package com.example.opengeekslabtest.model

import com.example.opengeekslabtest.Constants
import com.example.opengeekslabtest.network.responses.LinkCheckedResponse
import javax.inject.Inject

class CheckedResponsesStore @Inject constructor() {

    private var urlsToCheckQuantity = 0
    private var initialResponsesList: ArrayList<LinkCheckedResponse> = arrayListOf()
    private var neighboringServerUrls: ArrayList<Link> = arrayListOf()
    private var isNeighboringServerUrlsChecked = false

    fun setUrlsToCheckQuantity(currentQuantity: Int) {
        urlsToCheckQuantity = currentQuantity
    }

    fun getUrlsToCheckQuantity(): Int {
        return urlsToCheckQuantity
    }

    fun addCheckedResponse(checkedResponse: LinkCheckedResponse) {
        initialResponsesList.add(checkedResponse)
        addNeighboringServerLinksToLinkList(checkedResponse.refs)
    }

    private fun addNeighboringServerLinksToLinkList(checkedResponseNeighboringServerLinks: ArrayList<String>) {
        checkedResponseNeighboringServerLinks.forEach {  neighboringServerLink ->
            if (neighboringServerUrls.find { it.url == neighboringServerLink } == null) {
                neighboringServerUrls.add(Link(neighboringServerLink.split("/").last().toInt(), neighboringServerLink))
                neighboringServerUrls.sortBy { it.serverId }
            }
        }
    }

    fun addInitialUrlsToLinkList(initUrlsArray: Array<String>) {
        initUrlsArray.forEach {
            neighboringServerUrls.add(Link(it.split("/").last().toInt(), it))
        }
        neighboringServerUrls.sortBy { it.serverId }
    }

    fun getNeighboringServerUrls(): ArrayList<String> {
        isNeighboringServerUrlsChecked = true
        val neighboringUrls = arrayListOf<String>()
        neighboringServerUrls.forEach {
            neighboringUrls.add(it.url)
        }
        return neighboringUrls
    }

    fun isNeighboringServerUrlsChecked(): Boolean {
        return isNeighboringServerUrlsChecked
    }

    fun getFastestUrl(): String {
        initialResponsesList.sortBy { it.ping }

        return if (initialResponsesList.first().id == null && initialResponsesList.first().ping < Constants.RESPONSE_TIME_LIMIT) {
            Constants.ALL_URLS_ARE_DEAD
        } else {
            "${Constants.BASE_URL}${initialResponsesList.first().region}/${initialResponsesList.first().id}"
        }
    }

    fun clearResponsesList() {
        initialResponsesList.clear()
    }

    fun clearStore() {
        initialResponsesList.clear()
        neighboringServerUrls.clear()
        urlsToCheckQuantity = 0
        isNeighboringServerUrlsChecked = false
    }

    fun getResponseListSize(): Int {
        return initialResponsesList.size
    }
}