package com.hipaasafe.domain.model.documents

data class ShareDocumentRequestModel(
    var document_id:Int = 0,
    var doctor_uids:ArrayList<String> = ArrayList()
)

data class ShareDocumentResponseModel(
    var success:Boolean? = null,
    var message:String? =""
)
