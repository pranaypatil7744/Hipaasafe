package com.hipaasafe.presentation.home_screen.appointment_fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
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
        setUpView()
        setUpAdapter()
        setUpListener()
    }

    private fun setUpListener() {
        binding.apply {
            layoutScanQr.root.setOnClickListener {
                layoutYourTurn.visibility = VISIBLE
                setUpWaitingUI(3)
            }
            layoutCount.setOnClickListener {
                setUpWaitingUI(0)
            }
            btnGotIt.setOnClickListener {
                layoutYourTurn.visibility = GONE
            }
        }
    }

    private fun setUpWaitingUI(queueNo: Int) {
        binding.apply {
            layoutYourTurn.apply {
                if (queueNo > 0) {
                    imgHeart.visibility = INVISIBLE
                    layoutCount.visibility = VISIBLE
                    btnGotIt.visibility = GONE
                    tvCount.text = queueNo.toString()
                    tvMainTitle.text = context.getString(R.string.we_re_still_waiting_in_the_queue)
                    tvMainSubTitle.text =
                        context.getString(R.string.saving_the_world_happens_one_person_at_a_time)
                } else {
                    imgHeart.visibility = VISIBLE
                    layoutCount.visibility = GONE
                    tvMainTitle.text = getString(R.string.now_it_s_your_turn)
                    tvMainSubTitle.text =
                        getString(R.string.a_doctor_sees_pain_death_suffering_on_a_daily_basis_but_they_provide_only_care_and_cure)
                    btnGotIt.visibility = VISIBLE
                }
            }
        }
    }

    private fun setUpView() {
        binding.apply {
            layoutScanQr.apply {
                btnAdd.setImageResource(R.drawable.ic_qr_code)
                tvTitle.text = getString(R.string.clinic_visit)
                tvSubTitle.text = getString(R.string.make_an_appointment)
            }
            layoutYourTurn.apply {
                tvMainTitle.text = getString(R.string.now_it_s_your_turn)
                tvMainSubTitle.text =
                    getString(R.string.a_doctor_sees_pain_death_suffering_on_a_daily_basis_but_they_provide_only_care_and_cure)
            }
        }
    }

    private fun setUpAdapter() {
        binding.apply {
            upcomingAppointmentAdapter = UpcomingAppointmentAdapter(
                requireContext(),
                upcomingAppointmentList,
                this@AppointmentFragment
            )
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