package com.hipaasafe.domain.model.documents

data class GetReportsListResponseModel(
    var success: Boolean? = false,
    var message: String? = "",
    var data:ArrayList<ReportsDataModel>? = ArrayList()
)

data class ReportsDataModel(
    var id: Int = 0,
    var title: String? = ""
)

data class GetReportsListRequestModel(
//    var page:Int,
//    var limit:Int,
    var doctor_id:String ?= null
)
