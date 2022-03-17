package com.hipaasafe.data.repository

import com.hipaasafe.data.source.remote.ApiService
import com.hipaasafe.domain.model.notes.AddNoteRequestModel
import com.hipaasafe.domain.model.notes.AddNoteResponseModel
import com.hipaasafe.domain.model.notes.GetNotesListRequestModel
import com.hipaasafe.domain.model.notes.GetNotesListResponseModel
import com.hipaasafe.domain.repository.NotesRepository

class NotesRepositoryImp constructor(private val apiService: ApiService) : NotesRepository {
    override suspend fun callAddNoteApi(request: AddNoteRequestModel): AddNoteResponseModel {
        return apiService.callAddNoteApi(request)
    }

    override suspend fun callGetNotesListApi(request: GetNotesListRequestModel): GetNotesListResponseModel {
        return apiService.callGetNotesListApi(page = request.page, limit = request.limit, doctor_id = request.doctor_id, patient_id = request.patient_id)
    }
}