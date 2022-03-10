package com.hipaasafe.domain.usecase.reports

import com.hipaasafe.domain.model.reports.UploadReportFileRequestModel
import com.hipaasafe.domain.model.reports.UploadReportFileResponseModel
import com.hipaasafe.domain.repository.ReportsRepository
import com.hipaasafe.domain.usecase.base.UseCase

class UploadReportsFileUseCase constructor(private val reportsRepository: ReportsRepository) :
    UseCase<UploadReportFileResponseModel, Any?>() {
    override suspend fun run(params: Any?): UploadReportFileResponseModel {
        return reportsRepository.callUploadReportFileApi(params as UploadReportFileRequestModel)
    }
}