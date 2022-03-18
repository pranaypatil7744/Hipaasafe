package com.hipaasafe.presentation.home_screen.home_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.base.BaseFragment
import com.hipaasafe.databinding.BottomsheetForwardDocBinding
import com.hipaasafe.databinding.FragmentHomeDoctorBinding
import com.hipaasafe.domain.model.doctor_login.DoctorsMappedModel
import com.hipaasafe.presentation.adapter.PagerAdapter
import com.hipaasafe.presentation.home_screen.HomeActivity
import com.hipaasafe.presentation.home_screen.appointment_fragment_doctor.DoctorAppointmentFragment
import com.hipaasafe.presentation.home_screen.comet_chat_group.CometChatGroupFragment
import com.hipaasafe.presentation.home_screen.document_fragment.adapter.ForwardDocAdapter
import com.hipaasafe.presentation.home_screen.document_fragment.model.ForwardDocumentModel
import com.hipaasafe.presentation.home_screen.my_patients_fragment.MyPatientsFragment
import com.hipaasafe.presentation.home_screen.my_teams_fragment.MyTeamsFragment
import com.hipaasafe.utils.ImageUtils
import com.hipaasafe.utils.PreferenceUtils
import com.hipaasafe.utils.enum.LoginUserType
import java.lang.reflect.Type


class HomeFragmentDoctor : BaseFragment(), ForwardDocAdapter.ForwardClickManager {

    companion object {
        fun newInstance(): HomeFragmentDoctor {
            return HomeFragmentDoctor()
        }
    }
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var bottomSheetSelectDoctorBinding: BottomsheetForwardDocBinding
    private lateinit var doctorListAdapter: ForwardDocAdapter

    val myPatientsFragment = MyPatientsFragment.newInstance()
    val doctorAppointmentFragment = DoctorAppointmentFragment.newInstance()
    val chatFragment = CometChatGroupFragment.newInstance()
    val myTeamsFragment = MyTeamsFragment.newInstance()
    var isForAttachDoc:Boolean = false
    var loginUserType:Int =0
    var selectedDoctorId:String =""
    var doctorsListForNurse:ArrayList<DoctorsMappedModel> = ArrayList()
    var doctorsList:ArrayList<ForwardDocumentModel> = ArrayList()
    var selectedBottomTabPosition:Int = 0

    //    val myTeamsFragment = MyNetworkFragment.newInstance()
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
        preferenceUtils = PreferenceUtils(requireContext())
        getPreferenceData()
        getIntentData()
        setUpView()
        setUpListener()
    }

    private fun setUpListener() {
        binding.apply {
            btnDown.setOnClickListener {
                if (doctorsListForNurse.size != 0){
                    openDoctorsListBottomSheet()
                }
            }
            tvDoctorName.setOnClickListener {
                openDoctorsListBottomSheet()
            }
        }
    }

    private fun getPreferenceData() {
        binding.apply {
            loginUserType = preferenceUtils.getValue(Constants.PreferenceKeys.role_id).toIntOrNull()?:0
            if (loginUserType == LoginUserType.NURSE.value){
                layoutSelectDoctor.visibility = VISIBLE
                val collectionType: Type = object : TypeToken<ArrayList<DoctorsMappedModel>>() {}.type
                val data = Gson().fromJson<ArrayList<DoctorsMappedModel>>(preferenceUtils.getValue(Constants.PreferenceKeys.doctorsMappedModel),collectionType)
                doctorsListForNurse.clear()
                doctorsListForNurse.addAll(data)
                setNurseUI()
            }else{
                selectedDoctorId = preferenceUtils.getValue(Constants.PreferenceKeys.uid)
                layoutSelectDoctor.visibility = GONE
            }
        }
    }

    private fun setNurseUI() {
        binding.apply {
            if (doctorsListForNurse.size != 0){
                selectedDoctorId = doctorsListForNurse[0].uid.toString()
                hintSelectDoctor.visibility = GONE
                imgProfile.visibility = VISIBLE
                tvDoctorName.visibility = VISIBLE
                ImageUtils.INSTANCE?.loadRemoteImageForProfile(imgProfile,doctorsListForNurse[0].avatar)
                tvDoctorName.text = doctorsListForNurse[0].name
                doctorsList.clear()
                for (i in doctorsListForNurse){
                    doctorsList.add(ForwardDocumentModel(
                        title = i.name,
                        icon = i.avatar,
                        doctorId = i.uid
                    ))
                }
                if (::doctorListAdapter.isInitialized){
                    doctorListAdapter.notifyDataSetChanged()
                }
            }else{
                hintSelectDoctor.visibility = VISIBLE
            }
        }
    }

    private fun getIntentData() {
        binding.apply {
            isForAttachDoc = (requireActivity() as HomeActivity).intent.getBooleanExtra(Constants.IsForAttachDoc,false)
        }
    }

    private fun openDoctorsListBottomSheet() {
        bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottomsheet_forward_doc, null)
        bottomSheetSelectDoctorBinding = BottomsheetForwardDocBinding.bind(view)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.setCancelable(true)
        bottomSheetSelectDoctorBinding.apply {
            btnShare.visibility = GONE
            imgClose.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            doctorListAdapter = ForwardDocAdapter(
                requireContext(),
                doctorsList,
                listener = this@HomeFragmentDoctor, isHideCheck = true
            )
            recyclerAttendanceHistory.adapter = doctorListAdapter
        }
        bottomSheetDialog.show()
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
                selectedBottomTabPosition = it.itemId
                when (it.itemId) {
                    R.id.navigation_doctor_appointments -> {
                        viewPager.currentItem = 0
                        (requireActivity() as HomeActivity).setUpToolbar()
                        if (loginUserType == LoginUserType.NURSE.value){
                            layoutSelectDoctor.visibility = VISIBLE
                        }else{
                            layoutSelectDoctor.visibility = GONE
                        }
                    }
                    R.id.navigation_chat -> {
                        viewPager.currentItem = 1
                        (requireActivity() as HomeActivity).binding.toolbar.apply {
                            tvTitle.text = getString(R.string.chat)
                            tvDate.visibility = GONE
                            btnOne.visibility = GONE
                            layoutSelectDoctor.visibility = GONE
                        }
                    }
                    R.id.navigation_my_patients -> {
                        viewPager.currentItem = 2
                        (requireActivity() as HomeActivity).binding.toolbar.apply {
                            tvTitle.text = getString(R.string.my_patients)
                            tvDate.visibility = GONE
                            btnOne.visibility = GONE
                            if (loginUserType == LoginUserType.NURSE.value){
                                layoutSelectDoctor.visibility = VISIBLE
                            }else{
                                layoutSelectDoctor.visibility = GONE
                            }
                        }
                    }
                    R.id.navigation_my_team -> {
                        viewPager.currentItem = 3
                        (requireActivity() as HomeActivity).binding.toolbar.apply {
                            tvTitle.text = getString(R.string.my_teams)
                            tvDate.visibility = GONE
                            btnOne.visibility = GONE
                            layoutSelectDoctor.visibility = GONE
                        }
                    }
                }
                true
            }
        }
    }

    override fun onItemClick(position: Int) {
        bottomSheetDialog.dismiss()
        selectedDoctorId = doctorsList[position].doctorId.toString()
        when(selectedBottomTabPosition){
            R.id.navigation_doctor_appointments -> {
                doctorAppointmentFragment.setUpTabListener(doctorAppointmentFragment.selectedTabPosition)
            }
            R.id.navigation_my_patients ->{
                myPatientsFragment.callMyPatientsListApi()
            }
        }
    }
}