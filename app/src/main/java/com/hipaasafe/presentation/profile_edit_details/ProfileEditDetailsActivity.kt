package com.hipaasafe.presentation.profile_edit_details

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.View.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.KeyboardUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivityProfileEditDetailsBinding
import com.hipaasafe.databinding.BottomSheetAddPhotoBinding
import com.hipaasafe.listener.ValidationListener
import com.hipaasafe.presentation.login.model.CountryModel
import com.hipaasafe.presentation.profile_edit_details.model.ProfileEditRequestModel
import com.hipaasafe.presentation.profile_edit_details.model.ProfilePicUploadRequestModel
import com.hipaasafe.utils.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class ProfileEditDetailsActivity : BaseActivity(), ValidationListener {

    lateinit var binding: ActivityProfileEditDetailsBinding
    private val profileViewModel: ProfileViewModel by viewModel()
    private lateinit var addPhotoBottomSheetDialogBinding: BottomSheetAddPhotoBinding

    var profileFile: File? = null
    var fileName:String = ""
    var countryList: ArrayList<CountryModel> = ArrayList()
    var selectedDob: Calendar? = null
    var selectedCountryCode: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditDetailsBinding.inflate(layoutInflater)
        preferenceUtils = PreferenceUtils(this)
        setContentView(binding.root)
        profileViewModel.validationListener = this
        setUpObserver()
        setUpToolbar()
        setupView()
        setListener()
    }

    private fun setUpObserver() {
        binding.apply {
            with(profileViewModel) {
                patientUpdateProfileResponseData.observe(this@ProfileEditDetailsActivity) {
                    toggleLoader(false)
                    if (it.success) {
                        val data = getProfileEditDetailsModel()
                        preferenceUtils.apply {
                            setValue(Constants.PreferenceKeys.name, data.name)
//                            setValue(Constants.PreferenceKeys.email, data.email)
//                            setValue(Constants.PreferenceKeys.country_code, data.country_code)
//                            setValue(Constants.PreferenceKeys.number, data.number)
                            setValue(Constants.PreferenceKeys.dob, data.dob)
                            finish()
                        }
                    } else {
                        showToast(it.message)
                    }
                }
                updateProfilePicResponseData.observe(this@ProfileEditDetailsActivity){
                    toggleLoader(false)
                    if (it.success == true){
                        preferenceUtils.setValue(Constants.PreferenceKeys.avatar,it.data.avatar)
                        ImageUtils.INSTANCE?.loadLocalImage(imgDoc, profileFile)
                    }else{
                        showToast(it.message.toString())
                    }
                }
                messageData.observe(this@ProfileEditDetailsActivity) {
                    toggleLoader(false)
                    showToast(it.toString())
                }
            }
        }
    }

    private fun setUpCountryCodes() {
        selectedCountryCode = preferenceUtils.getValue(Constants.PreferenceKeys.country_code)
        binding.apply {
//            countryList.addAll(
//                AppUtils.INSTANCE?.getCountriesList(this@ProfileEditDetailsActivity) ?: ArrayList()
//            )
//            val adapterCodes =
//                ArrayAdapter<String>(
//                    this@ProfileEditDetailsActivity,
//                    android.R.layout.simple_list_item_1,
//                    countryList.map { it ->
//                        it.dial_code
//                    })
            val clist:ArrayList<String> = ArrayList()
            clist.clear()
            clist.add(selectedCountryCode)
            val ad = ArrayAdapter<String>(this@ProfileEditDetailsActivity,android.R.layout.simple_list_item_1,clist)

            spinnerCountryCode.adapter = ad
//            val usIndex = AppUtils.INSTANCE?.getCountryIndex(selectedCountryCode, countryList) ?: 0
//
//            if (usIndex > 0) {
//                spinnerCountryCode.setSelection(usIndex)
//            }
        }
    }

    private fun setupView() {
        binding.apply {
            if (profileFile != null) {
                ImageUtils.INSTANCE?.loadLocalImage(imgDoc, profileFile)
            } else {
                ImageUtils.INSTANCE?.loadRemoteImageForProfile(
                    imgDoc,
                    preferenceUtils.getValue(Constants.PreferenceKeys.avatar)
                )
            }
            edtName.setText(preferenceUtils.getValue(Constants.PreferenceKeys.name))
            edtEmail.setText(preferenceUtils.getValue(Constants.PreferenceKeys.email))
            etDob.setText(preferenceUtils.getValue(Constants.PreferenceKeys.dob))
            etMobile.setText(preferenceUtils.getValue(Constants.PreferenceKeys.number))
            setUpCountryCodes()
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
            imgPdf.visibility = INVISIBLE
            tvPdf.visibility = INVISIBLE
            tvAddPhoto.text = getString(R.string.choose_image)
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
        }

    }

    private val addImageUtils =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data?.extras
                fileName = data?.get(Constants.IntentExtras.EXTRA_FILE_NAME) as String
                val url = data.get(Constants.IntentExtras.EXTRA_FILE_PATH)
                profileFile =
                    File(url.toString())
