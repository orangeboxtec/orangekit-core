package com.orangebox.kit.core.dto

class ResponseList<O> {

    var list: List<O>? = null

    var totalAmount: Long? = null

    var pageQuantity: Long? = null
}