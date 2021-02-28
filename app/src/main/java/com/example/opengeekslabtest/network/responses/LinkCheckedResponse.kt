package com.example.opengeekslabtest.network.responses

import com.example.opengeekslabtest.Constants

class LinkCheckedResponse(
    val timeOur: Boolean = false,
    val crash: Boolean= false,
    val ping: Int = Constants.RESPONSE_TIME_LIMIT,
    val region: String = "",
    val id: Long? = null,
    val refs: ArrayList<String> = arrayListOf()
)