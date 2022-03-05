package com.hipaasafe.presentation.notification

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import com.hipaasafe.R
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivityNotificationBinding
import com.hipaasafe.presentation.notification.adapter.NotificationAdapter

class NotificationActivity : BaseActivity(), NotificationAdapter.NotificationManager {

    lateinit var binding: ActivityNotificationBinding
    lateinit var notificationAdapter: NotificationAdapter

    var notificationList: ArrayList<NotificationResult> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpToolbar()
        setUpNotificationList()
        setUpNotificationAdapter()
    }

    private fun setUpNotificationList() {
        binding.apply {
            notificationList.clear()
            notificationList.add(NotificationResult(
                _id = "1",title = "", message = "Akash has sent you a connection request", status = true, createdAt = ""
            ))
            notificationList.add(NotificationResult(
                _id = "1",title = "", message = "Akash has sent you a connection request", status = false, createdAt = ""
            ))
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

    private fun setUpNotificationAdapter() {
        notificationAdapter = NotificationAdapter(this, notificationList, this)
        binding.recyclerNotifications.apply {
            adapter = notificationAdapter
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            divider.visibility = View.VISIBLE
            btnBack.visibility = View.VISIBLE
            btnBack.setOnClickListener {
                finish()
            }
            tvDate.visibility = GONE
            tvTitle.text = getString(R.string.notifications)
        }

    }

    override fun onNotificationItemClicked(position: Int) {
        val data = notificationList[position]
        if (!data.status) {

        }
    }

}