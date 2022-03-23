package com.hipaasafe.presentation.past_appointments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivityPastAppointmentsBinding
import com.hipaasafe.databinding.BottomsheetForwardDocBinding
import com.hipaasafe.domain.model.appointment.AppointmentItemsModel
import com.hipaasafe.domain.model.appointment.GetAppointmentResponseModel
import com.hipaasafe.domain.model.appointment.GetAppointmentsRequestModel
import com.hipaasafe.domain.model.appointment.GetDoctorPastAppointmentsRequestModel
import com.hipaasafe.domain.model.doctor_login.DoctorsMappedModel
import com.hipaasafe.presentation.home_screen.appointment_fragment.AppointmentViewModel
import com.hipaasafe.presentation.home_screen.document_fragment.adapter.ForwardDocAdapter
import com.hipaasafe.presentation.home_screen.document_fragment.model.ForwardDocumentModel
import com.hipaasafe.presentation.past_appointments.adapter.PastAppointmentHistoryAdapter
import com.hipaasafe.presentation.past_appointments.model.PastAppointmentHistoryModel
import com.hipaasafe.utils.AppUtils
import com.hipaasafe.utils.ImageUtils
import com.hipaasafe.utils.PreferenceUtils
import com.hipaasafe.utils.enum.LoginUserType
import com.hipaasafe.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

class PastAppointmentsActivity : BaseActivity(), ForwardDocAdapter.ForwardClickManager {
    lateinit var binding: ActivityPastAppointmentsBinding
    lateinit var pastAppointmentHistoryAdapter: PastAppointmentHistoryAdapter
    private var pastAppointmentHistoryList: ArrayList<PastAppointmentHistoryModel> = ArrayList()
    private val appointmentViewModel: AppointmentViewModel by viewModel()
    var pageNo: Int = 1
    var isLoading: Boolean = true
    var loginUserType: Int = 0
    var fromDate: String = ""
    var toDate: String = ""
    var selectedFromDate: Calendar? = null
    var isCalendarClick:Boolean = false

    var selectedDoctorId:String =""
    var doctorsListForNurse:ArrayList<DoctorsMappedModel> = ArrayList()
    var doctorsList:ArrayList<ForwardDocumentModel> = ArrayList()
    lateinit var bottomSheetDialogDoctor: BottomSheetDialog
    lateinit var bottomSheetSelectDoctorBinding: BottomsheetForwardDocBinding
    private lateinit var doctorListAdapter: ForwardDocAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPastAppointmentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferenceUtils = PreferenceUtils(this)
        getPreferenceData()
        binding.apply {
            when (loginUserType) {
                LoginUserType.PATIENT.value -> {
                    callPastAppointmentsApi()
                    etFromDate.visibility = GONE
                    etToDate.visibility = GONE
                    layoutSelectDoctor.visibility = GONE
                }
                LoginUserType.NURSE.value -> {
                    val currentDate = AppUtils.INSTANCE?.getCurrentDate().toString()
                    toDate =
                        AppUtils.INSTANCE?.convertDateFormat("dd MMM yyyy", currentDate, "yyyy-MM-dd")
                            .toString()
                    fromDate = AppUtils.INSTANCE?.getPreviousDate(3).toString()
                    etFromDate.setText(fromDate)
                    etToDate.setText(toDate)
                    callDoctorPastAppointmentApi(fromDate = fromDate, toDate = toDate)
                    etFromDate.visibility = VISIBLE
                    etToDate.visibility = VISIBLE
                    layoutSelectDoctor.visibility = VISIBLE
                }
                else -> {
                    val currentDate = AppUtils.INSTANCE?.getCurrentDate().toString()
                    toDate =
                        AppUtils.INSTANCE?.convertDateFormat("dd MMM yyyy", currentDate, "yyyy-MM-dd")
                            .toString()
                    fromDate = AppUtils.INSTANCE?.getPreviousDate(3).toString()
                    etFromDate.setText(fromDate)
                    etToDate.setText(toDate)
                    callDoctorPastAppointmentApi(fromDate = fromDate, toDate = toDate)
                    etFromDate.visibility = VISIBLE
                    etToDate.visibility = VISIBLE
                    layoutSelectDoctor.visibility = GONE
                }
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
                        doctor_id = selectedDoctorId
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
            loginUserType = preferenceUtils.getValue(Constants.PreferenceKeys.role_id).toIntOrNull()?:0
            if (loginUserType == LoginUserType.NURSE.value){
                layoutSelectDoctor.visibility = VISIBLE
                val collectionType: Type = object : TypeToken<ArrayList<DoctorsMappedModel>>() {}.type
                val data = Gson().fromJson<ArrayList<DoctorsMappedModel>>(preferenceUtils.getValue(Constants.PreferenceKeys.doctorsMappedModel),collectionType)
                doctorsListForNurse.clear()
                doctorsListForNurse.addAll(data)
                setNurseUI(0)
            }else{
                selectedDoctorId = preferenceUtils.getValue(Constants.PreferenceKeys.uid)
                layoutSelectDoctor.visibility = GONE
            }
        }
    }

