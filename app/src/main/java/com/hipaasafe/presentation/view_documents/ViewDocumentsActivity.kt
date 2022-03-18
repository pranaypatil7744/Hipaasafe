package com.hipaasafe.presentation.view_documents

import android.content.Intent
import android.os.Bundle
import android.view.View.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivityViewDocumentsBinding
import com.hipaasafe.databinding.BottomSheetMyNotesBinding
import com.hipaasafe.databinding.BottomsheetForwardDocBinding
import com.hipaasafe.domain.model.doctor_login.DoctorsMappedModel
import com.hipaasafe.domain.model.notes.AddNoteRequestModel
import com.hipaasafe.domain.model.notes.GetNotesListRequestModel
import com.hipaasafe.domain.model.notes.NotesListModel
import com.hipaasafe.presentation.home_screen.document_fragment.DocumentFragment
import com.hipaasafe.presentation.home_screen.document_fragment.adapter.ForwardDocAdapter
import com.hipaasafe.presentation.home_screen.document_fragment.model.ForwardDocumentModel
import com.hipaasafe.presentation.request_documents.RequestDocumentActivity
import com.hipaasafe.presentation.view_documents.request_document_fragment.adapter.NotesListAdapter
import com.hipaasafe.utils.ImageUtils
import com.hipaasafe.utils.PreferenceUtils
import com.hipaasafe.utils.enum.LoginUserType
import com.hipaasafe.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel
import java.lang.reflect.Type


