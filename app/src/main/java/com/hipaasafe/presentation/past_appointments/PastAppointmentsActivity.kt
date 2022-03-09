package com.hipaasafe.presentation.past_appointments

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
import com.hipaasafe.presentation.home_screen.appointment_fragment.AppointmentViewModel
import com.hipaasafe.presentation.past_appointments.adapter.PastAppointmentHistoryAdapter
import com.hipaasafe.presentation.past_appointments.model.PastAppointmentHistoryModel
import com.hipaasafe.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class PastAppointmentsActivity : BaseActivity() {
    lateinit var binding: ActivityPastAppointmentsBinding
    lateinit var pastAppointmentHistoryAdapter: PastAppointmentHistoryAdapter
    private var pastAppointmentHistoryList: ArrayList<PastAppointmentHistoryModel> = ArrayList()
    private val appointmentViewModel: AppointmentViewModel by viewModel()
    var pageNo: Int = 1
    var isLoading: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPastAppointmentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        callPastAppointmentsApi()
        setUpObserver()
        setUpToolbar()
        setUpAdapter()
        setUpListener()
    }

    private fun setUpListener() {
        binding.apply {

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
                            isLoading = true
                            setUpAdapter(it.data.rows)
                        } else {
                            isLoading = false
                            showToast("no data ")
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
                appointmentViewModel.callGetAppointmentsListApi(request = getAppointmentsListRequestModel())
            } else {
                showToast(getString(R.string.please_check_your_internet_connection))
            }
        }
    }

    private fun setUpAdapter(list: ArrayList<AppointmentItemsModel>? = ArrayList()) {
        binding.apply {
            if (::pastAppointmentHistoryAdapter.isInitialized) {
                for (i in list ?: arrayListOf()) {
                    pastAppointmentHistoryList.add(
                        PastAppointmentHistoryModel(
                            title = i.appointment_doctor_details.name,
                            date = i.appointment_date,
                            time = i.appointment_time
                        )
                    )
                }
                pastAppointmentHistoryAdapter.notifyDataSetChanged()
            } else {
                pastAppointmentHistoryList.clear()
                for (i in list ?: arrayListOf()) {
                    pastAppointmentHistoryList.add(
                        PastAppointmentHistoryModel(
                            title = i.appointment_doctor_details.name,
                            date = i.appointment_date,
                            time = i.appointment_time
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