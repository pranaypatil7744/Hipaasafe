package com.hipaasafe.domain.usecase.documents

import com.hipaasafe.domain.model.documents.RemoveRequestDocumentRequestModel
import com.hipaasafe.domain.model.documents.RemoveRequestDocumentResponseModel
import com.hipaasafe.domain.repository.DocumentsRepository
import com.hipaasafe.domain.usecase.base.UseCase

class RemoveRequestDocUseCase constructor(private val documentsRepository: DocumentsRepository) :
    UseCase<RemoveRequestDocumentResponseModel, Any?>() {
    override suspend fun run(params: Any?): RemoveRequestDocumentResponseModel {
        return documentsRepository.callRemoveRequestDocApi(params as RemoveRequestDocumentRequestModel)
    }
}