package com.hipaasafe.presentation.sign_up

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivitySignUpBinding
import com.hipaasafe.domain.model.patient_login.PatientRegisterRequestModel
import com.hipaasafe.domain.model.patient_login.UserRegisterDataModel
import com.hipaasafe.listener.ValidationListener
import com.hipaasafe.presentation.home_screen.HomeActivity
import com.hipaasafe.presentation.login.LoginViewModel
import com.hipaasafe.utils.PreferenceUtils
import com.hipaasafe.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class SignUpActivity : BaseActivity(), ValidationListener {
    var countryCode: String = ""
    var mobileNo: String = ""
    lateinit var binding: ActivitySignUpBinding
    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferenceUtils = PreferenceUtils(this)
        loginViewModel.validationListener = this
        getIntentData()
        setUpListener()
        setUpObserver()
    }

    private fun setUpObserver() {
        binding.apply {
            with(loginViewModel) {
                patientRegisterResponseData.observe(this@SignUpActivity, {
                    toggleLoader(false)
                    if (it.success) {
                        savePatientData(it.data)
                        navigateToHome()
                    } else {
                        showToast(it.message)
                    }
                })

                messageData.observe(this@SignUpActivity, {
                    toggleLoader(false)
                    showToast(it.toString())
                })
            }
        }
    }

    private fun navigateToHome() {
        binding.apply {
            val i = Intent(this@SignUpActivity, HomeActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(i)
            finish()
        }
    }

    private fun savePatientData(data: UserRegisterDataModel) {
        binding.apply {
            preferenceUtils.apply {
                setValue(Constants.IS_LOGIN, true)
                setValue(Constants.PreferenceKeys.id, data.id.toString())
                setValue(Constants.PreferenceKeys.uid, data.uid.toString())
                setValue(Constants.PreferenceKeys.name, data.name.toString())
                setValue(Constants.PreferenceKeys.email, data.email.toString())
                setValue(Constants.PreferenceKeys.country_code, data.country_code.toString())
                setValue(Constants.PreferenceKeys.number, data.number.toString())
                setValue(Constants.PreferenceKeys.role_id, data.role_id.toString())
                setValue(Constants.PreferenceKeys.role_name, data.role_name.toString())
                setValue(Constants.PreferenceKeys.avatar, data.avatar.toString())
                setValue(Constants.PreferenceKeys.organization_id, data.organization_id.toString())
                setValue(
                    Constants.PreferenceKeys.mute_notifications,
                    data.mute_notifications ?: false
                )
                setValue(
                    Constants.PreferenceKeys.profile_update,
                    data.patient_details?.profile_update ?: false
                )
            }
        }
    }

    private fun getPatientRequestModelData():PatientRegisterRequestModel{
        binding.apply {
            val request = PatientRegisterRequestModel()
            request.name = etName.text.toString().trim()
            request.email = etEmail.text.toString().trim()
            request.age = etAge.text.toString().trim().toIntOrNull()?:0
            return request
        }
    }

    private fun setUpListener() {
        binding.apply {
            btnBack.setOnClickListener {
                finish()
            }
            btnContinue.setOnClickListener {
                clearErrors()
                loginViewModel.validatePatientRegisterData(request = getPatientRequestModelData())
            }
        }
    }

    private fun getIntentData() {
        binding.apply {
            intent.extras?.run {
                mobileNo = getString(Constants.LOGIN_WITH).toString()
                countryCode = getString(Constants.COUNTRY_CODE).toString()
                setUpView()
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

    private fun setUpView() {
        binding.apply {
            val clist: ArrayList<String> = ArrayList()
            clist.clear()
            clist.add(countryCode)
            val adapterCodes =
                ArrayAdapter<String>(
                    this@SignUpActivity,
                    android.R.layout.simple_list_item_1,
                    clist
                )
            binding.spinnerCountryCode.adapter = adapterCodes

            etMobile.setText(mobileNo)
        }
    }

    override fun onValidationSuccess(type: String, msg: Int) {
        callPatientRegisterApi()
    }

    private fun callPatientRegisterApi() {
        binding.apply {
            if (isNetworkAvailable()){
                toggleLoader(true)
                loginViewModel.callPatientRegisterApi(getPatientRequestModelData())
            }else{
                showToast(getString(R.string.please_check_your_internet_connection))
            }
        }
    }

    override fun onValidationFailure(type: String, msg: Int) {
        binding.apply {
            when(type){
                Constants.ErrorMsg.NAME_ERROR -> {
                    layoutName.error = getString(msg)
                }
                Constants.ErrorMsg.EMAIL_ERROR -> {
                    layoutEmail.error = getString(msg)
                }
                Constants.ErrorMsg.AGE_ERROR -> {
                    layoutAge.error = getString(msg)
                }
            }
        }
    }

    private fun clearErrors(){
        binding.apply {
            layoutName.error = ""
            layoutEmail.error = ""
            layoutAge.error = ""
        }
    }
}