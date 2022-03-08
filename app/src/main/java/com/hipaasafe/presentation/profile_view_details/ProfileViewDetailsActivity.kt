package com.hipaasafe.presentation.profile_view_details

import android.content.Intent
import android.os.Bundle
import android.view.View.*
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivityProfileViewDetailsBinding
import com.hipaasafe.presentation.profile_edit_details.ProfileEditDetailsActivity
import com.hipaasafe.presentation.profile_view_details.adapter.ViewProfileAdapter
import com.hipaasafe.presentation.profile_view_details.model.ViewProfileModel
import com.hipaasafe.utils.ImageUtils
import com.hipaasafe.utils.PreferenceUtils
import com.hipaasafe.utils.enum.LoginUserType

class ProfileViewDetailsActivity : BaseActivity() {
    lateinit var binding: ActivityProfileViewDetailsBinding
    lateinit var viewProfileAdapter: ViewProfileAdapter
    private val viewProfileList: ArrayList<ViewProfileModel> = ArrayList()
    var name:String = ""
    var email:String = ""
    var mobile:String = ""
    var age:String = ""
    var profile:String = ""
    var loginUserId:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileViewDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferenceUtils = PreferenceUtils(this)
        setUpToolbar()
        setListener()
        setUpAdapter()
    }

    private fun setUpAdapter() {
        binding.apply {
            viewProfileAdapter = ViewProfileAdapter(this@ProfileViewDetailsActivity,viewProfileList)
            recyclerViewProfile.adapter = viewProfileAdapter
        }
    }


    override fun onResume() {
        super.onResume()
        getPreferenceData()
        setUpViewProfileList()
    }

    private fun getPreferenceData() {
        binding.apply {
            name = preferenceUtils.getValue(Constants.PreferenceKeys.name)
            email = preferenceUtils.getValue(Constants.PreferenceKeys.email)
            mobile = preferenceUtils.getValue(Constants.PreferenceKeys.number)
            age = preferenceUtils.getValue(Constants.PreferenceKeys.age)
            profile = preferenceUtils.getValue(Constants.PreferenceKeys.avatar)
            loginUserId = preferenceUtils.getValue(Constants.PreferenceKeys.role_id).toIntOrNull()?:0
        }
    }

    private fun setUpViewProfileList() {
        binding.apply {
            ImageUtils.INSTANCE?.loadRemoteImageForProfile(imgProfile,profile)
            viewProfileList.clear()
            viewProfileList.add(
                ViewProfileModel(
                    icon = R.drawable.ic_profile,
                    title = "Name", subTitle = name
                )
            )
            viewProfileList.add(
                ViewProfileModel(
                    icon = R.drawable.ic_mail,
                    title = "Email", subTitle = email
                )
            )
            viewProfileList.add(
                ViewProfileModel(
                    icon = R.drawable.ic_call,
                    title = "Mobile Number", subTitle = mobile
                )
            )
            viewProfileList.add(
                ViewProfileModel(
                    icon = R.drawable.ic_calendar,
                    title = "Age", subTitle = age
                )
            )
            if (::viewProfileAdapter.isInitialized){
                viewProfileAdapter.notifyDataSetChanged()
            }
            if (loginUserId == LoginUserType.PATIENT.value){
                btnEditDetails.visibility = VISIBLE
            }else{
                btnEditDetails.visibility = INVISIBLE
            }
        }
    }

    private fun setListener() {
        binding.apply {
            btnEditDetails.setOnClickListener {
                val i = Intent(this@ProfileViewDetailsActivity,ProfileEditDetailsActivity::class.java)
                startActivity(i)
            }
        }
    }


    private fun setUpToolbar() {
        binding.toolbarHome.apply {
            tvTitle.text = getString(R.string.view_details)
            tvDate.visibility = GONE
            btnBack.setOnClickListener {
                finish()
            }
            btnBack.visibility = VISIBLE
            divider.visibility = VISIBLE
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

}