package com.hipaasafe.presentation.home_screen.my_network

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.Group
import com.cometchat.pro.models.User
import com.hipaasafe.base.BaseFragment
import com.hipaasafe.databinding.FragmentMyNetworkBinding
import com.hipaasafe.domain.model.get_doctors.DoctorMyTeamsRequestModel
import com.hipaasafe.domain.model.get_doctors.GetDoctorsRequestModel
import com.hipaasafe.presentation.home_screen.my_network.adapter.MyNetworkAdapter
import com.hipaasafe.presentation.home_screen.my_network.model.DoctorModel
import com.hipaasafe.utils.CometChatUtils.Companion.startGroupIntent
import com.hipaasafe.utils.CometChatUtils.Companion.userIntent
import com.hipaasafe.utils.PreferenceUtils
import com.hipaasafe.utils.enum.LoginUserType
import com.hipaasafe.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class MyNetworkFragment : BaseFragment(), MyNetworkAdapter.MyNetworkClickManager {

    companion object {
        fun newInstance(): MyNetworkFragment {
            return MyNetworkFragment()
        }
    }

    private val myNetworkViewModel: MyNetworkViewModel by viewModel()
    lateinit var binding: FragmentMyNetworkBinding
    var loginUserType:Int =0
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
        setUpAdapter()
        setUpObserver()
        callDoctorsListApi()
        setUpListener()
    }

    private fun callDoctorsListApi() {
        binding.apply {
            if (requireContext().isNetworkAvailable()) {
//                if (loginUserType == LoginUserType.PATIENT.value){
//                    myNetworkViewModel.callMyNetworkDoctorsListApi(
//                        GetDoctorsRequestModel(page = 1, limit = 30)
//                    )
//                }else if (loginUserType == LoginUserType.DOCTOR.value || loginUserType == LoginUserType.NURSE.value){
//                    myNetworkViewModel.callDoctorMyTeamsListApi(
//                        DoctorMyTeamsRequestModel(page = 1, limit = 30)
//                    )
//                }

                toggleLoader(true)
                myNetworkViewModel.callMyNetworkDoctorsListApi(
                    GetDoctorsRequestModel(page = 1, limit = 30)
                )
                layoutNoInternet.root.visibility = GONE
                recyclerMyNetwork.visibility = VISIBLE
            } else {
                layoutNoInternet.root.visibility = VISIBLE
                recyclerMyNetwork.visibility = GONE
            }
        }
    }

    private fun setUpListener() {
        binding.apply {
            layoutNoInternet.btnRetry.setOnClickListener {
                callDoctorsListApi()
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

    private fun setUpObserver() {
        binding.apply {
            with(myNetworkViewModel) {
                myNetworkListResponseData.observe(this@MyNetworkFragment.viewLifecycleOwner) {
                    toggleLoader(false)
                    if (it.success == true) {
                        if (it.data.count != 0) {
                            layoutNoData.root.visibility = GONE
                            myNetworkList.clear()
                            for (i in it.data.rows ?: arrayListOf()) {
                                myNetworkList.add(
                                    DoctorModel(
                                        name = i.list_doctor_details.name,
                                        location = i.list_doctor_details.doctor_details.location,
                                        speciality = i.list_doctor_details.doctor_details.speciality.title,
                                        experience = i.list_doctor_details.doctor_details.experience,
                                        guid = i.guid,
                                        avatar = i.list_doctor_details.avatar
                                    )
                                )
                            }
                            if (::myNetworkAdapter.isInitialized) {
                                myNetworkAdapter.notifyDataSetChanged()
                            }
                        } else {
                            layoutNoData.root.visibility = VISIBLE
                        }
                    } else {
                        showToast(it.message.toString())
                    }
                }
                doctorMyTeamsListResponseData.observe(requireActivity()){
                    toggleLoader(false)
                    if (it.success == true) {
                        if (it.data.count != 0) {
                            layoutNoData.root.visibility = GONE
                            myNetworkList.clear()
                            for (i in it.data.rows ?: arrayListOf()) {
                                myNetworkList.add(
                                    DoctorModel(
                                        name = i.name,
                                        location = i.doctor_details?.location,
                                        speciality = i.doctor_details?.speciality?.title,
                                        experience = i.doctor_details?.experience,
                                        guid = i.uid,
                                        avatar = i.avatar
                                    )
                                )
                            }
                            if (::myNetworkAdapter.isInitialized) {
                                myNetworkAdapter.notifyDataSetChanged()
                            }
                        } else {
                            layoutNoData.root.visibility = VISIBLE
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

    private fun setUpAdapter() {
        binding.apply {
            myNetworkAdapter =
                MyNetworkAdapter(requireContext(), myNetworkList, this@MyNetworkFragment)
            recyclerMyNetwork.adapter = myNetworkAdapter
        }
    }

    private fun joinGroup(group: Group?) {
        CometChat.joinGroup(
            group!!.guid,
            group.groupType,
            "",
            object : CometChat.CallbackListener<Group?>() {
                override fun onSuccess(g: Group?) {
                    if (g != null) {
                        toggleLoader(false)
                        startGroupIntent(g, requireContext())
                    }
                }

                override fun onError(e: CometChatException) {
                    toggleLoader(false)
                    if (e.code == "ERR_ALREADY_JOINED"){
                        startGroupIntent(group, requireContext())
                    }else{
                        showToast(e.message.toString())
                    }
                }
            })
    }

    override fun clickOnChat(position: Int) {
        if (loginUserType == LoginUserType.PATIENT.value){
            toggleLoader(true)
            val group = Group()
            group.guid = myNetworkList[position].guid
            group.groupType = CometChatConstants.GROUP_TYPE_PRIVATE
            group.name = myNetworkList[position].name
            group.icon = myNetworkList[position].avatar
            joinGroup(group)
//        startGroupIntent(group, requireContext())
        }else{
            val user= User()
            user.name = myNetworkList[position].name
            user.uid = myNetworkList[position].guid
            user.avatar = myNetworkList[position].avatar
            userIntent(user,requireContext())
        }
    }

}