package com.hipaasafe.presentation.profile_view_details

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View.*
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivityProfileViewDetailsBinding
import com.hipaasafe.presentation.help.HelpViewModel
import com.hipaasafe.presentation.help.model.HelpItemType
import com.hipaasafe.presentation.help.model.HelpModel
import com.hipaasafe.presentation.profile_edit_details.ProfileEditDetailsActivity
import com.hipaasafe.presentation.profile_view_details.adapter.ViewProfileAdapter
import com.hipaasafe.presentation.profile_view_details.model.ViewProfileModel
import com.hipaasafe.utils.AppUtils
import com.hipaasafe.utils.ImageUtils
import com.hipaasafe.utils.PreferenceUtils
import com.hipaasafe.utils.enum.LoginUserType
import com.hipaasafe.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileViewDetailsActivity : BaseActivity() {
    lateinit var binding: ActivityProfileViewDetailsBinding
    lateinit var viewProfileAdapter: ViewProfileAdapter
    private val viewProfileList: ArrayList<ViewProfileModel> = ArrayList()
    var name: String = ""
    var email: String = ""
    var mobile: String = ""
    var age: String = ""
    var profile: String = ""
    var experience: String = ""
    var speciality: String = ""
    var loginUserId: Int = 0
    var helpNo =""
    private val helpViewModel: HelpViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileViewDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferenceUtils = PreferenceUtils(this)
        setUpToolbar()
        setListener()
        setUpAdapter()
        if (loginUserId != LoginUserType.PATIENT.value){
            setUpObserver()
            callHelpDetailsApi()
        }
    }

    private fun setUpObserver() {
        binding.apply {
            with(helpViewModel) {
                helpDetailsResponseData.observe(this@ProfileViewDetailsActivity) {
                    toggleLoader(false)
                    if (it.success == true) {
                        helpNo = it.data[0].value.toString()
                        val text = getString(R.string.to_update_above_information_call_on)+" <b><u>"+ helpNo+"</u></b>"
                        tvInfo.text = Html.fromHtml(text)
                    } else {
                        showToast(it.message.toString())
                    }
                }
                messageData.observe(this@ProfileViewDetailsActivity) {
                    toggleLoader(false)
                    showToast(it.toString())
                }
            }
        }
    }

    private fun callHelpDetailsApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                helpViewModel.callGetStaticDetailsApi()
            } else {
                showToast(getString(R.string.please_check_your_internet_connection))
            }
        }
    }

    private fun setUpAdapter() {
        binding.apply {
            viewProfileAdapter =
                ViewProfileAdapter(this@ProfileViewDetailsActivity, viewProfileList)
            recyclerViewProfile.adapter = viewProfileAdapter
        }
    }


    override fun onResume() {
        super.onResume()
        getPreferenceData()
    }

    private fun getPreferenceData() {
        binding.apply {
            name = preferenceUtils.getValue(Constants.PreferenceKeys.name)
            email = preferenceUtils.getValue(Constants.PreferenceKeys.email)
            mobile = preferenceUtils.getValue(Constants.PreferenceKeys.number)
            age = preferenceUtils.getValue(Constants.PreferenceKeys.age)
            experience = preferenceUtils.getValue(Constants.PreferenceKeys.experience)
            speciality = preferenceUtils.getValue(Constants.PreferenceKeys.speciality)
            profile = preferenceUtils.getValue(Constants.PreferenceKeys.avatar)
            loginUserId =
                preferenceUtils.getValue(Constants.PreferenceKeys.role_id).toIntOrNull() ?: 0
            setUpViewProfileList()
        }
    }

    private fun setUpViewProfileList() {
        binding.apply {
            ImageUtils.INSTANCE?.loadRemoteImageForProfile(imgProfile, profile)
            viewProfileList.clear()
            viewProfileList.add(
                ViewProfileModel(
                    icon = R.drawable.ic_profile,
                    title = getString(R.string.name), subTitle = name
                )
            )
            viewProfileList.add(
                ViewProfileModel(
                    icon = R.drawable.ic_mail,
                    title = getString(R.string.email), subTitle = email
                )
            )
            if (loginUserId == LoginUserType.PATIENT.value) {
                viewProfileList.add(
                    ViewProfileModel(
                        icon = R.drawable.ic_call,
                        title = getString(R.string.mobile_number), subTitle = mobile
                    )
                )
                viewProfileList.add(
                    ViewProfileModel(
                        icon = R.drawable.ic_calendar,
                        title = getString(R.string.age), subTitle = age
                    )
                )
                btnEditDetails.visibility = VISIBLE
                layoutInfo.visibility = GONE
            } else {
                viewProfileList.add(
                    ViewProfileModel(
                        icon = R.drawable.ic_speciality,
                        title = getString(R.string.specialty), subTitle = speciality
                    )
                )
                viewProfileList.add(
                    ViewProfileModel(
                        icon = R.drawable.ic_calendar,
                        title = getString(R.string.years_of_experience), subTitle = experience
                    )
                )
                btnEditDetails.visibility = INVISIBLE
                layoutInfo.visibility = VISIBLE
            }

            if (::viewProfileAdapter.isInitialized) {
                viewProfileAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun setListener() {
        binding.apply {
            btnEditDetails.setOnClickListener {
                val i =
                    Intent(this@ProfileViewDetailsActivity, ProfileEditDetailsActivity::class.java)
                startActivity(i)
            }

            tvInfo.setOnClickListener {
                AppUtils.INSTANCE?.openDialer(this@ProfileViewDetailsActivity, helpNo)
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