package com.hipaasafe.presentation.past_appointments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivityPastAppointmentsBinding
import com.hipaasafe.domain.model.appointment.AppointmentItemsModel
import com.hipaasafe.domain.model.appointment.GetAppointmentResponseModel
import com.hipaasafe.domain.model.appointment.GetAppointmentsRequestModel
import com.hipaasafe.domain.model.appointment.GetDoctorPastAppointmentsRequestModel
import com.hipaasafe.presentation.home_screen.appointment_fragment.AppointmentViewModel
import com.hipaasafe.presentation.past_appointments.adapter.PastAppointmentHistoryAdapter
import com.hipaasafe.presentation.past_appointments.model.PastAppointmentHistoryModel
import com.hipaasafe.utils.AppUtils
import com.hipaasafe.utils.PreferenceUtils
import com.hipaasafe.utils.enum.LoginUserType
import com.hipaasafe.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

class PastAppointmentsActivity : BaseActivity() {
    lateinit var binding: ActivityPastAppointmentsBinding
    lateinit var pastAppointmentHistoryAdapter: PastAppointmentHistoryAdapter
    private var pastAppointmentHistoryList: ArrayList<PastAppointmentHistoryModel> = ArrayList()
    private val appointmentViewModel: AppointmentViewModel by viewModel()
    var pageNo: Int = 1
    var isLoading: Boolean = true
    var loginUserType: Int = 0
    var loginUserId: String = ""
    var fromDate: String = ""
    var toDate: String = ""
    var selectedFromDate: Calendar? = null
    var isCalendarClick:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPastAppointmentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferenceUtils = PreferenceUtils(this)
        getPreferenceData()
        binding.apply {
            if (loginUserType == LoginUserType.PATIENT.value) {
                callPastAppointmentsApi()
                etFromDate.visibility = GONE
                etToDate.visibility = GONE
            } else {
                val currentDate = AppUtils.INSTANCE?.getCurrentDate().toString()
                toDate =
                    AppUtils.INSTANCE?.convertDateFormat("dd MMM yyyy", currentDate, "yyyy-MM-dd")
                        .toString()
                fromDate = AppUtils.INSTANCE?.getPreviousDate(3).toString()
                callDoctorPastAppointmentApi(fromDate = fromDate, toDate = toDate)
                etFromDate.visibility = VISIBLE
                etToDate.visibility = VISIBLE
            }
        }
        setUpObserver()
        setUpToolbar()
        setUpAdapter()
        setUpListener()
    }

    private fun callDoctorPastAppointmentApi(fromDate: String, toDate: String) {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                layoutNoInternet.root.visibility = GONE
                recyclerPastAppointments.visibility = VISIBLE
                appointmentViewModel.callGetDoctorPastAppointmentsListApi(
                    request = GetDoctorPastAppointmentsRequestModel(
                        from_date = fromDate,
                        to_date = toDate,
                        doctor_id = loginUserId
                    )
                )
            } else {
                layoutNoInternet.root.visibility = VISIBLE
                recyclerPastAppointments.visibility = GONE
            }
        }
    }

    private fun getPreferenceData() {
        binding.apply {
            loginUserType =
                preferenceUtils.getValue(Constants.PreferenceKeys.role_id).toIntOrNull() ?: 0
            loginUserId = preferenceUtils.getValue(Constants.PreferenceKeys.uid)
        }
    }

    private fun setUpListener() {
        binding.apply {
            swipeMyPatient.setOnRefreshListener {
                swipeMyPatient.isRefreshing = false
                if (loginUserType == LoginUserType.PATIENT.value) {
                    callPastAppointmentsApi()
                } else {
                    callDoctorPastAppointmentApi(fromDate = fromDate, toDate = toDate)
                }
            }
            layoutNoInternet.btnRetry.setOnClickListener {
                if (loginUserType == LoginUserType.PATIENT.value) {
                    callPastAppointmentsApi()
                } else {
                    callDoctorPastAppointmentApi(fromDate = fromDate, toDate = toDate)
                }
            }

            etFromDate.setOnClickListener {
                val cal = Calendar.getInstance()
                val y = cal.get(Calendar.YEAR)
                val m = cal.get(Calendar.MONTH)
                val d = cal.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    this@PastAppointmentsActivity,
                    { view, year, monthOfYear, dayOfMonth ->

                        val selectedCalendar = Calendar.getInstance()
                        selectedCalendar.set(Calendar.YEAR, year)
                        selectedCalendar.set(Calendar.MONTH, monthOfYear)
                        selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        selectedFromDate = selectedCalendar
                        val selectedDate =
                            AppUtils.INSTANCE?.convertDateToString(
                                selectedCalendar.time,
                                "yyyy-MM-dd"
                            )
                        etFromDate.setText(selectedDate)
                        etToDate.setText("")
                    },
                    y,
                    m,
                    d
                )
                datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
                datePickerDialog.show()
            }

            etToDate.setOnClickListener {
                val cal = Calendar.getInstance()
                val y = cal.get(Calendar.YEAR)
                val m = cal.get(Calendar.MONTH)
                val d = cal.get(Calendar.DAY_OF_MONTH)


                val datePickerDialog = DatePickerDialog(
                    this@PastAppointmentsActivity,
                    { view, year, monthOfYear, dayOfMonth ->
                        val selectedCalendar = Calendar.getInstance()
                        selectedCalendar.set(Calendar.YEAR, year)
                        selectedCalendar.set(Calendar.MONTH, monthOfYear)
                        selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        val selectedDate =
                            AppUtils.INSTANCE?.convertDateToString(
                                selectedCalendar.time,
                                "yyyy-MM-dd"
                            )
                        etToDate.setText(selectedDate)
                        if (etFromDate.text.toString().trim()
                                .isNotEmpty() && etToDate.text.toString().trim().isNotEmpty()
                        ) {
                            fromDate = etFromDate.text.toString().trim()
                            toDate = etToDate.text.toString().trim()
                            callDoctorPastAppointmentApi(
                                fromDate = fromDate,
                                toDate = toDate
                            )
                            isCalendarClick = true
                        } else {
                            showToast("Please select from date")
                        }
                    },
                    y,
                    m,
                    d
                )
                if (selectedFromDate == null) {
                    datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
                } else {
                    datePickerDialog.datePicker.minDate = selectedFromDate?.time?.time!!
                    datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
                }
                datePickerDialog.show()
            }

            recyclerPastAppointments.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                    if (!recyclerView.canScrollVertically(1)){
//                        if (isLoading) {
//                            isLoading = false
//                            pageNo += 1
//                            callPastAppointmentsApi()
//                        }
//                    }
                }
            })
        }
    }

    private fun setUpObserver() {
        binding.apply {
            with(appointmentViewModel) {
                getAppointmentsResponseData.observe(this@PastAppointmentsActivity) {
                    toggleLoader(false)
                    if (it.success) {
                        if (it.data != null && it.data.count != 0) {
                            layoutNoData.root.visibility = GONE
//                            isLoading = true
                            pastAppointmentHistoryList.clear()
                            setUpAdapter(it.data.rows)
                        } else {
                            isLoading = false
                            layoutNoData.root.visibility = VISIBLE
                        }
                    } else {
                        showToast(it.message.toString())
                    }
                }

                getDoctorPastAppointmentsListResponseData.observe(this@PastAppointmentsActivity) {
                    toggleLoader(false)
                    if (it.success == true) {
                        if (it.data != null) {
                            layoutNoData.root.visibility = GONE
                            setUpAdapter(it.data)
                        } else {
                            layoutNoData.root.visibility = VISIBLE
                        }
                    } else {
                        showToast(it.message.toString())
                    }
                }

                messageData.observe(this@PastAppointmentsActivity) {
                    toggleLoader(false)
                    showToast(it.toString())
                }
            }
        }
    }


    private fun toggleLoader(showLoader: Boolean) {
        binding.apply {
            toggleFadeView(
                root,
                contentLoading.layoutLoading,
                contentLoading.imageLoading,
                showLoader
            )
        }
    }

    private fun getAppointmentsListRequestModel(): GetAppointmentsRequestModel {
        val request = GetAppointmentsRequestModel()
        request.page = pageNo
        request.limit = 30
        request.type = Constants.PAST
        return request
    }

    private fun callPastAppointmentsApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                layoutNoInternet.root.visibility = GONE
                recyclerPastAppointments.visibility = VISIBLE
                appointmentViewModel.callGetAppointmentsListApi(request = getAppointmentsListRequestModel())
            } else {
                layoutNoInternet.root.visibility = VISIBLE
                recyclerPastAppointments.visibility = GONE
            }
        }
    }

    private fun setUpAdapter(list: ArrayList<AppointmentItemsModel>? = ArrayList()) {
        binding.apply {
            if (::pastAppointmentHistoryAdapter.isInitialized) {
                if (isCalendarClick){
                    pastAppointmentHistoryList.clear()
                }
                for (i in list ?: arrayListOf()) {
                    val name =
                        if (loginUserType == LoginUserType.PATIENT.value) i.appointment_doctor_details?.name else i.patient_details?.name
                    pastAppointmentHistoryList.add(
                        PastAppointmentHistoryModel(
                            title = name,
                            date = i.appointment_date,
                            time = i.appointment_time,
                            age = i.patient_details?.age ?: ""
                        )
                    )
                }
                pastAppointmentHistoryAdapter.notifyDataSetChanged()
            } else {
                pastAppointmentHistoryList.clear()
                for (i in list ?: arrayListOf()) {
                    val name =
                        if (loginUserType == LoginUserType.PATIENT.value) i.appointment_doctor_details?.name else i.patient_details?.name
                    pastAppointmentHistoryList.add(
                        PastAppointmentHistoryModel(
                            title = name,
                            date = i.appointment_date,
                            time = i.appointment_time,
                            age = i.patient_details?.age ?: ""
                        )
                    )
                }
                pastAppointmentHistoryAdapter = PastAppointmentHistoryAdapter(
                    this@PastAppointmentsActivity,
                    pastAppointmentHistoryList
                )
                recyclerPastAppointments.adapter = pastAppointmentHistoryAdapter
            }
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.past_appointments)
            tvDate.visibility = GONE
            btnBack.visibility = VISIBLE
            btnBack.setOnClickListener {
                finish()
            }
            divider.visibility = VISIBLE
        }
    }
}