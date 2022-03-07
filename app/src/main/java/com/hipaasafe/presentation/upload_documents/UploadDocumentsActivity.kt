package com.hipaasafe.presentation.upload_documents

import android.os.Bundle
import android.view.View.*
import androidx.core.content.ContextCompat
import androidx.core.view.isEmpty
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivityUploadDocumentsBinding
import com.hipaasafe.databinding.BottomsheetForwardDocBinding
import com.hipaasafe.presentation.home_screen.document_fragment.adapter.ForwardDocAdapter
import com.hipaasafe.presentation.home_screen.document_fragment.model.ForwardDocumentModel

class UploadDocumentsActivity : BaseActivity(), ForwardDocAdapter.ForwardClickManager {
    lateinit var binding: ActivityUploadDocumentsBinding
    lateinit var bottomSheetForwardDocBinding: BottomsheetForwardDocBinding
    lateinit var doctorListAdapter: ForwardDocAdapter
    private var doctorList: ArrayList<ForwardDocumentModel> = ArrayList()
    private var unselectedDoctorsList: ArrayList<ForwardDocumentModel> = ArrayList()
    var pendingDocName: String = ""
    var pendingDocBy: String = ""
    var isFromAddDocument: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadDocumentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()
        setUpToolbar()
        setUpListener()
    }

    private fun setUpListener() {
        binding.apply {
            btnDown.setOnClickListener {
                if (isFromAddDocument){
                    openForwardListBottomSheet()
                }
            }
            layoutAddDoc.setOnClickListener {
                it.visibility = INVISIBLE
                layoutUploadDoc.visibility = VISIBLE
                tvUploadedDocName.text = "Document.pdf"
            }
            btnDiscard.setOnClickListener {
                layoutUploadDoc.visibility = INVISIBLE
                layoutAddDoc.visibility = VISIBLE
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
                etDocumentName.isEnabled = true
                btnDown.isClickable = true
            } else {
                etDocumentName.setText(pendingDocName)
                hintSelectDoctor.visibility = INVISIBLE
                etDocumentName.isEnabled = false
                btnDown.isClickable = false
                val chip = layoutInflater.inflate(R.layout.layout_chip,chipsDoctors,false) as Chip
                chip.apply {
                    text = pendingDocBy
                    chipIcon =
                        ContextCompat.getDrawable(this@UploadDocumentsActivity,R.drawable.ic_default_profile_picture)
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
                val chip = layoutInflater.inflate(R.layout.layout_chip,chipsDoctors,false) as Chip
                chip.apply {
                    text = i.title
                    chipIcon =
                        i.icon?.let { ContextCompat.getDrawable(this@UploadDocumentsActivity, it) }
                    setOnCloseIconClickListener {
                        chipsDoctors.removeView(it)
                        val c = it as Chip

                        if (chipsDoctors.isEmpty()){
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