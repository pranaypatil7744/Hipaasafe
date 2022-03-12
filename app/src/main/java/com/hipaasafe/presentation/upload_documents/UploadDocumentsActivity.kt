package com.hipaasafe.presentation.upload_documents

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View.*
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isEmpty
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivityUploadDocumentsBinding
import com.hipaasafe.databinding.BottomSheetAddPhotoBinding
import com.hipaasafe.databinding.BottomsheetForwardDocBinding
import com.hipaasafe.domain.model.get_doctors.GetDoctorsRequestModel
import com.hipaasafe.domain.model.documents.GetReportsListRequestModel
import com.hipaasafe.domain.model.documents.ReportsDataModel
import com.hipaasafe.domain.model.documents.UploadAndShareDocumentRequestModel
import com.hipaasafe.domain.model.documents.UploadReportFileRequestModel
import com.hipaasafe.presentation.home_screen.document_fragment.adapter.ForwardDocAdapter
import com.hipaasafe.presentation.home_screen.document_fragment.model.ForwardDocumentModel
import com.hipaasafe.presentation.home_screen.my_network.MyNetworkViewModel
import com.hipaasafe.utils.AddImageUtils
import com.hipaasafe.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File

class UploadDocumentsActivity : BaseActivity(), ForwardDocAdapter.ForwardClickManager {
    lateinit var binding: ActivityUploadDocumentsBinding
    private lateinit var addPhotoBottomSheetDialogBinding: BottomSheetAddPhotoBinding

    lateinit var bottomSheetForwardDocBinding: BottomsheetForwardDocBinding
    private val documentViewModel: DocumentViewModel by viewModel()
    private val myNetworkViewModel: MyNetworkViewModel by viewModel()