    private fun setNurseUI(position: Int) {
        binding.apply {
            if (doctorsListForNurse.size != 0){
                selectedDoctorId = doctorsListForNurse[position].uid.toString()
                hintSelectDoctor.visibility = GONE
                imgProfile.visibility = VISIBLE
                tvDoctorName.visibility = VISIBLE
                ImageUtils.INSTANCE?.loadRemoteImageForProfile(imgProfile,doctorsListForNurse[position].avatar)
                tvDoctorName.text = doctorsListForNurse[position].name
                doctorsList.clear()
                for (i in doctorsListForNurse){
                    doctorsList.add(ForwardDocumentModel(
                        title = i.name,
                        icon = i.avatar,
                        doctorId = i.uid
                    ))
                }
                if (::doctorListAdapter.isInitialized){
                    doctorListAdapter.notifyDataSetChanged()
                }
            }else{
                hintSelectDoctor.visibility = VISIBLE
            }
        }
    }

    private fun setUpListener() {
        binding.apply {

            btnDown.setOnClickListener {
                if (doctorsListForNurse.size != 0){
                    openDoctorsListBottomSheet()
                }
            }
            tvDoctorName.setOnClickListener {
                openDoctorsListBottomSheet()
            }

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

    private fun openDoctorsListBottomSheet() {
        bottomSheetDialogDoctor = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottomsheet_forward_doc, null)
        bottomSheetSelectDoctorBinding = BottomsheetForwardDocBinding.bind(view)
        bottomSheetDialogDoctor.setContentView(view)
        bottomSheetDialogDoctor.setCancelable(true)
        bottomSheetSelectDoctorBinding.apply {
            btnShare.visibility = GONE
            imgClose.setOnClickListener {
                bottomSheetDialogDoctor.dismiss()
            }
            doctorListAdapter = ForwardDocAdapter(
                this@PastAppointmentsActivity,
                doctorsList,
                listener = this@PastAppointmentsActivity, isHideCheck = true
            )
            recyclerAttendanceHistory.adapter = doctorListAdapter
        }
        bottomSheetDialogDoctor.show()
    }

    private fun setUpObserver() {
        binding.apply {
            with(appointmentViewModel) {
                getAppointmentsResponseData.observe(this@PastAppointmentsActivity) {
                    toggleLoader(false)
                    pastAppointmentHistoryList.clear()
                    if (it.success) {
                        if (it.data != null && it.data.count != 0) {
                            layoutNoData.root.visibility = GONE
//                            isLoading = true
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
                    pastAppointmentHistoryList.clear()
                    if (it.success == true) {
                        if (it.data != null && it.data.size != 0) {
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
                            dob = i.patient_details?.dob ?: ""
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
                            dob = i.patient_details?.dob ?: ""
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

    override fun onItemClick(position: Int) {
        bottomSheetDialogDoctor.dismiss()
        setNurseUI(position)
        callDoctorPastAppointmentApi(fromDate, toDate)
    }
}