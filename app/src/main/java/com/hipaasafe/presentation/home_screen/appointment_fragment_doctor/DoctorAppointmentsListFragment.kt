package com.hipaasafe.presentation.home_screen.appointment_fragment_doctor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.hipaasafe.R
import com.hipaasafe.base.BaseFragment
import com.hipaasafe.databinding.FragmentDoctorAppointmentsListBinding
import com.hipaasafe.domain.model.appointment.DoctorAppointmentListModel
import com.hipaasafe.domain.model.appointment.DoctorAppointmentsRequestModel
import com.hipaasafe.presentation.home_screen.HomeActivity
import com.hipaasafe.presentation.home_screen.appointment_fragment.AppointmentViewModel
import com.hipaasafe.presentation.home_screen.appointment_fragment_doctor.adapter.AppointmentsListAdapter
import com.hipaasafe.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class DoctorAppointmentsListFragment : BaseFragment() {

    lateinit var binding: FragmentDoctorAppointmentsListBinding
    var doctorAppointmentList: ArrayList<DoctorAppointmentListModel> = ArrayList()
    var pageNo: Int = 1
    lateinit var parentFr: DoctorAppointmentFragment

    private val appointmentViewModel: AppointmentViewModel by viewModel()

    companion object {
        fun newInstance(): DoctorAppointmentsListFragment {
            return DoctorAppointmentsListFragment()
        }
    }

    lateinit var appointmentsListAdapter: AppointmentsListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDoctorAppointmentsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentFr = (requireActivity() as HomeActivity).homeFragmentDoctor.doctorAppointmentFragment
        setUpObserver()
        setUpAdapter()
        setUpListener()
        callDoctorAppointmentListApi(
            date = parentFr.nextSevenDaysList[parentFr.selectedTabPosition].date.toString(),
            parentFr.loginDoctorId
        )
    }

    private fun setUpListener() {
        binding.apply {
            layoutNoInternet.btnRetry.setOnClickListener {
                callDoctorAppointmentListApi(
                    date = parentFr.nextSevenDaysList[parentFr.selectedTabPosition].date.toString(),
                    parentFr.loginDoctorId
                )
            }
        }
    }

    private fun setUpObserver() {
        binding.apply {
            with(appointmentViewModel) {
                doctorAppointmentsListResponseData.observe(requireActivity()) {
                    toggleLoader(false)
                    if (it.data.count != 0 && it.data.count != null) {
                        layoutNoAppointments.root.visibility = GONE
                        recyclerDoctorAppointments.visibility = VISIBLE
                        doctorAppointmentList.clear()
                        doctorAppointmentList.addAll(it.data.rows)
                    } else {
                        layoutNoAppointments.root.visibility = VISIBLE
                        layoutNoAppointments.tvInfo.text =
                            getString(R.string.no_appointments_for) + " " + parentFr.nextSevenDaysList[parentFr.selectedTabPosition].label
                        recyclerDoctorAppointments.visibility = GONE
                    }
                    setUpAdapter()
                }
                messageData.observe(requireActivity()) {
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

    fun callDoctorAppointmentListApi(date: String, doctorId: String) {
        binding.apply {
            if (requireContext().isNetworkAvailable()) {
                toggleLoader(true)
                layoutNoInternet.root.visibility = GONE
                recyclerDoctorAppointments.visibility = VISIBLE
                appointmentViewModel.callDoctorAppointmentsListApi(
                    request =
                    DoctorAppointmentsRequestModel(
                        pageNo, limit = 10, date = date, doctor_id = doctorId
                    )
                )
            } else {
                layoutNoInternet.root.visibility = VISIBLE
                recyclerDoctorAppointments.visibility = GONE
            }
        }
    }

    private fun setUpAdapter() {
        binding.apply {
            if (::appointmentsListAdapter.isInitialized) {
                appointmentsListAdapter.notifyDataSetChanged()
            } else {
                appointmentsListAdapter =
                    AppointmentsListAdapter(requireContext(), doctorAppointmentList)
                recyclerDoctorAppointments.adapter = appointmentsListAdapter
            }
        }
    }

}