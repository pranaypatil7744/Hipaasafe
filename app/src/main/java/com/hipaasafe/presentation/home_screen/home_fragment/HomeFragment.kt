package com.hipaasafe.presentation.home_screen.home_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import com.hipaasafe.R
import com.hipaasafe.base.BaseFragment
import com.hipaasafe.databinding.FragmentHomeBinding
import com.hipaasafe.presentation.adapter.PagerAdapter
import com.hipaasafe.presentation.home_screen.HomeActivity
import com.hipaasafe.presentation.home_screen.appointment_fragment.AppointmentFragment
import com.hipaasafe.presentation.home_screen.comet_chat_group.CometChatGroupFragment
import com.hipaasafe.presentation.home_screen.my_network.MyNetworkFragment

class HomeFragment : BaseFragment() {
    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    lateinit var binding: FragmentHomeBinding
    val myNetworkFragment = MyNetworkFragment.newInstance()
    val appointmentFragment = AppointmentFragment.newInstance()
    val chatFragment = CometChatGroupFragment.newInstance()
    val appointmentFragment3 = AppointmentFragment.newInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
    }

    private fun setUpView() {
        binding.apply {
            val fList: ArrayList<Fragment> = ArrayList()
            fList.add(appointmentFragment)
            fList.add(chatFragment)
            fList.add(myNetworkFragment)
            fList.add(appointmentFragment3)
            viewPager.adapter = PagerAdapter(requireActivity(), fList)
            viewPager.isUserInputEnabled = false
            navigationView.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.navigation_appointments -> {
                        viewPager.currentItem = 0
                        (requireActivity() as HomeActivity).setUpToolbar()
                    }
                    R.id.navigation_chat -> {
                        viewPager.currentItem = 1
                        (requireActivity() as HomeActivity).binding.toolbar.apply {
                            tvTitle.text = getString(R.string.chat)
                            tvDate.visibility = GONE
                            btnOne.visibility = GONE
                        }
                    }
                    R.id.navigation_my_network -> {
                        viewPager.currentItem = 2
                        (requireActivity() as HomeActivity).binding.toolbar.apply {
                            tvTitle.text = getString(R.string.my_network)
                            tvDate.visibility = GONE
                            btnOne.visibility = GONE
                        }
                    }
                    R.id.navigation_documents -> {
                        viewPager.currentItem = 3
                        (requireActivity() as HomeActivity).binding.toolbar.apply {
                            tvTitle.text = getString(R.string.documents)
                            tvDate.visibility = GONE
                            btnOne.visibility = GONE
                        }
                    }
                }
                true
            }
        }
    }

}