package com.hipaasafe.presentation.home_screen.document_fragment.model

data class DocumentsModel(
    var documentItemType: DocumentItemType,
    var title:String? = "",
    var subTitle:String? = "",
)
enum class DocumentItemType(val value:Int){
    ITEM_ADD_DOC(1),
    ITEM_TITLE(2),
    ITEM_UPLOADED_DOC(3),
    ITEM_PENDING_DOC(4),
}