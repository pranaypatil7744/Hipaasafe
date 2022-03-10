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
import com.hipaasafe.domain.model.reports.GetReportsListRequestModel
import com.hipaasafe.domain.model.reports.ReportsDataModel
import com.hipaasafe.domain.model.reports.UploadAndShareDocumentRequestModel
import com.hipaasafe.domain.model.reports.UploadReportFileRequestModel
import com.hipaasafe.presentation.home_screen.document_fragment.adapter.ForwardDocAdapter
import com.hipaasafe.presentation.home_screen.document_fragment.model.ForwardDocumentModel
import com.hipaasafe.utils.AddImageUtils
import com.hipaasafe.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File

class UploadDocumentsActivity : BaseActivity(), ForwardDocAdapter.ForwardClickManager {
    lateinit var binding: ActivityUploadDocumentsBinding
    private lateinit var addPhotoBottomSheetDialogBinding: BottomSheetAddPhotoBinding

    lateinit var bottomSheetForwardDocBinding: BottomsheetForwardDocBinding
    private val documentViewModel: DocumentViewModel by viewModel()
    lateinit var doctorListAdapter: ForwardDocAdapter
    private var doctorList: ArrayList<ForwardDocumentModel> = ArrayList()
    var repostList: ArrayList<ReportsDataModel> = ArrayList()
    private var unselectedDoctorsList: ArrayList<ForwardDocumentModel> = ArrayList()
    var pendingDocName: String = ""
    var pendingDocBy: String = ""
    var isFromAddDocument: Boolean = false
    var uploadDocPath: String = ""
    var fileName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadDocumentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()
        setUpObserver()
        setUpToolbar()
        setUpListener()
        callGetReportsList()
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
                    toggleLoader(false)
                    if (it.success == true) {
                        showToast(it.data?.uploaded_file.toString())
                    } else {
                        showToast(it.message.toString())
                    }
                }
                uploadAndShareDocumentResponseData.observe(this@UploadDocumentsActivity) {
                    toggleLoader(false)
                    if (it.success == true) {
                        if (isFromAddDocument) {
                            finish()
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
                callUploadReportFileApi()
            }

            layoutAddDoc.setOnClickListener {
                openAddPhotoBottomSheet()
            }
            btnDiscard.setOnClickListener {
                showAddDocumentLayout()
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
                request = UploadAndShareDocumentRequestModel(
                    document_file = "",
                    report_name_id = 0,
                    doctor_uids = arrayListOf()
                )
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
                doctorList =
                    getSerializable(Constants.DoctorsList) as ArrayList<ForwardDocumentModel>
                pendingDocName = getString(Constants.PendingDocumentName).toString()
                pendingDocBy = getString(Constants.PendingDocumentBy).toString()
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
            unselectedDoctorsList = doctorList.filter {
                !it.isSelected
            } as ArrayList<ForwardDocumentModel>
            doctorListAdapter = ForwardDocAdapter(
                this@UploadDocumentsActivity,
                unselectedDoctorsList,
                listener = this@UploadDocumentsActivity
            )
            recyclerAttendanceHistory.adapter = doctorListAdapter
            btnShare.text = getString(R.string._continue)
            btnShare.setOnClickListener {
                doctorListAdapter.notifyDataSetChanged()
                val checkSelected = unselectedDoctorsList.find {
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
                bottomSheetDialog.dismiss()
            }
        }
        bottomSheetDialog.show()
    }

    private fun setUpSelectedDoctorsChips() {
        binding.apply {
            val selectedDoctors = unselectedDoctorsList.filter {
                it.isSelected
            }
            for (i in selectedDoctors) {
                val chip = layoutInflater.inflate(R.layout.layout_chip, chipsDoctors, false) as Chip
                chip.apply {
                    text = i.title
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
                            unselectedDoctorsList = doctorList
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
        unselectedDoctorsList[position].isSelected = !unselectedDoctorsList[position].isSelected
        doctorListAdapter.notifyItemChanged(position)
    }
}