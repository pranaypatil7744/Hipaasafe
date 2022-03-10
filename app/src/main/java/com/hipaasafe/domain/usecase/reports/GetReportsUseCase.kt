package com.hipaasafe.domain.usecase.reports

import com.hipaasafe.domain.model.reports.GetReportsListRequestModel
import com.hipaasafe.domain.model.reports.GetReportsListResponseModel
import com.hipaasafe.domain.repository.ReportsRepository
import com.hipaasafe.domain.usecase.base.UseCase

class GetReportsUseCase constructor(private val reportsRepository: ReportsRepository) :
    UseCase<GetReportsListResponseModel, Any?>() {
    override suspend fun run(params: Any?): GetReportsListResponseModel {
        return reportsRepository.callGetReportsList(params as GetReportsListRequestModel)
    }
}