//                val bitmap: Bitmap = BitmapFactory.decodeFile(bitmapFile.toString())
//                val imageBitmap = ImageUtils.INSTANCE?.bitMapToString(bitmap).toString()
//                val imageExtn = fileName.toString().split(".").last()
                if (profileFile?.exists() == true){
                    callUpdateProfilePicApi()
                }
            }
        }

    private fun callUpdateProfilePicApi() {
        binding.apply {
            if (isNetworkAvailable()){
                toggleLoader(true)
                profileViewModel.callUpdateProfilePicApi(request = ProfilePicUploadRequestModel(
                    profile_pic =profileFile!! ,
                    fileName = fileName
                ))
            }else{
                showToast(getString(R.string.please_check_your_internet_connection))
            }
        }
    }


    private fun setListener() {
        binding.apply {
            imgCamera.setOnClickListener {
                openAddPhotoBottomSheet()
            }
            toolbarHome.btnBack.setOnClickListener {
                finish()
            }
            btnSaveDetails.setOnClickListener {
                clearErrorLabels()
                profileViewModel.validateEditProfileData(getProfileEditDetailsModel())
            }
            edtName.onFocusChangeListener =
                View.OnFocusChangeListener { _: View, hasFocus: Boolean ->
                    if (hasFocus) {
                        clearErrorLabels()
                        layoutName.setStartIconTintList(
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    this@ProfileEditDetailsActivity,
                                    R.color.azure_radiance
                                )
                            )
                        )
                        layoutName.boxStrokeColor =
                            ContextCompat.getColor(
                                this@ProfileEditDetailsActivity,
                                R.color.azure_radiance
                            )

                    } else {
                        layoutName.setStartIconTintList(
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    this@ProfileEditDetailsActivity,
                                    R.color.heather
                                )
                            )
                        )
                        layoutName.boxStrokeColor =
                            ContextCompat.getColor(
                                this@ProfileEditDetailsActivity,
                                R.color.alabaster
                            )

                    }
                    val input = edtName.text.toString().trim()
                    if (!TextUtils.isEmpty(input)) {
                        layoutName.setStartIconTintList(
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    this@ProfileEditDetailsActivity,
                                    R.color.azure_radiance
                                )
                            )
                        )
                    }
                }

            edtEmail.onFocusChangeListener =
                View.OnFocusChangeListener { _: View, hasFocus: Boolean ->
                    if (hasFocus) {
                        clearErrorLabels()
                        layoutEmail.setStartIconTintList(
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    this@ProfileEditDetailsActivity,
                                    R.color.azure_radiance
                                )
                            )
                        )
                        layoutEmail.boxStrokeColor =
                            ContextCompat.getColor(
                                this@ProfileEditDetailsActivity,
                                R.color.azure_radiance
                            )

                    } else {
                        layoutEmail.setStartIconTintList(
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    this@ProfileEditDetailsActivity,
                                    R.color.heather
                                )
                            )
                        )
                        layoutEmail.boxStrokeColor =
                            ContextCompat.getColor(
                                this@ProfileEditDetailsActivity,
                                R.color.alabaster
                            )

                    }
                    val input = edtEmail.text.toString().trim()
                    if (!TextUtils.isEmpty(input)) {
                        layoutEmail.setStartIconTintList(
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    this@ProfileEditDetailsActivity,
                                    R.color.azure_radiance
                                )
                            )
                        )
                    }
                }

            etMobile.onFocusChangeListener =
                View.OnFocusChangeListener { _: View, hasFocus: Boolean ->
                    if (hasFocus) {
                        clearErrorLabels()
                        layoutMobile.setBackgroundResource(R.drawable.bg_box_focused)
                    } else {
                        layoutMobile.setBackgroundResource(R.drawable.bg_box)
                    }
                    val input = etMobile.text.toString().trim()
                    if (!TextUtils.isEmpty(input)) {
                        layoutMobile.setBackgroundResource(R.drawable.bg_box_focused)
                    }
                }
            etDob.onFocusChangeListener =
                View.OnFocusChangeListener { _: View, hasFocus: Boolean ->
                    if (hasFocus) {
                        clearErrorLabels()
                        layoutDob.setStartIconTintList(
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    this@ProfileEditDetailsActivity,
                                    R.color.azure_radiance
                                )
                            )
                        )
                        layoutDob.boxStrokeColor =
                            ContextCompat.getColor(
                                this@ProfileEditDetailsActivity,
                                R.color.azure_radiance
                            )

                    } else {
                        layoutDob.setStartIconTintList(
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    this@ProfileEditDetailsActivity,
                                    R.color.heather
                                )
                            )
                        )
                        layoutDob.boxStrokeColor =
                            ContextCompat.getColor(
                                this@ProfileEditDetailsActivity,
                                R.color.alabaster
                            )

                    }
                    val input = etDob.text.toString().trim()
                    if (!TextUtils.isEmpty(input)) {
                        layoutDob.setStartIconTintList(
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    this@ProfileEditDetailsActivity,
                                    R.color.azure_radiance
                                )
                            )
                        )
                    }
                }
            spinnerCountryCode.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    selectedCountryCode = parent.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
            etDob.setOnClickListener {
                val cal = Calendar.getInstance()
                val y = cal.get(Calendar.YEAR)
                val m = cal.get(Calendar.MONTH)
                val d = cal.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    this@ProfileEditDetailsActivity,
                    { view, year, monthOfYear, dayOfMonth ->

                        val selectedCalendar = Calendar.getInstance()
                        selectedCalendar.set(Calendar.YEAR, year)
                        selectedCalendar.set(Calendar.MONTH, monthOfYear)
                        selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        selectedDob = selectedCalendar
                        val selectedDate =
                            AppUtils.INSTANCE?.convertDateToString(
                                selectedCalendar.time,
                                "yyyy-MM-dd"
                            )
                        etDob.setText(selectedDate)
                    },
                    y,
                    m,
                    d
                )
                datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
                datePickerDialog.show()
            }
        }
    }

    private fun getProfileEditDetailsModel(): ProfileEditRequestModel {
        binding.apply {
            val request = ProfileEditRequestModel()
            request.name = edtName.text.toString().trim()
//            request.email = edtEmail.text.toString().trim()
//            request.number = etMobile.text.toString().trim()
            request.dob = etDob.text.toString().trim()
//            request.country_code = selectedCountryCode
            return request
        }
    }


    private fun setUpToolbar() {
        binding.toolbarHome.apply {
            tvTitle.text = getString(R.string.edit_details)
            divider.visibility = VISIBLE
            tvDate.visibility = GONE
            btnBack.visibility = VISIBLE
        }
    }

    override fun onValidationSuccess(type: String, msg: Int) {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            profileViewModel.callPatientUpdateProfileApi(getProfileEditDetailsModel())
        } else {
            showToast(getString(R.string.no_internet_connection_please_try_again_later))
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

    override fun onValidationFailure(type: String, msg: Int) {
        clearErrorLabels()
        binding.apply {
            when (type) {
                Constants.ErrorMsg.NAME_ERROR -> {
                    layoutName.error = getString(msg)
                }
                Constants.ErrorMsg.EMAIL_ERROR -> {
                    layoutEmail.error = getString(msg)
                }
                Constants.ErrorMsg.AGE_ERROR -> {
                    layoutDob.error = getString(msg)
                }
                Constants.ErrorMsg.MOBILE_ERROR -> {
                    errorText.text = getString(msg)
                    errorText.visibility = VISIBLE
                    layoutMobile.setBackgroundResource(R.drawable.bg_box_error)
                }
            }
            KeyboardUtils.hideSoftInput(this@ProfileEditDetailsActivity)
        }
    }

    private fun clearErrorLabels() {
        binding.apply {
            layoutName.error = ""
            layoutEmail.error = ""
            layoutDob.error = ""
            errorText.text = ""
            errorText.visibility = View.INVISIBLE
            layoutMobile.setBackgroundResource(R.drawable.bg_box)
        }
    }

}