class ViewDocumentsActivity : BaseActivity(), ForwardDocAdapter.ForwardClickManager {
    lateinit var binding: ActivityViewDocumentsBinding
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var notesListAdapter: NotesListAdapter
    var chatName: String = ""
    var age: String = ""
    var groupId: String = ""
    var notesList: ArrayList<NotesListModel> = ArrayList()
    var documentFragment = DocumentFragment.newInstance()
    lateinit var bottomSheetMyNotesBinding: BottomSheetMyNotesBinding
    private val notesViewModel: NotesViewModel by viewModel()
    var loginUserType:Int =0
    var selectedDoctorId:String =""
    var doctorsListForNurse:ArrayList<DoctorsMappedModel> = ArrayList()
    var doctorsList:ArrayList<ForwardDocumentModel> = ArrayList()
    lateinit var bottomSheetDialogDoctor: BottomSheetDialog
    lateinit var bottomSheetSelectDoctorBinding: BottomsheetForwardDocBinding
    private lateinit var doctorListAdapter: ForwardDocAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewDocumentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferenceUtils = PreferenceUtils(this)
        getPreferenceData()
        getIntentData()
        setUpView()
        setFragment(documentFragment)
        setUpToolbar()
        setUpListener()
    }

    private fun setUpObserver() {
        with(notesViewModel) {
            getNotesListResponseData.observe(this@ViewDocumentsActivity) {
                toggleLoader(false)
                if (::notesListAdapter.isInitialized) {
                    bottomSheetMyNotesBinding.apply {
                        if (it.success == true) {
                            if (it.data.count != 0 && it.data.count != null) {
                                layoutNoData.root.visibility = GONE
                                notesList.clear()
                                notesList.addAll(it.data.rows)
                                notesListAdapter.notifyDataSetChanged()
                            } else {
                                layoutNoData.root.visibility = VISIBLE
                            }
                        } else {
                            showToast(it.message.toString())
                        }
                    }
                }
            }

            addNotesResponseData.observe(this@ViewDocumentsActivity){
                toggleLoader(false)
                if (it.success == true){
                    bottomSheetMyNotesBinding.etAddNote.setText("")
                    callGetNotesListApi()
                }else{
                    showToast(it.message.toString())
                }
            }
        }
    }

    private fun getPreferenceData() {
        binding.apply {
            loginUserType = preferenceUtils.getValue(Constants.PreferenceKeys.role_id).toIntOrNull()?:0
            if (loginUserType == LoginUserType.NURSE.value){
                layoutSelectDoctor.visibility = VISIBLE
                val collectionType: Type = object : TypeToken<ArrayList<DoctorsMappedModel>>() {}.type
                val data = Gson().fromJson<ArrayList<DoctorsMappedModel>>(preferenceUtils.getValue(Constants.PreferenceKeys.doctorsMappedModel),collectionType)
                doctorsListForNurse.clear()
                doctorsListForNurse.addAll(data)
                setNurseUI(0)
            }else{
                selectedDoctorId = preferenceUtils.getValue(Constants.PreferenceKeys.uid)
                layoutSelectDoctor.visibility = GONE
            }
        }
    }

    private fun setNurseUI(position: Int) {
        binding.apply {
            if (doctorsListForNurse.size != 0){
                selectedDoctorId = doctorsListForNurse[position].uid.toString()
                hintSelectDoctor.visibility = GONE
                imgProfile.visibility = VISIBLE
                tvDoctorName.visibility = VISIBLE
                ImageUtils.INSTANCE?.loadRemoteImageForProfile(imgProfile,doctorsListForNurse[position].avatar)
                tvDoctorName.text = doctorsListForNurse[position].name
                doctorsList.clear()
                for (i in doctorsListForNurse){
                    doctorsList.add(ForwardDocumentModel(
                        title = i.name,
                        icon = i.avatar,
                        doctorId = i.uid
                    ))
                }
                if (::doctorListAdapter.isInitialized){
                    doctorListAdapter.notifyDataSetChanged()
                }
            }else{
                hintSelectDoctor.visibility = VISIBLE
            }
        }
    }

    private fun setUpView() {
        binding.apply {
            documentFragment.isForPatientDocuments = true
            documentFragment.isShowUploadDoc = false
        }
    }

    private fun setUpListener() {
        binding.apply {
            btnRequestedDocument.setOnClickListener {
                val i = Intent(this@ViewDocumentsActivity, RequestDocumentActivity::class.java)
                val b = Bundle()
                b.putString(Constants.CometChatConstant.NAME, chatName)
                b.putString(Constants.CometChatConstant.GUID,groupId)
                i.putExtras(b)
                requestDocResult.launch(i)
            }
            layoutMyNotes.setOnClickListener {
                openForwardListBottomSheet()
            }

            btnDown.setOnClickListener {
                if (doctorsListForNurse.size != 0){
                    openDoctorsListBottomSheet()
                }
            }
            tvDoctorName.setOnClickListener {
                openDoctorsListBottomSheet()
            }
        }
    }

    private fun openDoctorsListBottomSheet() {
        bottomSheetDialogDoctor = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottomsheet_forward_doc, null)
        bottomSheetSelectDoctorBinding = BottomsheetForwardDocBinding.bind(view)
        bottomSheetDialogDoctor.setContentView(view)
        bottomSheetDialogDoctor.setCancelable(true)
        bottomSheetSelectDoctorBinding.apply {
            btnShare.visibility = GONE
            imgClose.setOnClickListener {
                bottomSheetDialogDoctor.dismiss()
            }
            doctorListAdapter = ForwardDocAdapter(
                this@ViewDocumentsActivity,
                doctorsList,
                listener = this@ViewDocumentsActivity, isHideCheck = true
            )
            recyclerAttendanceHistory.adapter = doctorListAdapter
        }
        bottomSheetDialogDoctor.show()
    }

    private val requestDocResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                binding.apply {
                    documentFragment.callFetchReportsApi()
                }
            }
        }

    private fun getIntentData() {
        binding.apply {
            intent?.extras?.run {
                chatName = getString(Constants.CometChatConstant.NAME).toString()
                groupId = getString(Constants.CometChatConstant.GUID).toString()
            }
        }
    }

    fun setFragment(fragment: Fragment) {
        binding.apply {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(containerViewDocuments.id, fragment)
            transaction.commit()
        }
    }

    private fun openForwardListBottomSheet() {
        bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_my_notes, null)
        bottomSheetMyNotesBinding = BottomSheetMyNotesBinding.bind(view)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.setCancelable(true)
        setUpObserver()
        callGetNotesListApi()
        bottomSheetMyNotesBinding.apply {
            tvMyNotes.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            imgUpDown.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            notesListAdapter = NotesListAdapter(this@ViewDocumentsActivity, notesList)
            recyclerNotes.adapter = notesListAdapter
            btnSave.setOnClickListener {
                val note = etAddNote.text.toString().trim()
                if (note.isNotEmpty()) {
                    callAddNoteApi(note)
                } else {
                    showToast("Please enter note first")
                }
            }
        }
        bottomSheetDialog.show()
    }

    private fun callGetNotesListApi() {
        bottomSheetMyNotesBinding.apply {
            if (isNetworkAvailable()) {
                bottomSheetMyNotesBinding.layoutNoInternet.root.visibility = GONE
                recyclerNotes.visibility = VISIBLE
                toggleLoader(true)
                notesViewModel.callGetNotesListApi(
                    request = GetNotesListRequestModel(
                        page = 1, limit = 30, doctor_id = selectedDoctorId, guid =  groupId
                    )
                )
            } else {
                recyclerNotes.visibility = INVISIBLE
                bottomSheetMyNotesBinding.layoutNoInternet.root.visibility = VISIBLE
            }
        }
    }

    private fun callAddNoteApi(note: String) {
        bottomSheetMyNotesBinding.apply {
            if (isNetworkAvailable()) {
                bottomSheetMyNotesBinding.layoutNoInternet.root.visibility = GONE
                toggleLoader(true)
                notesViewModel.callAddNoteApi(
                    request =
                    AddNoteRequestModel(
                        doctor_id = selectedDoctorId,
                        guid = groupId,
                        notes = note
                    )
                )
            } else {
                bottomSheetMyNotesBinding.layoutNoInternet.root.visibility = VISIBLE
            }
        }
    }

    private fun toggleLoader(showLoader: Boolean) {
        toggleFadeView(
            bottomSheetMyNotesBinding.root,
            bottomSheetMyNotesBinding.contentLoading.layoutLoading,
            bottomSheetMyNotesBinding.contentLoading.imageLoading,
            showLoader
        )
    }

    private fun setUpToolbar() {
        binding.toolbarChat.apply {
            tvChatName.text = chatName
            tvLastActive.text = ""
            btnBack.setOnClickListener {
                finish()
            }
            toolbarIcon1.visibility = GONE
            toolbarIcon2.visibility = GONE
        }
    }

    override fun onItemClick(position: Int) {
        bottomSheetDialogDoctor.dismiss()
        setNurseUI(position)
    }

}