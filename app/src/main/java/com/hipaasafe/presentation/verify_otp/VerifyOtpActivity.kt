package com.hipaasafe.presentation.verify_otp

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
import com.hipaasafe.listener.ValidationListener
import com.hipaasafe.utils.AppUtils
import com.hipaasafe.utils.ImageUtils
import java.util.*

class VerifyOtpActivity : BaseActivity(), ValidationListener {

    var isFromLoginDoctor: Boolean = false
    var loginWith: String = ""

    lateinit var timer: Timer

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
        binding.ivTimer.visibility = View.INVISIBLE
        binding.tvResendOtp.visibility = View.VISIBLE
    }

    private fun startTimer() {
        stopTimer()
        binding.ivTimer.visibility = View.VISIBLE
        ImageUtils.INSTANCE?.loadLocalGIFImage(binding.ivTimer, R.drawable.timer)
        timer = Timer()
        //        delay - delay in milliseconds before task is to be executed.
//        period - time in milliseconds between successive task executions.
        timer.scheduleAtFixedRate(
            BeatTimerTask(),
            0,
            1000
        )

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
        setListener()
        startTimer()
    }

    private fun getIntentData() {
        intent.extras?.run {
            isFromLoginDoctor = getBoolean(Constants.IS_DOCTOR_LOGIN)
            loginWith = getString(Constants.LOGIN_WITH).toString()
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
            }

            etOtp.onFocusChangeListener =
                View.OnFocusChangeListener { _: View, hasFocus: Boolean ->


                    if (hasFocus) {
                        clearErrorLabels()
                        textInputOtp.setStartIconTintList(
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    this@VerifyOtpActivity,
                                    R.color.azure_radiance
                                )
                            )
                        )
                        textInputOtp.boxStrokeColor =
                            ContextCompat.getColor(this@VerifyOtpActivity, R.color.azure_radiance)

                    } else {
                        textInputOtp.setStartIconTintList(
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    this@VerifyOtpActivity,
                                    R.color.heather
                                )
                            )
                        )
                        textInputOtp.boxStrokeColor =
                            ContextCompat.getColor(this@VerifyOtpActivity, R.color.alabaster)

                    }
                    val input = binding.etOtp.text.toString().trim()
                    if (!TextUtils.isEmpty(input)) {
                        textInputOtp.setStartIconTintList(
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


    private fun toggleLoader(showLoader: Boolean) {
        toggleFadeView(
            binding.root,
            binding.contentLoading.layoutLoading,
            binding.contentLoading.imageLoading,
            showLoader
        )
    }

    override fun onValidationSuccess(type: String, msg: Int) {

    }

    override fun onValidationFailure(type: String, msg: Int) {
        clearErrorLabels()
        binding.textInputOtp.error = getString(msg)
    }

    private fun clearErrorLabels() {
        binding.textInputOtp.error = ""
    }


    private fun callLoginSendOtpApi() {
        toggleLoader(true)
    }

    private fun callSendOtpApi() {
        BaseApplication.second = Constants.RESEND_OTP_SECOND
        if (isFromLoginDoctor) {
            callLoginSendOtpApi()

        } else {
            callRegisterSendOtpApi()
        }

    }

    private fun callRegisterSendOtpApi() {

    }
}