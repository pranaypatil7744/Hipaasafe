package com.hipaasafe.presentation.home_screen.home_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.base.BaseFragment
import com.hipaasafe.databinding.FragmentHomeBinding
import com.hipaasafe.presentation.adapter.PagerAdapter
import com.hipaasafe.presentation.home_screen.HomeActivity
import com.hipaasafe.presentation.home_screen.appointment_fragment.AppointmentFragment
import com.hipaasafe.presentation.home_screen.comet_chat_group.CometChatGroupFragment
import com.hipaasafe.presentation.home_screen.document_fragment.DocumentFragment
import com.hipaasafe.presentation.home_screen.my_network.MyNetworkFragment

class HomeFragment : BaseFragment() {
    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    lateinit var binding: FragmentHomeBinding
    val appointmentFragment = AppointmentFragment.newInstance()
    val chatFragment = CometChatGroupFragment.newInstance()
    val myNetworkFragment = MyNetworkFragment.newInstance()
    val documentFragment = DocumentFragment.newInstance()
    var isForChatScreen:Boolean = false
    var isForDocumentScreen:Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getIntentData()
        setUpView()
    }

    private fun getIntentData() {
        binding.apply {
            requireActivity().intent?.extras?.run {
                isForChatScreen = getBoolean(Constants.IS_CHAT_SCREEN,false)
                isForDocumentScreen = getBoolean(Constants.IS_DOCUMENT_SCREEN,false)
            }
        }
    }

    private fun setUpView() {
        binding.apply {
            val fList: ArrayList<Fragment> = ArrayList()
            fList.add(appointmentFragment)
            fList.add(chatFragment)
            fList.add(myNetworkFragment)
            fList.add(documentFragment)
            viewPager.adapter = PagerAdapter(requireActivity(), fList)
            viewPager.isUserInputEnabled = false
            navigationView.apply {
                when {
                    isForChatScreen -> {
                        selectedItemId = R.id.navigation_chat
                        viewPager.setCurrentItem(1,true)
                        (requireActivity() as HomeActivity).binding.toolbar.apply {
                            tvTitle.text = getString(R.string.chat)
                            tvDate.visibility = GONE
                            btnOne.visibility = GONE
                        }
                    }
                    isForDocumentScreen -> {
                        selectedItemId = R.id.navigation_documents
                        viewPager.setCurrentItem(3,true)
                        (requireActivity() as HomeActivity).binding.toolbar.apply {
                            tvTitle.text = getString(R.string.documents)
                            tvDate.visibility = GONE
                            btnOne.visibility = GONE
                        }
                    }
                    else -> {
                        viewPager.currentItem = 0
                        selectedItemId = R.id.navigation_appointments
                    }
                }
            }
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