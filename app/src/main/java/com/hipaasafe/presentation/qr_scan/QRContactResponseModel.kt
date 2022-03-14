package com.hipaasafe.presentation.qr_scan

import java.io.Serializable

data class QRContactResponseModel(
    var companyName: String? = "",
    var name: String? = "",
    var mailingMsgName: String? = "",
    var jobTitle: String? = "",
    var businessEmail: String? = "",
    var personalEmail: String? = "",
    var otherEmail: String? = "",
    var mobile: String? = "",
    var landline: String? = "",
    var otherNumber: String? = ""
) : Serializable
