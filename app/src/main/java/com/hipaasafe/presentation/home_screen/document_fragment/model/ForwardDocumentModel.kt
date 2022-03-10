package com.hipaasafe.presentation.home_screen.document_fragment.model

import java.io.Serializable

data class ForwardDocumentModel(
    var title:String? = "",
    var icon:String? ="",
    var isSelected:Boolean = false,
    var guid:String? = ""
):Serializable
