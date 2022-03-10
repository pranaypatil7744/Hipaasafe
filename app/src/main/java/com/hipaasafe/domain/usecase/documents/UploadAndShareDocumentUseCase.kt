package com.hipaasafe.domain.usecase.documents

import com.hipaasafe.domain.model.documents.UploadAndShareDocumentRequestModel
import com.hipaasafe.domain.model.documents.UploadAndShareDocumentResponseModel
import com.hipaasafe.domain.repository.DocumentsRepository
import com.hipaasafe.domain.usecase.base.UseCase

class UploadAndShareDocumentUseCase constructor(private val documentsRepository: DocumentsRepository) :
    UseCase<UploadAndShareDocumentResponseModel, Any?>() {
    override suspend fun run(params: Any?): UploadAndShareDocumentResponseModel {
        return documentsRepository.callUploadAndShareDocumentApi(params as UploadAndShareDocumentRequestModel)
    }
}