package com.hipaasafe.presentation.home_screen.my_patients_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.models.Group
import com.hipaasafe.base.BaseFragment
import com.hipaasafe.databinding.FragmentMyPatientsBinding
import com.hipaasafe.domain.model.CommonRequestModel
import com.hipaasafe.domain.model.get_patients.PatientsListModel
import com.hipaasafe.presentation.home_screen.my_patients_fragment.adapter.MyPatientsAdapter
import com.hipaasafe.utils.CometChatUtils.Companion.startGroupIntent
import com.hipaasafe.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel


class MyPatientsFragment : BaseFragment(), MyPatientsAdapter.MyPatientsClickManager {

    lateinit var binding: FragmentMyPatientsBinding
    lateinit var patientsAdapter: MyPatientsAdapter

    companion object {
        fun newInstance(): MyPatientsFragment {
            return MyPatientsFragment()
        }
    }

    private var patientsList: ArrayList<PatientsListModel> = ArrayList()
    var pageNo: Int = 1
    private val patientsViewModel: PatientsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMyPatientsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        setUpObserver()
        callMyPatientsListApi()
        setUpListener()
    }

    private fun setUpListener() {
        binding.apply {
            layoutNoInternet.btnRetry.setOnClickListener {
                callMyPatientsListApi()
            }
        }
    }

    private fun setUpAdapter() {
        binding.apply {
            patientsAdapter =
                MyPatientsAdapter(requireContext(), patientsList, this@MyPatientsFragment)
            recyclerMyPatients.adapter = patientsAdapter
        }
    }

    private fun setUpObserver() {
        binding.apply {
            with(patientsViewModel) {
                patientsListResponseData.observe(requireActivity()) {
                    toggleLoader(false)
                    if (it.success == true) {
                        if (it.data.count != 0) {
                            layoutNoData.root.visibility = GONE
                            patientsList.clear()
                            patientsList.addAll(it.data.rows)
                            if (::patientsAdapter.isInitialized){
                                patientsAdapter.notifyDataSetChanged()
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

    private fun toggleLoader(showLoader: Boolean) {
        toggleFadeView(
            binding.root,
            binding.contentLoading.layoutLoading,
            binding.contentLoading.imageLoading,
            showLoader
        )
    }

    private fun callMyPatientsListApi() {
        binding.apply {
            if (requireContext().isNetworkAvailable()) {
                toggleLoader(true)
                layoutNoInternet.root.visibility = GONE
                recyclerMyPatients.visibility = VISIBLE
                patientsViewModel.callGetStaticDetailsApi(
                    request = CommonRequestModel(
                        page = pageNo,
                        limit = 10
                    )
                )
            } else {
                layoutNoInternet.root.visibility = VISIBLE
                recyclerMyPatients.visibility = GONE
            }
        }
    }

    override fun onClickChat(position: Int) {
        val group = Group()
        group.guid = patientsList[position].guid
        group.groupType = CometChatConstants.GROUP_TYPE_PRIVATE
        group.name = patientsList[position].name
        group.icon = patientsList[position].list_patient_details.avatar
//        joinGroup(group)
        startGroupIntent(group, requireContext())
    }

}