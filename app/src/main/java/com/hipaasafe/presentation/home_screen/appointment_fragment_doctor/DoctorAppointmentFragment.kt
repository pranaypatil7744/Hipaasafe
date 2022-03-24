package com.hipaasafe.presentation.home_screen.appointment_fragment_doctor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hipaasafe.base.BaseFragment
import com.hipaasafe.databinding.FragmentDoctorAppointmentBinding
import com.hipaasafe.domain.model.appointment.DoctorAppointmentDashboardRequestModel
import com.hipaasafe.presentation.adapter.PagerAdapter
import com.hipaasafe.presentation.home_screen.HomeActivity
import com.hipaasafe.presentation.home_screen.appointment_fragment.AppointmentViewModel
import com.hipaasafe.presentation.home_screen.appointment_fragment_doctor.model.AppointmentTabModel
import com.hipaasafe.utils.AppUtils
import com.hipaasafe.utils.PreferenceUtils
import com.hipaasafe.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel


class DoctorAppointmentFragment : BaseFragment() {

    companion object {
        fun newInstance(): DoctorAppointmentFragment {
            return DoctorAppointmentFragment()
        }
    }

    private val appointmentViewModel: AppointmentViewModel by viewModel()

    var doctorAppointmentsListFragment1 = DoctorAppointmentsListFragment.newInstance()
    var doctorAppointmentsListFragment2 = DoctorAppointmentsListFragment.newInstance()
    var doctorAppointmentsListFragment3 = DoctorAppointmentsListFragment.newInstance()
    var doctorAppointmentsListFragment4 = DoctorAppointmentsListFragment.newInstance()
    var doctorAppointmentsListFragment5 = DoctorAppointmentsListFragment.newInstance()
    var doctorAppointmentsListFragment6 = DoctorAppointmentsListFragment.newInstance()
    var doctorAppointmentsListFragment7 = DoctorAppointmentsListFragment.newInstance()
    var nextSevenDaysList: ArrayList<AppointmentTabModel> = ArrayList()
    lateinit var binding: FragmentDoctorAppointmentBinding
    var selectedTabPosition: Int = 0
    var selectedDoctorId: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDoctorAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferenceUtils = PreferenceUtils(requireContext())
        val homeFragmentDoctor = (requireActivity() as HomeActivity).homeFragmentDoctor
        selectedDoctorId = homeFragmentDoctor.selectedDoctorId
        setUpObserver()
        callDashboardCountsApi()
        setUpListener()
    }

    private fun setUpObserver() {
        binding.apply {
            with(appointmentViewModel) {
                doctorAppointmentDashboardCountResponseData.observe(requireActivity()) {
                    toggleLoader(false)
                    if (it.success == true) {
                        if (!it.data.isNullOrEmpty()) {
                            layoutNoAppointments.root.visibility = GONE
                            tabLayout.visibility = VISIBLE
                            divider.visibility = VISIBLE
                            viewPager.visibility = VISIBLE
                            nextSevenDaysList.clear()
                            for (i in it.data) {
                                nextSevenDaysList.add(
                                    AppointmentTabModel(
                                        date = i.date?.split("T")?.first(),
                                        label = AppUtils.INSTANCE?.convertDateFormat(
                                            "yyyy-MM-dd",
                                            i.date.toString().split("T").first(),
                                            "dd MMM"
                                        )+" (${i.count})"
                                    )
                                )
                            }
                            if (nextSevenDaysList.isNotEmpty() && nextSevenDaysList.size > 1){
                                nextSevenDaysList[0].label = "Today (${it.data[0].count})"
                                nextSevenDaysList[1].label = "Tomorrow (${it.data[1].count})"
                            }
                            setUpTabs()
                            setUpTabListener(selectedTabPosition)
                        } else {
                            layoutNoAppointments.root.visibility = VISIBLE
                            layoutNoAppointments.tvInfo.text = "No appointments"
                            tabLayout.visibility = GONE
                            divider.visibility = GONE
                            viewPager.visibility = GONE
                        }
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
        toggleFadeView(
            binding.root,
            binding.contentLoading.layoutLoading,
            binding.contentLoading.imageLoading,
            showLoader
        )
    }


    fun callDashboardCountsApi() {
        binding.apply {
            if (requireActivity().isNetworkAvailable()) {
                toggleLoader(true)
                layoutNoInternet.root.visibility = GONE
                tabLayout.visibility = VISIBLE
                divider.visibility = VISIBLE
                viewPager.visibility = VISIBLE
                appointmentViewModel.callDoctorDashboardCountApi(
                    request = DoctorAppointmentDashboardRequestModel(
                        doctor_id = selectedDoctorId
                    )
                )
            } else {
                layoutNoInternet.root.visibility = VISIBLE
                tabLayout.visibility = GONE
                divider.visibility = GONE
                viewPager.visibility = GONE
            }
        }
    }

    private fun setUpListener() {
        binding.apply {
            layoutNoInternet.btnRetry.setOnClickListener {
                callDashboardCountsApi()
            }
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    selectedTabPosition = tab?.position ?: 0
                    setUpTabListener(selectedTabPosition)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

            })
        }
    }

    fun setUpTabListener(tabPosition: Int) {
        when (tabPosition) {
            0 -> {
                doctorAppointmentsListFragment1.apply {
                    callDoctorAppointmentListApi(
                        date = nextSevenDaysList[0].date.toString(),
                        selectedDoctorId
                    )
                }
            }
            1 -> {
                doctorAppointmentsListFragment2.apply {
                    callDoctorAppointmentListApi(
                        date = nextSevenDaysList[1].date.toString(),
                        selectedDoctorId
                    )
                }
            }
            2 -> {
                doctorAppointmentsListFragment3.apply {
                    callDoctorAppointmentListApi(
                        date = nextSevenDaysList[2].date.toString(),
                        selectedDoctorId
                    )
                }
            }
            3 -> {
                doctorAppointmentsListFragment4.apply {
                    callDoctorAppointmentListApi(
                        date = nextSevenDaysList[3].date.toString(),
                        selectedDoctorId
                    )
                }
            }
            4 -> {
                doctorAppointmentsListFragment5.apply {
                    callDoctorAppointmentListApi(
                        date = nextSevenDaysList[4].date.toString(),
                        selectedDoctorId
                    )
                }
            }
            5 -> {
                doctorAppointmentsListFragment6.apply {
                    callDoctorAppointmentListApi(
                        date = nextSevenDaysList[5].date.toString(),
                        selectedDoctorId
                    )
                }
            }
            6 -> {
                doctorAppointmentsListFragment7.apply {
                    callDoctorAppointmentListApi(
                        date = nextSevenDaysList[6].date.toString(),
                        selectedDoctorId
                    )
                }
            }
        }
    }

    private fun setUpView() {
        binding.apply {
            nextSevenDaysList = AppUtils.INSTANCE?.getNextSevenDays() ?: ArrayList()
        }
    }

    private fun setUpTabs() {
        binding.apply {
            val fragmentList: ArrayList<Fragment> = ArrayList()
            fragmentList.clear()
            fragmentList.add(doctorAppointmentsListFragment1)
            fragmentList.add(doctorAppointmentsListFragment2)
            fragmentList.add(doctorAppointmentsListFragment3)
            fragmentList.add(doctorAppointmentsListFragment4)
            fragmentList.add(doctorAppointmentsListFragment5)
            fragmentList.add(doctorAppointmentsListFragment6)
            fragmentList.add(doctorAppointmentsListFragment7)
            val adapter =
                PagerAdapter(this@DoctorAppointmentFragment.requireActivity(), fragmentList)
            viewPager.adapter = adapter
            viewPager.isUserInputEnabled = true
            viewPager.offscreenPageLimit = 7

            val tabsList: ArrayList<String> = ArrayList()
            tabsList.clear()
            for (i in nextSevenDaysList) {
                tabsList.add(i.label.toString())
            }
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = tabsList[position]
            }.attach()
        }
    }

}