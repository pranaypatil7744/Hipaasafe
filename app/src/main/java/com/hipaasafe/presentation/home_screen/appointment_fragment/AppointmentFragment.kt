package com.hipaasafe.presentation.home_screen.appointment_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hipaasafe.R
import com.hipaasafe.base.BaseFragment
import com.hipaasafe.databinding.FragmentAppointmentBinding
import com.hipaasafe.presentation.home_screen.appointment_fragment.adapter.UpcomingAppointmentAdapter
import com.hipaasafe.presentation.home_screen.appointment_fragment.model.AppointmentItemType
import com.hipaasafe.presentation.home_screen.appointment_fragment.model.AppointmentStatus
import com.hipaasafe.presentation.home_screen.appointment_fragment.model.UpcomingAppointmentModel

class AppointmentFragment : BaseFragment(), UpcomingAppointmentAdapter.AppointmentClickManager {

    companion object {
        fun newInstance(): AppointmentFragment {
            return AppointmentFragment()
        }
    }

    lateinit var binding: FragmentAppointmentBinding
    lateinit var upcomingAppointmentAdapter: UpcomingAppointmentAdapter
    private var upcomingAppointmentList: ArrayList<UpcomingAppointmentModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
    }

    private fun setUpAdapter() {
        binding.apply {
            upcomingAppointmentAdapter = UpcomingAppointmentAdapter(requireContext(),upcomingAppointmentList,this@AppointmentFragment)
            recyclerUpcomingAppointment.adapter = upcomingAppointmentAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        callUpcomingAppointmentApi()
    }

    private fun callUpcomingAppointmentApi() {
        upcomingAppointmentList.clear()
        upcomingAppointmentList.add(
            UpcomingAppointmentModel(
                appointmentItemType = AppointmentItemType.ITEM_TITLE,
                name = getString(R.string.upcoming_appointments)
            )
        )
        upcomingAppointmentList.add(
            UpcomingAppointmentModel(
                appointmentItemType = AppointmentItemType.ITEM_APPOINTMENT,
                name = "Dr. Sanjeev Arora",
                speciality = "Cardiologist",
                date = "Tomorrow",
                time = "1:00 PM",
                appointmentStatus = AppointmentStatus.ITEM_RESCHEDULED
            )
        )
        upcomingAppointmentList.add(
            UpcomingAppointmentModel(
                appointmentItemType = AppointmentItemType.ITEM_DIVIDER
            )
        )
        upcomingAppointmentList.add(
            UpcomingAppointmentModel(
                appointmentItemType = AppointmentItemType.ITEM_APPOINTMENT,
                name = "Dr. Sanjeev Arora",
                speciality = "Cardiologist",
                date = "Tomorrow",
                time = "1:00 PM",
                appointmentStatus = AppointmentStatus.ITEM_PENDING
            )
        )
        upcomingAppointmentList.add(
            UpcomingAppointmentModel(
                appointmentItemType = AppointmentItemType.ITEM_DIVIDER
            )
        )
        upcomingAppointmentList.add(
            UpcomingAppointmentModel(
                appointmentItemType = AppointmentItemType.ITEM_APPOINTMENT,
                name = "Dr. Sanjeev Arora",
                speciality = "Cardiologist",
                date = "Tomorrow",
                time = "1:00 PM",
                appointmentStatus = AppointmentStatus.ITEM_CANCEL
            )
        )
        upcomingAppointmentList.add(
            UpcomingAppointmentModel(
                appointmentItemType = AppointmentItemType.ITEM_DIVIDER
            )
        )
        upcomingAppointmentList.add(
            UpcomingAppointmentModel(
                appointmentItemType = AppointmentItemType.ITEM_APPOINTMENT,
                name = "Dr. Sanjeev Arora",
                speciality = "Cardiologist",
                date = "Tomorrow",
                time = "1:00 PM",
                appointmentStatus = AppointmentStatus.ITEM_CONFIRM
            )
        )
        upcomingAppointmentList.add(
            UpcomingAppointmentModel(
                appointmentItemType = AppointmentItemType.ITEM_DIVIDER
            )
        )
        upcomingAppointmentList.add(
            UpcomingAppointmentModel(
                appointmentItemType = AppointmentItemType.ITEM_APPOINTMENT,
                name = "Dr. Sanjeev Arora",
                speciality = "Cardiologist",
                date = "Tomorrow",
                time = "1:00 PM",
                appointmentStatus = AppointmentStatus.ITEM_COMPLETED
            )
        )
    }

    override fun clickedOnCancelAppointment(position: Int) {

    }

    override fun clickedOnConfirmAppointment(position: Int) {

    }

    override fun clickedOnRescheduleAppointment(position: Int) {

    }
}