package com.hipaasafe.presentation.home_screen.my_network

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.core.CometChatUtils
import com.cometchat.pro.models.Group
import com.hipaasafe.Constants
import com.hipaasafe.base.BaseFragment
import com.hipaasafe.databinding.FragmentMyNetworkBinding
import com.hipaasafe.presentation.home_screen.my_network.adapter.MyNetworkAdapter
import com.hipaasafe.presentation.home_screen.my_network.model.DoctorModel
import com.hipaasafe.utils.CometChatUtils.Companion.startGroupIntent
import com.hipaasafe.utils.PreferenceUtils

class MyNetworkFragment : BaseFragment(), MyNetworkAdapter.MyNetworkClickManager {

    companion object {
        fun newInstance(): MyNetworkFragment {
            return MyNetworkFragment()
        }
    }

    lateinit var binding: FragmentMyNetworkBinding
    private val myNetworkList: ArrayList<DoctorModel> = ArrayList()
    lateinit var myNetworkAdapter: MyNetworkAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyNetworkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferenceUtils = PreferenceUtils(requireContext())
        setUpMyNetworkList()
        setUpAdapter()
    }

    private fun setUpAdapter() {
        binding.apply {
            myNetworkAdapter = MyNetworkAdapter(requireContext(), myNetworkList,this@MyNetworkFragment)
            recyclerMyNetwork.adapter = myNetworkAdapter
        }
    }

    private fun setUpMyNetworkList() {
        binding.apply {
            val guid = preferenceUtils.getValue(Constants.PreferenceKeys.uid)
            myNetworkList.clear()
            myNetworkList.add(
                DoctorModel(
                    name = "Sanjeev Arora",
                    location = "Mumbai",
                    speciality = "Cardiologist",
                    experience = "5",
                    guid = guid
                )
            )

            myNetworkList.add(
                DoctorModel(
                    name = "Sanjeev Arora",
                    location = "Mumbai",
                    speciality = "Cardiologist",
                    experience = "5",
                    guid = guid
                )
            )
            myNetworkList.add(
                DoctorModel(
                    name = "Sanjeev Arora",
                    location = "Mumbai",
                    speciality = "Cardiologist",
                    experience = "5",
                    guid = guid
                )
            )
            myNetworkList.add(
                DoctorModel(
                    name = "Sanjeev Arora",
                    location = "Mumbai",
                    speciality = "Cardiologist",
                    experience = "5",
                    guid = guid
                )
            )
        }
    }

    override fun clickOnChat(position: Int) {
        val group = Group()
        group.guid = myNetworkList[position].guid
        group.groupType = CometChatConstants.RECEIVER_TYPE_GROUP
        startGroupIntent(group,requireContext())
    }

}