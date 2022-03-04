package com.hipaasafe.presentation.verify_otp

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.KeyboardUtils
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.base.BaseApplication
import com.hipaasafe.databinding.ActivityVerifyOtpBinding
import com.hipaasafe.domain.model.doctor_login.DoctorLoginSendOtpRequestModel
import com.hipaasafe.domain.model.doctor_login.DoctorLoginValidateOtpRequestModel
import com.hipaasafe.domain.model.patient_login.PatientSendOtpRequestModel
import com.hipaasafe.domain.model.patient_login.PatientValidateOtpRequestModel
import com.hipaasafe.presentation.home_screen.HomeActivity
import com.hipaasafe.presentation.login.LoginViewModel
import com.hipaasafe.presentation.sign_up.SignUpActivity
import com.hipaasafe.utils.AppUtils
import com.hipaasafe.utils.ImageUtils
import com.hipaasafe.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class VerifyOtpActivity : BaseActivity() {

    var isFromLoginDoctor: Boolean = false
    var loginWith: String = ""
    var countryCode: String = ""

    lateinit var timer: Timer
    private val loginViewModel: LoginViewModel by viewModel()


    inner class BeatTimerTask : TimerTask() {

        override fun run() {
            runOnUiThread(Runnable {
                val secondsLeft = BaseApplication.second
                if (secondsLeft == 0L) {
                    BaseApplication.second = 0
                    showResendOtpUi()
                } else {
                    BaseApplication.second = secondsLeft - 1000
                    showTimeLeft()
                }
            })
        }

    }

    private fun showTimeLeft() {
        binding.tvLoad.text = AppUtils.INSTANCE?.convertSecondToTime(BaseApplication.second)
    }

    private fun showResendOtpUi() {
        binding.apply {
            ivTimer.visibility = View.INVISIBLE
            tvResendOtp.visibility = View.VISIBLE
        }
    }

    private fun startTimer() {
        stopTimer()
        binding.apply {
            ivTimer.visibility = View.VISIBLE
            ImageUtils.INSTANCE?.loadLocalGIFImage(ivTimer, R.drawable.timer)
            timer = Timer()
            //        delay - delay in milliseconds before task is to be executed.
//        period - time in milliseconds between successive task executions.
            timer.scheduleAtFixedRate(
                BeatTimerTask(),
                0,
                1000
            )
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
    }

    private fun stopTimer() {
        if (::timer.isInitialized) {
            timer.let {
                it.cancel()
                it.purge()
            }
        }

    }

    lateinit var binding: ActivityVerifyOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        BaseApplication.second = Constants.RESEND_OTP_SECOND
        getIntentData()
        setUpObserver()
        setListener()
        startTimer()
    }

    private fun setUpObserver() {
        binding.apply {
            with(loginViewModel) {
                doctorLoginSendOtpResponseData.observe(this@VerifyOtpActivity, {
                    toggleLoader(false)
                    if (it.success) {
                        startTimer()
                        showToast("Resent OTP")
                    } else {
                        showToast(it.message)
                    }
                })
                patientSendOtpResponseData.observe(this@VerifyOtpActivity, {
                    toggleLoader(false)
                    if (it.success) {
                        startTimer()
                        showToast("Resent OTP")
                    } else {
                        showToast(it.message)
                    }
                })
                doctorLoginValidateOtpResponseData.observe(this@VerifyOtpActivity, {
                    toggleLoader(false)
                    if (it.success) {
                        val i = Intent(this@VerifyOtpActivity, HomeActivity::class.java)
                        startActivity(i)
                    } else {
                        showToast(it.message)
                    }
                })
                patientValidateOtpResponseData.observe(this@VerifyOtpActivity, {
                    toggleLoader(false)
                    if (it.success) {
                        //TODO check Condition
                        val i = Intent(this@VerifyOtpActivity, SignUpActivity::class.java)
                        val b = Bundle()
                        b.putString(Constants.LOGIN_WITH, loginWith)
                        b.putString(Constants.COUNTRY_CODE, countryCode)
                        i.putExtras(b)
                        startActivity(i)
                    } else {
                        showToast(it.message)
                    }
                })
                messageData.observe(this@VerifyOtpActivity, {
                    toggleLoader(false)
                    showToast(it.toString())
                })
            }
        }
    }

    private fun getIntentData() {
        intent.extras?.run {
            isFromLoginDoctor = getBoolean(Constants.IS_DOCTOR_LOGIN)
            loginWith = getString(Constants.LOGIN_WITH).toString()
            countryCode = getString(Constants.COUNTRY_CODE).toString()
            setUpViews()
        }
    }

    private fun setUpViews() {
        val text =
            "<font color=#1C1C1C>" + getString(R.string.didn_t_received_the_otp) + "</font><font color=#0098FF> " + getString(
                R.string.resend_otp
            ) + "</font>"
        binding.apply {
            tvResendOtp.text = Html.fromHtml(text)
            tvEmail.text = loginWith
        }
    }

    private fun setListener() {
        binding.apply {
            tvResendOtp.setOnClickListener {
                KeyboardUtils.hideSoftInput(this@VerifyOtpActivity)
                clearErrorLabels()
                callSendOtpApi()
            }
            btnVerify.setOnClickListener {
                KeyboardUtils.hideSoftInput(this@VerifyOtpActivity)
                clearErrorLabels()
                val otp = etOtp.text.toString().trim()
                when {
                    otp.isEmpty() -> {
                        layoutOtp.error = getString(R.string.please_enter_otp)
                    }
                    otp.length < 4 -> {
                        layoutOtp.error = getString(R.string.please_enter_4_digit_otp)
                    }
                    else -> {
                        callVerifyOtpApi()
                    }
                }
            }

            btnBack.setOnClickListener {
                finish()
            }

            etOtp.onFocusChangeListener =
                View.OnFocusChangeListener { _: View, hasFocus: Boolean ->


                    if (hasFocus) {
                        clearErrorLabels()
                        layoutOtp.setStartIconTintList(
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    this@VerifyOtpActivity,
                                    R.color.azure_radiance
                                )
                            )
                        )
                        layoutOtp.boxStrokeColor =
                            ContextCompat.getColor(this@VerifyOtpActivity, R.color.azure_radiance)

                    } else {
                        layoutOtp.setStartIconTintList(
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    this@VerifyOtpActivity,
                                    R.color.heather
                                )
                            )
                        )
                        layoutOtp.boxStrokeColor =
                            ContextCompat.getColor(this@VerifyOtpActivity, R.color.alabaster)

                    }
                    val input = binding.etOtp.text.toString().trim()
                    if (!TextUtils.isEmpty(input)) {
                        layoutOtp.setStartIconTintList(
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    this@VerifyOtpActivity,
                                    R.color.azure_radiance
                                )
                            )
                        )
                    }
                }
        }
    }

    private fun callVerifyOtpApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                if (isFromLoginDoctor) {
                    loginViewModel.callDoctorLoginValidateOtpApi(
                        request =
                        DoctorLoginValidateOtpRequestModel(
                            loginWith, otp = etOtp.text.toString().trim().toIntOrNull() ?: 0
                        )
                    )
                } else {
                    loginViewModel.callPatientValidateOtpApi(
                        request = PatientValidateOtpRequestModel(
                            country_code = countryCode,
                            number = loginWith,
                            otp = etOtp.text.toString().trim().toIntOrNull() ?: 0
                        )
                    )
                }
            } else {
                showToast(getString(R.string.please_check_your_internet_connection))
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

    private fun clearErrorLabels() {
        binding.layoutOtp.error = ""
    }


    private fun callDoctorLoginSendOtpApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                loginViewModel.callDoctorLoginSendOtpApi(
                    request = DoctorLoginSendOtpRequestModel(
                        loginWith
                    )
                )
            } else {
                showToast(getString(R.string.please_check_your_internet_connection))
            }
        }
    }

    private fun callPatientSendOtpApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                loginViewModel.callPatientSendOtpApi(
                    request = PatientSendOtpRequestModel(
                        country_code = countryCode,
                        number = loginWith
                    )
                )
            } else {
                showToast(getString(R.string.please_check_your_internet_connection))
            }
        }
    }

    private fun callSendOtpApi() {
        BaseApplication.second = Constants.RESEND_OTP_SECOND
        if (isFromLoginDoctor) {
            callDoctorLoginSendOtpApi()
        } else {
            callPatientSendOtpApi()
        }

    }

    private fun callRegisterSendOtpApi() {

    }
}