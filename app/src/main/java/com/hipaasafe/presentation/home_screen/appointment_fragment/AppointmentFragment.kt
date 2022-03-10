package com.hipaasafe.presentation.home_screen.appointment_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.base.BaseFragment
import com.hipaasafe.databinding.FragmentAppointmentBinding
import com.hipaasafe.domain.model.appointment.GetAppointmentsRequestModel
import com.hipaasafe.domain.model.appointment.ModifyAppointmentRequestModel
import com.hipaasafe.presentation.home_screen.appointment_fragment.adapter.UpcomingAppointmentAdapter
import com.hipaasafe.presentation.home_screen.appointment_fragment.model.AppointmentItemType
import com.hipaasafe.presentation.home_screen.appointment_fragment.model.AppointmentStatus
import com.hipaasafe.presentation.home_screen.appointment_fragment.model.UpcomingAppointmentModel
import com.hipaasafe.utils.AppUtils
import com.hipaasafe.utils.DialogUtils
import com.hipaasafe.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class AppointmentFragment : BaseFragment(), UpcomingAppointmentAdapter.AppointmentClickManager,
    DialogUtils.DialogManager {

    companion object {
        fun newInstance(): AppointmentFragment {
            return AppointmentFragment()
        }
    }

    private val appointmentViewModel: AppointmentViewModel by viewModel()
    var pageNo: Int = 1
    var isLoading: Boolean = true
    lateinit var binding: FragmentAppointmentBinding
    lateinit var upcomingAppointmentAdapter: UpcomingAppointmentAdapter
    private var upcomingAppointmentList: ArrayList<UpcomingAppointmentModel> = ArrayList()
    var selectedItemPosition: Int = 0
    var selectedType: String = ""

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
        setUpObserver()
        setUpAdapter()
        setUpListener()
        callUpcomingAppointmentApi()
    }

    private fun setUpObserver() {
        binding.apply {
            with(appointmentViewModel) {
                getAppointmentsResponseData.observe(requireActivity()) {
                    toggleLoader(false)
                    if (it.success) {
                        if (it.data != null && it.data.count != 0) {
                            upcomingAppointmentList.clear()
                            upcomingAppointmentList.add(
                                UpcomingAppointmentModel(
                                    appointmentItemType = AppointmentItemType.ITEM_TITLE,
                                    name = getString(R.string.upcoming_appointments)
                                )
                            )
                            for (i in it.data.rows) {
                                upcomingAppointmentList.add(
                                    UpcomingAppointmentModel(
                                        appointmentItemType = AppointmentItemType.ITEM_APPOINTMENT,
                                        name = i.appointment_doctor_details.name,
                                        speciality = i.appointment_doctor_details.doctor_details.speciality.title,
                                        date = i.appointment_date,
                                        time = i.appointment_time,
                                        appointment_id = i.appointment_id.toString(),
                                        appointmentStatus = AppUtils.INSTANCE?.getBookingStatus(i.appointment_status.toString())
                                    )
                                )
                                upcomingAppointmentList.add(
                                    UpcomingAppointmentModel(
                                        appointmentItemType = AppointmentItemType.ITEM_DIVIDER
                                    )
                                )
                            }
                            upcomingAppointmentAdapter.notifyDataSetChanged()
                        } else {
                            showToast("no data ")
                        }
                    } else {
                        showToast(it.message.toString())
                    }
                }

                modifyAppointmentResponseData.observe(requireActivity()) {
                    toggleLoader(false)
                    if (it.success == true) {
                        when (selectedType) {
                            Constants.CANCEL -> {
                                upcomingAppointmentList[selectedItemPosition].appointmentStatus =
                                    AppointmentStatus.ITEM_CANCEL
                            }
                            Constants.CONFIRM -> {
                                upcomingAppointmentList[selectedItemPosition].appointmentStatus =
                                    AppointmentStatus.ITEM_CONFIRM
                            }
                            Constants.RESCHEDULE -> {
                                upcomingAppointmentList[selectedItemPosition].appointmentStatus =
                                    AppointmentStatus.ITEM_RESCHEDULED
                            }
                        }
                        upcomingAppointmentAdapter.notifyItemChanged(selectedItemPosition)

                    } else {
                        showToast(it.message.toString())
                    }
                }

                messageData.observe(requireActivity()) {
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
        request.type = Constants.UPCOMING
        return request
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

    private fun callUpcomingAppointmentApi() {
        binding.apply {
            if (requireContext().isNetworkAvailable()) {
                toggleLoader(true)
                appointmentViewModel.callGetAppointmentsListApi(request = getAppointmentsListRequestModel())
            } else {
                showToast(getString(R.string.please_check_your_internet_connection))
            }
        }
    }

    override fun clickedOnCancelAppointment(position: Int) {
        selectedItemPosition = position
        DialogUtils.showCancelConfirmationDialog(
            requireActivity(),
            this,
            title = getString(R.string.are_you_sure_you_want_to_cancel_the_appointment),
            btnText = getString(R.string.yes_cancel),
            true
        )
    }

    override fun onContinueClick() {
        selectedType = Constants.RESCHEDULE
        callModifyAppointmentApi(Constants.RESCHEDULE)
    }

    override fun clickedOnConfirmAppointment(position: Int) {
        selectedItemPosition = position
        selectedType = Constants.CONFIRM
        callModifyAppointmentApi(Constants.CONFIRM)
    }

    override fun clickedOnRescheduleAppointment(position: Int) {
        selectedItemPosition = position
        DialogUtils.showCancelConfirmationDialog(
            requireActivity(),
            this,
            title = getString(R.string.are_you_sure_you_want_to_reschedule),
            btnText = getString(R.string.yes_request_reschedule),
            false
        )
    }

    override fun onCancelClick() {
        selectedType = Constants.CANCEL
        callModifyAppointmentApi(Constants.CANCEL)
    }

    private fun callModifyAppointmentApi(type: String) {
        binding.apply {
            if (requireContext().isNetworkAvailable()) {
                toggleLoader(true)
                appointmentViewModel.callGetModifyAppointmentApi(
                    request = ModifyAppointmentRequestModel(
                        appointment_id = upcomingAppointmentList[selectedItemPosition].appointment_id,
                        type
                    )
                )
            } else {
                showToast(getString(R.string.please_check_your_internet_connection))
            }
        }
    }
}