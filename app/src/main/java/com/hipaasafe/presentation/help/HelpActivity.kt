package com.hipaasafe.presentation.help

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.hipaasafe.R
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivityHelpBinding
import com.hipaasafe.presentation.help.adapter.HelpAdapter
import com.hipaasafe.utils.AppUtils

class HelpActivity : BaseActivity(), HelpAdapter.HelpManager {
    lateinit var binding: ActivityHelpBinding
    var helpList: ArrayList<HelpModel> = ArrayList()
    lateinit var helpAdapter: HelpAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpToolbar()
        setUpData()
    }

    private fun setUpData() {
        helpList.clear()
        helpList.add(HelpModel(type = HelpItemType.HEADER))
        helpList.add(
            HelpModel(
                type = HelpItemType.EMAIL,
                icon = R.drawable.img_email,
                label = getString(R.string.send_us_an_e_mail),
                data = getString(R.string.info_docusecure_com)
            )
        )
        helpList.add(
            HelpModel(
                type = HelpItemType.CALL,
                icon = R.drawable.img_call,
                label = getString(R.string.you_can_call_us),
                data = getString(R.string._91_9876543210)
            )
        )
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

    private fun setUpAdapter() {
        helpAdapter = HelpAdapter(this, helpList, this)
        binding.recyclerHelp.adapter = helpAdapter
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            divider.visibility = View.VISIBLE
            tvTitle.text = getString(R.string.help)
            tvDate.visibility = GONE
            btnBack.visibility = VISIBLE
            btnBack.setOnClickListener {
                finish()
            }
        }
    }

    override fun onItemClicked(data: HelpModel) {
        if (data.type == HelpItemType.EMAIL) {
            AppUtils.INSTANCE?.openMailer(this, data.data)
        } else {
            AppUtils.INSTANCE?.openDialer(this, data.data)

        }
    }
}