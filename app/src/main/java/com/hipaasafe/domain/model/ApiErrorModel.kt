package com.hipaasafe.domain.model

data class ApiErrorModel(var success: Boolean? = false, var message: String? = "")


data class ScanResult(var doctor_id:Int? =0,var organization_id:String?="")