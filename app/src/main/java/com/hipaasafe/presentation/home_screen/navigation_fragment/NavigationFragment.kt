package com.hipaasafe.presentation.home_screen.navigation_fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.base.BaseApplication
import com.hipaasafe.base.BaseFragment
import com.hipaasafe.databinding.FragmentNavigationBinding
import com.hipaasafe.presentation.help.HelpActivity
import com.hipaasafe.presentation.home_screen.HomeActivity
import com.hipaasafe.presentation.home_screen.navigation_fragment.adapter.NavAdapter
import com.hipaasafe.presentation.home_screen.navigation_fragment.model.NavItemType
import com.hipaasafe.presentation.home_screen.navigation_fragment.model.NavigationModel
import com.hipaasafe.presentation.login_main.LoginMainActivity
import com.hipaasafe.presentation.notification.NotificationActivity
import com.hipaasafe.presentation.profile_view_details.ProfileViewDetailsActivity
import com.hipaasafe.utils.PreferenceUtils
import com.onesignal.OneSignal

class NavigationFragment : BaseFragment(), NavAdapter.NavClickManager {
    companion object {
        fun newInstance(): NavigationFragment {
            return NavigationFragment()
        }
    }

    private var navMenuList: ArrayList<NavigationModel> = ArrayList()
    lateinit var binding: FragmentNavigationBinding
    lateinit var navAdapter: NavAdapter
    var name = ""
    var profile = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNavigationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferenceUtils = PreferenceUtils(requireContext())
        setUpAdapter()
    }

    override fun onResume() {
        super.onResume()
        getPreferenceData()
        setUpNavMenuList()
    }

    private fun getPreferenceData() {
        binding.apply {
            name = preferenceUtils.getValue(Constants.PreferenceKeys.name)
            profile = preferenceUtils.getValue(Constants.PreferenceKeys.avatar)
        }
    }

    private fun setUpAdapter() {
        binding.apply {
            navAdapter = NavAdapter(requireContext(), navMenuList, this@NavigationFragment)
            recyclerNavMenu.adapter = navAdapter
        }
    }

    private fun setUpNavMenuList() {
        binding.apply {
            navMenuList.clear()
            navMenuList.add(
                NavigationModel(
                    title = name,
                    icon = R.drawable.ic_default_profile_picture,
                    navItemType = NavItemType.ITEM_PROFILE,
                )
            )
            navMenuList.add(NavigationModel(navItemType = NavItemType.DIVIDER))
            navMenuList.add(
                NavigationModel(
                    navItemType = NavItemType.ITEM_MENU,
                    title = getString(R.string.help_support),
                    icon = R.drawable.ic_help
                )
            )
            navMenuList.add(
                NavigationModel(
                    navItemType = NavItemType.ITEM_MENU,
                    title = getString(R.string.notification),
                    icon = R.drawable.ic_notification
                )
            )
            navMenuList.add(
                NavigationModel(
                    navItemType = NavItemType.ITEM_SIGN_OUT,
                    title = getString(R.string.sign_out)
                )
            )
        }
    }

    override fun onClickClose(position: Int) {
        (context as HomeActivity).binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun onClickViewProfile(position: Int) {
        val i = Intent(requireContext(),ProfileViewDetailsActivity::class.java)
        startActivity(i)
    }

    override fun onClickMenu(position: Int) {
        when (navMenuList[position].title) {
            getString(R.string.help_support) -> {
                val i = Intent(requireContext(), HelpActivity::class.java)
                startActivity(i)
            }
            getString(R.string.notification) -> {
                val i = Intent(requireContext(), NotificationActivity::class.java)
                startActivity(i)
            }
        }
    }

    override fun onClickSignOut(position: Int) {
        showLogoutDialog()
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(requireActivity())
            .setMessage(getString(R.string.are_you_sure_you_want_to_logout))
            .setPositiveButton(
                getString(R.string.yes)
            ) { _, _ ->
                preferenceUtils.clear()
                OneSignal.clearOneSignalNotifications()
                OneSignal.disablePush(true)
                BaseApplication.second = Constants.RESEND_OTP_SECOND
                navigateToLoginScreen()
            }.setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }.setIcon(android.R.drawable.ic_dialog_alert).show()
    }

    private fun navigateToLoginScreen() {
        val loginIntent = Intent(requireActivity(), LoginMainActivity::class.java)
        loginIntent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(loginIntent)
    }
}