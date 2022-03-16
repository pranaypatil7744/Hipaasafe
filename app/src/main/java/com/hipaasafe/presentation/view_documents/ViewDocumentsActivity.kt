package com.hipaasafe.presentation.view_documents

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivityViewDocumentsBinding
import com.hipaasafe.databinding.BottomSheetMyNotesBinding
import com.hipaasafe.databinding.BottomsheetForwardDocBinding
import com.hipaasafe.presentation.home_screen.document_fragment.DocumentFragment
import com.hipaasafe.presentation.home_screen.document_fragment.adapter.ForwardDocAdapter
import com.hipaasafe.presentation.request_documents.RequestDocumentActivity
import com.hipaasafe.presentation.view_documents.request_document_fragment.RequestDocumentFragment
import com.hipaasafe.utils.PreferenceUtils

class ViewDocumentsActivity : BaseActivity() {
    lateinit var binding: ActivityViewDocumentsBinding
    var chatName: String = ""
    var age: String = ""
    var patientUid: String = ""
    var doctorUid: String = ""
    var groupId:String =""
    var documentFragment = DocumentFragment.newInstance()
    lateinit var bottomSheetMyNotesBinding: BottomSheetMyNotesBinding

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

    private fun getPreferenceData() {
        binding.apply {
            doctorUid = preferenceUtils.getValue(Constants.PreferenceKeys.uid)
        }
    }

    private fun setUpView() {
        binding.apply {
            documentFragment.patientUid = patientUid
            documentFragment.selectedDoctorUid = doctorUid
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
                b.putString(Constants.CometChatConstant.PATIENT_ID,patientUid)
                i.putExtras(b)
                requestDocResult.launch(i)
            }
            layoutMyNotes.setOnClickListener {
                openForwardListBottomSheet()
            }
        }
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
                patientUid = getString(Constants.CometChatConstant.PATIENT_ID).toString()
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
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_my_notes, null)
        bottomSheetMyNotesBinding = BottomSheetMyNotesBinding.bind(view)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.setCancelable(true)
        bottomSheetMyNotesBinding.apply {
            tvMyNotes.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            imgUpDown.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            btnSave.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
        }
        bottomSheetDialog.show()
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
}