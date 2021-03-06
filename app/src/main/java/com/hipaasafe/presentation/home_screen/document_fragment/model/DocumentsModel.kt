package com.hipaasafe.presentation.home_screen.document_fragment.model

import java.io.Serializable

data class DocumentsModel(
    var documentItemType: DocumentItemType,
    var title:String? = "",
    var subTitle:String? = "",
    var guid:String? ="",
    var doctorId:String? ="",
    var patientId:String? ="",
    var uploadDocumentId:Int =0,
    var DocumentRequestId:Int =0,
    var uploadedFileName:String?= "",
    var uploadedFileType:String?= "",
    var uploadedFileBy:String?= "",
    var uploadedFileById:String?= "",
    var uploadedFileUrl:String? =""
):Serializable
enum class DocumentItemType(val value:Int){
    ITEM_ADD_DOC(1),
    ITEM_TITLE(2),
    ITEM_UPLOADED_DOC(3),
    ITEM_PENDING_DOC(4),
    ITEM_REQUEST_DOC(5)
}