    private lateinit var doctorListAdapter: ForwardDocAdapter
    private var doctorList: ArrayList<ForwardDocumentModel> = ArrayList()
    var repostList: ArrayList<ReportsDataModel> = ArrayList()
    private var unselectedDoctorsList: ArrayList<ForwardDocumentModel> = ArrayList()
    var pendingDocName: String = ""
    var pendingDocBy: String = ""
    var pendingDocGuid: String = ""
    var isFromAddDocument: Boolean = false
    var uploadDocPath: String = ""
    var fileName: String = ""
    var uploadedFile: String = ""
    var selectedDocumentId: Int = 0
    var selectedDoctorUids: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadDocumentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()
        setUpObserver()
        setUpToolbar()
        setUpListener()
        callDoctorsApi()
        callGetReportsList()
    }

    private fun callDoctorsApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                myNetworkViewModel.callMyNetworkDoctorsListApi(
                    GetDoctorsRequestModel(page = 1, limit = 30)
                )
            } else {
                showToast(getString(R.string.please_check_your_internet_connection))
            }
        }
    }

    private fun getUploadAndShareDocumentRequestModel(): UploadAndShareDocumentRequestModel {
        val request = UploadAndShareDocumentRequestModel()
        request.document_file = uploadedFile
        request.report_name_id = selectedDocumentId
        request.doctor_uids = selectedDoctorUids
        return request
    }

    private fun setUpObserver() {
        binding.apply {
            with(documentViewModel) {
                getReportsListResponseData.observe(this@UploadDocumentsActivity) {
                    toggleLoader(false)
                    if (it.success == true) {
                        if (it.data?.isNotEmpty() == true) {
                            repostList.clear()
                            repostList.addAll(it.data ?: arrayListOf())
                            val list: ArrayList<String> = ArrayList()
                            list.clear()
                            for (i in repostList) {
                                list.add(i.title.toString())
                            }
                            val adapterReports =
                                ArrayAdapter<String>(
                                    this@UploadDocumentsActivity,
                                    android.R.layout.simple_list_item_1,
                                    list
                                )
                            etDocumentName.setAdapter(adapterReports)
                        }
                    } else {
                        showToast(it.message.toString())
                    }
                }
                uploadReportFileResponseData.observe(this@UploadDocumentsActivity) {
                    if (it.success == true) {
                        uploadedFile = it.data?.uploaded_file.toString()
                        callUploadAndShareDocumentApi()
                    } else {
                        toggleLoader(false)
                        showToast(it.message.toString())
                    }
                }
                uploadAndShareDocumentResponseData.observe(this@UploadDocumentsActivity) {
                    toggleLoader(false)
                    if (it.success == true) {
                        val resultIntent = intent
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    } else {
                        showToast(it.message.toString())
                    }
                }
                messageData.observe(this@UploadDocumentsActivity) {
                    toggleLoader(false)
                    showToast(it.toString())
                }
            }

            with(myNetworkViewModel) {
                myNetworkListResponseData.observe(this@UploadDocumentsActivity) {
                    toggleLoader(false)
                    if (it.success == true) {
                        if (it.data.count != 0) {
                            doctorList.clear()
                            for (i in it.data.rows ?: arrayListOf()) {
                                doctorList.add(
                                    ForwardDocumentModel(
                                        title = i.list_doctor_details.name,
                                        icon = i.list_doctor_details.avatar,
                                        guid = i.guid,
                                        isSelected = false
                                    )
                                )
                            }
                            if (::doctorListAdapter.isInitialized) {
                                doctorListAdapter.notifyDataSetChanged()
                            }
                        } else {

                        }
                    } else {
                        showToast(it.message.toString())
                    }
                }
                messageData.observe(this@UploadDocumentsActivity) {
                    toggleLoader(false)
                    showToast(it.toString())
                }
            }
        }
    }

    private fun toggleLoader(showLoader: Boolean) {
        toggleFadeView(
            binding.root,
            binding.contentLoading.layoutLoading,
            binding.contentLoading.imageLoading,
            showLoader
        )
    }

    private fun callGetReportsList() {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                documentViewModel.callGetReportListApi(
                    request = GetReportsListRequestModel(
                        page = 1,
                        limit = 30
                    )
                )
            } else {
                showToast(getString(R.string.please_check_your_internet_connection))
            }
        }
    }

    private fun setUpListener() {
        binding.apply {
            btnDown.setOnClickListener {
                if (isFromAddDocument) {
                    openForwardListBottomSheet()
                }
            }
            layoutShare.setOnClickListener {
                if (isFromAddDocument) {
                    openForwardListBottomSheet()
                }
            }

            btnShareAndUpload.setOnClickListener {
                val selectedList = doctorList.filter {
                    it.isSelected
                }
                if (selectedList.isNotEmpty()) {
                    for (i in selectedList) {
                        selectedDoctorUids.add(i.guid.toString())
                    }
                }
                when {
                    uploadDocPath.isEmpty() -> {
                        showToast("Please upload document")
                    }
                    selectedDocumentId == 0 -> {
                        showToast("Please select document name")
                    }
//                    selectedDoctorUids.isEmpty() -> {
//                        showToast(getString(R.string.please_select_at_least_1_doctor))
//                    }
                    else -> {
//                        showToast("Success")
                        callUploadReportFileApi()
                    }
                }
            }

            layoutAddDoc.setOnClickListener {
                openAddPhotoBottomSheet()
            }
            btnDiscard.setOnClickListener {
                showAddDocumentLayout()
            }

            etDocumentName.setOnItemClickListener { parent, view, position, id ->
                selectedDocumentId = repostList[position].id ?: 0
            }
        }
    }

    private fun callUploadReportFileApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            documentViewModel.callUploadReportFileApi(
                request =
                UploadReportFileRequestModel(
                    user_reports = File(uploadDocPath), fileName = fileName
                )
            )
        } else {
            showToast(getString(R.string.please_check_your_internet_connection))
        }
    }

    private fun callUploadAndShareDocumentApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            documentViewModel.callUploadAndShareDocumentApi(
                getUploadAndShareDocumentRequestModel()
            )
        } else {
            showToast(getString(R.string.please_check_your_internet_connection))
        }
    }

    private fun hideAddDocumentLayout() {
        binding.apply {
            layoutAddDoc.visibility = INVISIBLE
            layoutUploadDoc.visibility = VISIBLE
        }
    }

    private fun showAddDocumentLayout() {
        binding.apply {
            layoutAddDoc.visibility = VISIBLE
            layoutUploadDoc.visibility = INVISIBLE
            uploadDocPath = ""
            fileName = ""
        }
    }

    private fun openAddPhotoBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_add_photo, null)
        addPhotoBottomSheetDialogBinding = BottomSheetAddPhotoBinding.bind(view)
        bottomSheetDialog.apply {
            setCancelable(true)
            setContentView(view)
            show()
        }
        val intent = Intent(this, AddImageUtils::class.java)
        val b = Bundle()
        addPhotoBottomSheetDialogBinding.apply {
            imgPdf.visibility = VISIBLE
            tvPdf.visibility = VISIBLE
            tvAddPhoto.text = getString(R.string.choose_document)
            imgCamera.setOnClickListener {
                b.putBoolean(Constants.IS_CAMERA, true)
                intent.putExtras(b)
                addImageUtils.launch(intent)
                bottomSheetDialog.dismiss()
            }
            imgGallery.setOnClickListener {
                b.putBoolean(Constants.IS_CAMERA, false)
                intent.putExtras(b)
                addImageUtils.launch(intent)
                bottomSheetDialog.dismiss()
            }
            imgPdf.setOnClickListener {
                b.putBoolean(Constants.IS_CAMERA, false)
                b.putBoolean(Constants.IS_PDF, true)
                intent.putExtras(b)
                pdfResult.launch(intent)
                bottomSheetDialog.dismiss()
            }
        }

    }

    private val pdfResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data?.extras
                uploadDocPath = data?.get(Constants.IntentExtras.EXTRA_FILE_PATH).toString()
                fileName = data?.get(Constants.IntentExtras.EXTRA_FILE_NAME).toString()
                binding.apply {
                    imgDoc.setImageResource(R.drawable.ic_default_pdf)
                    tvUploadedDocName.text = fileName.toString()
                    hideAddDocumentLayout()
                }
            }
        }


    private val addImageUtils =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data?.extras
                fileName = data?.get(Constants.IntentExtras.EXTRA_FILE_NAME).toString()
                uploadDocPath = data?.get(Constants.IntentExtras.EXTRA_FILE_PATH).toString()
                val imageExtn = fileName.toString().split(".").last()
                binding.apply {
                    imgDoc.setImageResource(R.drawable.ic_default_img)
                    tvUploadedDocName.text = fileName.toString()
                    hideAddDocumentLayout()
                }
            }
        }

    private fun getIntentData() {
        binding.apply {
            intent.extras?.run {
                isFromAddDocument = getBoolean(Constants.IsFromAdd)
                pendingDocName = getString(Constants.PendingDocumentName).toString()
                pendingDocBy = getString(Constants.PendingDocumentBy).toString()
                pendingDocGuid = getString(Constants.PendingDocumentGuid).toString()
                selectedDocumentId = getInt(Constants.PendingDocumentId)
                setUpView()
            }
        }
    }

    private fun setUpView() {
        binding.apply {
            if (isFromAddDocument) {
                hintSelectDoctor.visibility = VISIBLE
            } else {
                etDocumentName.setText(pendingDocName)
                hintSelectDoctor.visibility = INVISIBLE
                selectedDoctorUids.clear()
                selectedDoctorUids.add(pendingDocGuid)
                val chip = layoutInflater.inflate(R.layout.layout_chip, chipsDoctors, false) as Chip
                chip.apply {
                    text = pendingDocBy
                    chipIcon =
                        ContextCompat.getDrawable(
                            this@UploadDocumentsActivity,
                            R.drawable.ic_default_profile_picture
                        )
                    isCloseIconVisible = false
//                    setOnCloseIconClickListener {
//                        chipsDoctors.removeAllViews()
//                        hintSelectDoctor.visibility = VISIBLE
//                    }
                }
                chipsDoctors.addView(chip)
            }
        }
    }

    private fun openForwardListBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottomsheet_forward_doc, null)
        bottomSheetForwardDocBinding = BottomsheetForwardDocBinding.bind(view)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.setCancelable(true)
        bottomSheetForwardDocBinding.apply {
//            unselectedDoctorsList = doctorList.filter {
//                !it.isSelected
//            } as ArrayList<ForwardDocumentModel>
            doctorListAdapter = ForwardDocAdapter(
                this@UploadDocumentsActivity,
                doctorList,
                listener = this@UploadDocumentsActivity
            )
            recyclerAttendanceHistory.adapter = doctorListAdapter
            btnShare.text = getString(R.string._continue)
            btnShare.setOnClickListener {
                doctorListAdapter.notifyDataSetChanged()
                val checkSelected = doctorList.find {
                    it.isSelected
                }
                if (checkSelected != null) {
                    bottomSheetDialog.dismiss()
                    binding.hintSelectDoctor.visibility = INVISIBLE
                    setUpSelectedDoctorsChips()
                } else {
                    binding.hintSelectDoctor.visibility = VISIBLE
                    showToast(getString(R.string.please_select_at_least_1_doctor))
                }
            }
            imgClose.setOnClickListener {
                for (i in doctorList){
                    i.isSelected = false
                }
                doctorListAdapter.notifyDataSetChanged()
                bottomSheetDialog.dismiss()
            }
        }
        bottomSheetDialog.show()
    }

    private fun setUpSelectedDoctorsChips() {
        binding.apply {
            chipsDoctors.removeAllViews()
            val selectedDoctors = doctorList.filter {
                it.isSelected
            }
            for (i in selectedDoctors) {
                val chip = layoutInflater.inflate(R.layout.layout_chip, chipsDoctors, false) as Chip
                chip.apply {
                    text = i.title
                    tag = i.guid
                    chipIcon =
                        ContextCompat.getDrawable(
                            this@UploadDocumentsActivity,
                            R.drawable.ic_default_profile_picture
                        )
                    setOnCloseIconClickListener {
                        chipsDoctors.removeView(it)
                        val c = it as Chip
                        if (chipsDoctors.isEmpty()) {
                            hintSelectDoctor.visibility = VISIBLE
                        }
                        val list = doctorList.find {
                            it.guid?.equals(c.tag) == true
                        }
                        if (list != null){
                            val index = doctorList.indexOf(list)
                            doctorList[index].isSelected = false
                        }
                    }
                }
                chipsDoctors.addView(chip)
            }
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            btnBack.apply {
                visibility = VISIBLE
                setOnClickListener {
                    finish()
                }
            }
            tvTitle.text = getString(R.string.upload_documents)
            tvDate.visibility = GONE
            divider.visibility = VISIBLE
        }
    }

    override fun onItemClick(position: Int) {
        doctorList[position].isSelected = !doctorList[position].isSelected
        doctorListAdapter.notifyItemChanged(position)
    }
}