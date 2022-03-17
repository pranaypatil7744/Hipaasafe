package com.hipaasafe.domain.repository

import com.hipaasafe.domain.model.notes.AddNoteRequestModel
import com.hipaasafe.domain.model.notes.AddNoteResponseModel
import com.hipaasafe.domain.model.notes.GetNotesListRequestModel
import com.hipaasafe.domain.model.notes.GetNotesListResponseModel

interface NotesRepository {

    suspend fun callAddNoteApi(request:AddNoteRequestModel):AddNoteResponseModel

    suspend fun callGetNotesListApi(request:GetNotesListRequestModel):GetNotesListResponseModel
}