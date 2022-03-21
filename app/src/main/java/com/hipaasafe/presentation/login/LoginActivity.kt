package com.hipaasafe.presentation.login

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivityLoginBinding
import com.hipaasafe.domain.model.doctor_login.DoctorLoginSendOtpRequestModel
import com.hipaasafe.domain.model.patient_login.PatientSendOtpRequestModel
import com.hipaasafe.presentation.login.model.CountryModel
import com.hipaasafe.presentation.verify_otp.VerifyOtpActivity
import com.hipaasafe.utils.AppUtils
import com.hipaasafe.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel


class LoginActivity : BaseActivity() {
    lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModel()
    var countryList: ArrayList<CountryModel> = ArrayList()
    private var selectedCountryShortForm: String? = ""
    var isDoctorLogin: Boolean = false
    var selectedCountryCode: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpObserver()
        getIntentData()
        setUpListener()
        setUpCountryCodes()
    }

    private fun setUpObserver() {
        binding.apply {
            with(loginViewModel) {
                doctorLoginSendOtpResponseData.observe(this@LoginActivity) {
                    toggleLoader(false)
                    if (it.success) {
                        navigateToVerifyOtpScreen()
                    } else {
                        showToast(it.message)
                    }
                }
                patientSendOtpResponseData.observe(this@LoginActivity) {
                    toggleLoader(false)
                    if (it.success) {
                        navigateToVerifyOtpScreen()
                    } else {
                        showToast(it.message)
                    }
                }
                messageData.observe(this@LoginActivity) {
                    toggleLoader(false)
                    showToast(it)
                }
            }
        }
    }

    private fun navigateToVerifyOtpScreen() {
        binding.apply {
            val i = Intent(this@LoginActivity, VerifyOtpActivity::class.java)
            val b = Bundle()
            b.putBoolean(Constants.IS_DOCTOR_LOGIN, isDoctorLogin)
            if (isDoctorLogin) {
                b.putString(Constants.LOGIN_WITH, etEmail.text.toString().trim())
            } else {
                b.putString(Constants.LOGIN_WITH, etMobile.text.toString().trim())
                b.putString(Constants.COUNTRY_CODE, selectedCountryCode)
            }
            i.putExtras(b)
            startActivity(i)
        }
    }

    private fun getIntentData() {
        binding.apply {
            intent.extras?.run {
                isDoctorLogin = getBoolean(Constants.IS_DOCTOR_LOGIN, false)
                setUpView()
            }
        }
    }

    private fun setUpView() {
        binding.apply {
            if (isDoctorLogin) {
                setDoctorLoginView()
            } else {
                setPatientLoginView()
            }
        }
    }

    private fun setPatientLoginView() {
        binding.apply {
            imgMain.setImageResource(R.drawable.img_login_patient)
            layoutEmail.visibility = INVISIBLE
            layoutMobile.visibility = VISIBLE
            btnContinue.text = getString(R.string.sent_otp)
            tvByClicking.visibility = VISIBLE
            tvTermCondition.visibility = VISIBLE
        }
    }

    private fun setDoctorLoginView() {
        binding.apply {
            imgMain.setImageResource(R.drawable.img_login_doctor)
            layoutEmail.visibility = VISIBLE
            layoutMobile.visibility = INVISIBLE
            btnContinue.text = getString(R.string._continue)
            tvByClicking.visibility = INVISIBLE
            tvTermCondition.visibility = INVISIBLE
        }
    }

    private fun setUpCountryCodes() {

        binding.apply {
            countryList.addAll(AppUtils.INSTANCE?.getCountriesList(this@LoginActivity) ?: ArrayList())
            val adapterCodes =
                ArrayAdapter<String>(this@LoginActivity, android.R.layout.simple_list_item_1, countryList.map { it ->
                    it.dial_code
                })

            etCountry.setAdapter(adapterCodes)
            val usIndex = AppUtils.INSTANCE?.getCountryIndex("+1",countryList) ?: 0
            etCountry.setSelection(usIndex)
            etCountry.setText(countryList[usIndex].dial_code)
            selectedCountryCode = countryList[usIndex].dial_code.toString()
        }
    }

    private fun setUpListener() {
        binding.apply {
            etCountry.setOnItemClickListener { parent, view, position, id ->
                val i  = parent.getItemAtPosition(position)
                selectedCountryCode = i.toString()

            }
            btnBack.setOnClickListener {
                finish()
            }
            btnContinue.setOnClickListener {
                clearErrors()
                if (isDoctorLogin) {
                    val email = etEmail.text.toString().trim()
                    when {
                        email.isEmpty() -> {
                            layoutEmail.error = getString(R.string.please_enter_email_id)
                        }
                        AppUtils.INSTANCE?.isValidEmail(email) == false -> {
                            layoutEmail.error = getString(R.string.please_enter_valid_email_id)
                        }
                        else -> {
                            callDoctorLoginSendOtpApi()
                        }
                    }
                } else {
                    val mobile = etMobile.text.toString().trim()
                    when {
                        mobile.isEmpty() -> {
                            errorText.text = getString(R.string.please_enter_mobile_number)
                            errorText.visibility = VISIBLE
                            layoutMobile.setBackgroundResource(R.drawable.bg_box_error)
                        }
                        mobile.length < 6  -> {
                            errorText.text = getString(R.string.please_enter_valid_mobile_number)
                            errorText.visibility = VISIBLE
                            layoutMobile.setBackgroundResource(R.drawable.bg_box_error)
                        }
                        else -> {
                            callPatientSendOtpApi()
                        }
                    }
                }
            }

            etMobile.onFocusChangeListener =
                View.OnFocusChangeListener { _: View, hasFocus: Boolean ->
                    if (hasFocus) {
                        clearErrors()
                        layoutMobile.setBackgroundResource(R.drawable.bg_box_focused)
                    } else {
                        layoutMobile.setBackgroundResource(R.drawable.bg_box)
                    }
                    val input = etMobile.text.toString().trim()
                    if (!TextUtils.isEmpty(input)) {
                        layoutMobile.setBackgroundResource(R.drawable.bg_box_focused)
                    }
                }

            etEmail.onFocusChangeListener =
                View.OnFocusChangeListener { _: View, hasFocus: Boolean ->
                    if (hasFocus) {
                        clearErrors()
                        layoutEmail.setStartIconTintList(
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    this@LoginActivity,
                                    R.color.azure_radiance
                                )
                            )
                        )
                        layoutEmail.boxStrokeColor =
                            ContextCompat.getColor(this@LoginActivity, R.color.azure_radiance)

                    } else {
                        layoutEmail.setStartIconTintList(
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    this@LoginActivity,
                                    R.color.heather
                                )
                            )
                        )
                        layoutEmail.boxStrokeColor =
                            ContextCompat.getColor(this@LoginActivity, R.color.alabaster)

                    }
                    val input = etEmail.text.toString().trim()
                    if (!TextUtils.isEmpty(input)) {
                        layoutEmail.setStartIconTintList(
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    this@LoginActivity,
                                    R.color.azure_radiance
                                )
                            )
                        )
                    }
                }
        }
    }

    private fun clearErrors() {
        binding.apply {
            layoutEmail.error = ""
            errorText.text = ""
            errorText.visibility = INVISIBLE
            layoutMobile.setBackgroundResource(R.drawable.bg_box)
        }
    }

    private fun callPatientSendOtpApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                loginViewModel.callPatientSendOtpApi(
                    request = PatientSendOtpRequestModel(
                        country_code = selectedCountryCode,
                        number = etMobile.text.toString().trim()
                    )
                )
            } else {
                showToast(getString(R.string.please_check_your_internet_connection))
            }
        }
    }

    private fun toggleLoader(showLoader: Boolean) {
        toggleFadeView(
            binding.root,
            binding.contentLoading.root,
            binding.contentLoading.imageLoading,
            showLoader
        )
    }

    private fun callDoctorLoginSendOtpApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                loginViewModel.callDoctorLoginSendOtpApi(
                    request = DoctorLoginSendOtpRequestModel(
                        etEmail.text.toString().trim()
                    )
                )
            } else {
                showToast(getString(R.string.please_check_your_internet_connection))
            }
        }
    }

}