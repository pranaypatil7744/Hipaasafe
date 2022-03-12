package com.hipaasafe.presentation.home_screen.home_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hipaasafe.R
import com.hipaasafe.base.BaseFragment
import com.hipaasafe.databinding.FragmentHomeDoctorBinding
import com.hipaasafe.presentation.adapter.PagerAdapter
import com.hipaasafe.presentation.home_screen.HomeActivity
import com.hipaasafe.presentation.home_screen.appointment_fragment_doctor.DoctorAppointmentFragment
import com.hipaasafe.presentation.home_screen.comet_chat_group.CometChatGroupFragment
import com.hipaasafe.presentation.home_screen.my_patients_fragment.MyPatientsFragment
import com.hipaasafe.presentation.home_screen.my_teams_fragment.MyTeamsFragment


class HomeFragmentDoctor : BaseFragment() {

    companion object {
        fun newInstance(): HomeFragmentDoctor {
            return HomeFragmentDoctor()
        }
    }

    val myPatientsFragment = MyPatientsFragment.newInstance()
    val doctorAppointmentFragment = DoctorAppointmentFragment.newInstance()
    val chatFragment = CometChatGroupFragment.newInstance()
    val myTeamsFragment = MyTeamsFragment.newInstance()
    lateinit var binding: FragmentHomeDoctorBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeDoctorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
    }

    private fun setUpView() {
        binding.apply {
            val fList: ArrayList<Fragment> = ArrayList()
            fList.add(doctorAppointmentFragment)
            fList.add(chatFragment)
            fList.add(myPatientsFragment)
            fList.add(myTeamsFragment)
            viewPager.adapter = PagerAdapter(requireActivity(), fList)
            viewPager.isUserInputEnabled = false
            navigationViewDoctor.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.navigation_doctor_appointments -> {
                        viewPager.currentItem = 0
                        (requireActivity() as HomeActivity).setUpToolbar()
                    }
                    R.id.navigation_chat -> {
                        viewPager.currentItem = 1
                        (requireActivity() as HomeActivity).binding.toolbar.apply {
                            tvTitle.text = getString(R.string.chat)
                            tvDate.visibility = View.GONE
                            btnOne.visibility = View.GONE
                        }
                    }
                    R.id.navigation_my_patients -> {
                        viewPager.currentItem = 2
                        (requireActivity() as HomeActivity).binding.toolbar.apply {
                            tvTitle.text = getString(R.string.my_patients)
                            tvDate.visibility = View.GONE
                            btnOne.visibility = View.GONE
                        }
                    }
                    R.id.navigation_my_team -> {
                        viewPager.currentItem = 3
                        (requireActivity() as HomeActivity).binding.toolbar.apply {
                            tvTitle.text = getString(R.string.my_teams)
                            tvDate.visibility = View.GONE
                            btnOne.visibility = View.GONE
                        }
                    }
                }
                true
            }
        }
    }
}