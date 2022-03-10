package com.hipaasafe.domain.model.documents

import java.io.File

data class UploadReportFileResponseModel(
    var success:Boolean? = false,
    var message:String? = "",
    var data:UploadReportData? = UploadReportData()
)

data class UploadReportData(
    var uploaded_file:String? = ""
)

data class UploadReportFileRequestModel(
    var user_reports:File,
    var fileName:String= "",
    )
