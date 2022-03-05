package com.hipaasafe.presentation.profile_view_details

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import com.hipaasafe.R
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivityProfileViewDetailsBinding
import com.hipaasafe.presentation.profile_view_details.adapter.ViewProfileAdapter
import com.hipaasafe.presentation.profile_view_details.model.ViewProfileModel

class ProfileViewDetailsActivity : BaseActivity() {
    lateinit var binding: ActivityProfileViewDetailsBinding
    lateinit var viewProfileAdapter: ViewProfileAdapter
    private val viewProfileList: ArrayList<ViewProfileModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileViewDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        setUpViewProfileList()
    }

    private fun setUpViewProfileList() {
        binding.imgProfile.setImageResource(R.drawable.ic_default_profile_picture)
        viewProfileList.clear()
        viewProfileList.add(
            ViewProfileModel(
                icon = R.drawable.ic_profile,
                title = "Name", subTitle = "Vikrant Kahar"
            )
        )
        viewProfileList.add(
            ViewProfileModel(
                icon = R.drawable.ic_mail,
                title = "Email", subTitle = "kahar12@gmail.com"
            )
        )
        viewProfileList.add(
            ViewProfileModel(
                icon = R.drawable.ic_call,
                title = "Mobile Number", subTitle = "+91 9876543210"
            )
        )
        viewProfileList.add(
            ViewProfileModel(
                icon = R.drawable.ic_calendar,
                title = "Age", subTitle = "24"
            )
        )
        if (::viewProfileAdapter.isInitialized){
            viewProfileAdapter.notifyDataSetChanged()
        }
    }

    private fun setListener() {
        binding.apply {
            btnEditDetails.setOnClickListener {

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