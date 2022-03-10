package com.hipaasafe.domain.repository

import com.hipaasafe.domain.model.reports.GetReportsListRequestModel
import com.hipaasafe.domain.model.reports.GetReportsListResponseModel
import com.hipaasafe.domain.model.reports.UploadReportFileRequestModel
import com.hipaasafe.domain.model.reports.UploadReportFileResponseModel

interface ReportsRepository {

    suspend fun callGetReportsList(request: GetReportsListRequestModel): GetReportsListResponseModel

    suspend fun callUploadReportFileApi(request:UploadReportFileRequestModel):UploadReportFileResponseModel
}