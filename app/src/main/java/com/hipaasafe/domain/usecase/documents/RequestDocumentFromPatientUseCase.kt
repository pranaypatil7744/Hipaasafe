package com.hipaasafe.domain.usecase.documents

import com.hipaasafe.domain.model.documents.RequestDocumentFromPatientRequestModel
import com.hipaasafe.domain.model.documents.RequestDocumentFromPatientResponseModel
import com.hipaasafe.domain.repository.DocumentsRepository
import com.hipaasafe.domain.usecase.base.UseCase

class RequestDocumentFromPatientUseCase constructor(private val documentsRepository: DocumentsRepository) :
    UseCase<RequestDocumentFromPatientResponseModel, Any?>() {
    override suspend fun run(params: Any?): RequestDocumentFromPatientResponseModel {
        return documentsRepository.callRequestDocumentFromPatientApi(params as RequestDocumentFromPatientRequestModel)
    }
}