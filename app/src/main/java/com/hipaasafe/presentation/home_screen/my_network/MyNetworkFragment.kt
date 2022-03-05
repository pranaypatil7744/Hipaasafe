package com.hipaasafe.presentation.home_screen.my_network

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hipaasafe.base.BaseFragment
import com.hipaasafe.databinding.FragmentMyNetworkBinding
import com.hipaasafe.presentation.home_screen.my_network.adapter.MyNetworkAdapter
import com.hipaasafe.presentation.home_screen.my_network.model.DoctorModel

class MyNetworkFragment : BaseFragment() {

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
        setUpMyNetworkList()
        setUpAdapter()
    }

    private fun setUpAdapter() {
        binding.apply {
            myNetworkAdapter = MyNetworkAdapter(requireContext(), myNetworkList)
            recyclerMyNetwork.adapter = myNetworkAdapter
        }
    }

    private fun setUpMyNetworkList() {
        binding.apply {
            myNetworkList.clear()
            myNetworkList.add(
                DoctorModel(
                    name = "Sanjeev Arora",
                    location = "Mumbai",
                    speciality = "Cardiologist",
                    experience = "5"
                )
            )

            myNetworkList.add(
                DoctorModel(
                    name = "Sanjeev Arora",
                    location = "Mumbai",
                    speciality = "Cardiologist",
                    experience = "5"
                )
            )
            myNetworkList.add(
                DoctorModel(
                    name = "Sanjeev Arora",
                    location = "Mumbai",
                    speciality = "Cardiologist",
                    experience = "5"
                )
            )
            myNetworkList.add(
                DoctorModel(
                    name = "Sanjeev Arora",
                    location = "Mumbai",
                    speciality = "Cardiologist",
                    experience = "5"
                )
            )
        }
    }

}