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
import com.hipaasafe.Constants
import com.hipaasafe.base.BaseFragment
import com.hipaasafe.databinding.FragmentDoctorAppointmentBinding
import com.hipaasafe.presentation.adapter.PagerAdapter
import com.hipaasafe.presentation.home_screen.appointment_fragment_doctor.model.AppointmentTabModel
import com.hipaasafe.utils.AppUtils
import com.hipaasafe.utils.PreferenceUtils
import com.hipaasafe.utils.enum.LoginUserType


class DoctorAppointmentFragment : BaseFragment() {

    companion object {
        fun newInstance(): DoctorAppointmentFragment {
            return DoctorAppointmentFragment()
        }
    }

    var doctorAppointmentsListFragment1 = DoctorAppointmentsListFragment.newInstance()
    var doctorAppointmentsListFragment2 = DoctorAppointmentsListFragment.newInstance()
    var doctorAppointmentsListFragment3 = DoctorAppointmentsListFragment.newInstance()
    var doctorAppointmentsListFragment4 = DoctorAppointmentsListFragment.newInstance()
    var doctorAppointmentsListFragment5 = DoctorAppointmentsListFragment.newInstance()
    var doctorAppointmentsListFragment6 = DoctorAppointmentsListFragment.newInstance()
    var doctorAppointmentsListFragment7 = DoctorAppointmentsListFragment.newInstance()
    var nextSevenDaysList: ArrayList<AppointmentTabModel> = ArrayList()
    lateinit var binding: FragmentDoctorAppointmentBinding
    var loginDoctorId: String = ""
    var loginUserType:Int =0
    var selectedTabPosition: Int = 0
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
        getPreferenceData()
        setUpView()
        setUpTabs()
        setUpListener()
    }

    private fun getPreferenceData() {
        binding.apply {
            loginDoctorId = preferenceUtils.getValue(Constants.PreferenceKeys.uid)
            loginUserType = preferenceUtils.getValue(Constants.PreferenceKeys.role_id).toIntOrNull()?:0
        }
    }

    private fun setUpListener() {
        binding.apply {
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    selectedTabPosition = tab?.position ?: 0
                    when (tab?.position) {
                        0 -> {
                            doctorAppointmentsListFragment1.apply {
                                callDoctorAppointmentListApi(
                                    date = nextSevenDaysList[0].date.toString(),
                                    loginDoctorId
                                )
                            }
                        }
                        1 -> {
                            doctorAppointmentsListFragment2.apply {
                                callDoctorAppointmentListApi(
                                    date = nextSevenDaysList[1].date.toString(),
                                    loginDoctorId
                                )
                            }
                        }
                        2 -> {
                            doctorAppointmentsListFragment3.apply {
                                callDoctorAppointmentListApi(
                                    date = nextSevenDaysList[2].date.toString(),
                                    loginDoctorId
                                )
                            }
                        }
                        3 -> {
                            doctorAppointmentsListFragment4.apply {
                                callDoctorAppointmentListApi(
                                    date = nextSevenDaysList[3].date.toString(),
                                    loginDoctorId
                                )
                            }
                        }
                        4 -> {
                            doctorAppointmentsListFragment5.apply {
                                callDoctorAppointmentListApi(
                                    date = nextSevenDaysList[4].date.toString(),
                                    loginDoctorId
                                )
                            }
                        }
                        5 -> {
                            doctorAppointmentsListFragment6.apply {
                                callDoctorAppointmentListApi(
                                    date = nextSevenDaysList[5].date.toString(),
                                    loginDoctorId
                                )
                            }
                        }
                        6 -> {
                            doctorAppointmentsListFragment7.apply {
                                callDoctorAppointmentListApi(
                                    date = nextSevenDaysList[6].date.toString(),
                                    loginDoctorId
                                )
                            }
                        }
                    }

                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

            })
        }
    }

    private fun setUpView() {
        binding.apply {
            nextSevenDaysList = AppUtils.INSTANCE?.getNextSevenDays() ?: ArrayList()
            if (loginUserType == LoginUserType.NURSE.value){
                layoutSelectDoctor.visibility = VISIBLE
            }else{
                layoutSelectDoctor.visibility = GONE
            }
        }
    }

    private fun setUpTabs() {
        binding.apply {
            val fragmentList: ArrayList<Fragment> = ArrayList()
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