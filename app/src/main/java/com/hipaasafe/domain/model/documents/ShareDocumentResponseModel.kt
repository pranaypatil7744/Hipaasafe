package com.hipaasafe.domain.model.documents

data class ShareDocumentRequestModel(
    var document_file:String = "",
    var doctor_uids:ArrayList<String> = ArrayList()
)

data class ShareDocumentResponseModel(
    var success:Boolean? = null,
    var message:String? ="",
    var data:String? = null
)
