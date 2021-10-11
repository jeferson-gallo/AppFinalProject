package com.eljeff.appfinalproject.data.server

class ListProductServer (
    var name : String? = null,
    var total : String? = null,
    var date : String? = null,
    var delivered : Boolean? = null,
    var products : MutableList<ProductSumz>? = null
)