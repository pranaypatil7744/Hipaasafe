package com.hipaasafe.presentation.home_screen.my_network

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.models.Group
import com.hipaasafe.R
import com.hipaasafe.base.BaseFragment
import com.hipaasafe.databinding.FragmentMyNetworkBinding
import com.hipaasafe.domain.model.get_doctors.GetDoctorsRequestModel
import com.hipaasafe.presentation.home_screen.my_network.adapter.MyNetworkAdapter
import com.hipaasafe.presentation.home_screen.my_network.model.DoctorModel
import com.hipaasafe.utils.CometChatUtils.Companion.startGroupIntent
import com.hipaasafe.utils.PreferenceUtils
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
        setUpObserver()
        callMyNetworksApi()
        setUpAdapter()
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
                            showToast("no data")
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

    private fun callMyNetworksApi() {
        binding.apply {
            if (requireContext().isNetworkAvailable()) {
                toggleLoader(true)
                myNetworkViewModel.callPatientUpdateProfileApi(
                    GetDoctorsRequestModel(page = 1, limit = 30)
                )
            } else {
                showToast(getString(R.string.please_check_your_internet_connection))
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

    override fun clickOnChat(position: Int) {
        val group = Group()
        group.guid = myNetworkList[position].guid
        group.groupType = CometChatConstants.RECEIVER_TYPE_GROUP
        startGroupIntent(group, requireContext())
    }

}