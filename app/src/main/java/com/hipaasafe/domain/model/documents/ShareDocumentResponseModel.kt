package com.hipaasafe.domain.model.documents

data class ShareDocumentRequestModel(
    var document_file:String = "",
    var uids:ArrayList<String> = ArrayList(),
    var type:String=""
)

data class ShareDocumentResponseModel(
    var success:Boolean? = null,
    var message:String? ="",
    var data:String? = null
)
