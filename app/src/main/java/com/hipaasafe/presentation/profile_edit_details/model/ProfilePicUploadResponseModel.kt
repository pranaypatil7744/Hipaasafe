package com.hipaasafe.presentation.profile_edit_details.model

import java.io.File

data class ProfilePicUploadResponseModel(
    var success:Boolean? = null,
    var message:String? = "",
    var data:DataProfileUpload = DataProfileUpload()
)

data class DataProfileUpload(
    var avatar:String? = ""
)

data class ProfilePicUploadRequestModel(
    var profile_pic: File,
    var fileName:String
)
