package com.hipaasafe.presentation.home_screen.navigation_fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.google.gson.Gson
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.base.BaseApplication
import com.hipaasafe.base.BaseFragment
import com.hipaasafe.databinding.FragmentNavigationBinding
import com.hipaasafe.domain.model.notifications.MuteNotificationsRequestModel
import com.hipaasafe.presentation.help.HelpActivity
import com.hipaasafe.presentation.help.HelpViewModel
import com.hipaasafe.presentation.help.model.HelpItemType
import com.hipaasafe.presentation.help.model.HelpModel
import com.hipaasafe.presentation.home_screen.HomeActivity
import com.hipaasafe.presentation.home_screen.navigation_fragment.adapter.NavAdapter
import com.hipaasafe.presentation.home_screen.navigation_fragment.model.NavItemType
import com.hipaasafe.presentation.home_screen.navigation_fragment.model.NavigationModel
import com.hipaasafe.presentation.login_main.LoginMainActivity
import com.hipaasafe.presentation.notification.NotificationActivity
import com.hipaasafe.presentation.notification.NotificationViewModel
import com.hipaasafe.presentation.notification.model.NotificationSettingsMuteRequestModel
import com.hipaasafe.presentation.notification.model.NotificationSettingsUnMuteRequestModel
import com.hipaasafe.presentation.profile_view_details.ProfileViewDetailsActivity
import com.hipaasafe.settings.CometChatSettings
import com.hipaasafe.utils.AppUtils
import com.hipaasafe.utils.CometChatUtils
import com.hipaasafe.utils.PreferenceUtils
import com.hipaasafe.utils.enum.LoginUserType
import com.hipaasafe.utils.isNetworkAvailable
import com.onesignal.OneSignal
import org.json.JSONObject
import org.koin.android.viewmodel.ext.android.viewModel

enum class NavMenu(val value:Int){
    DND_MENU(3)
}
class NavigationFragment : BaseFragment(), NavAdapter.NavClickManager {
    companion object {
        fun newInstance(): NavigationFragment {
            return NavigationFragment()
        }
    }
    private val helpViewModel: HelpViewModel by viewModel()

    private val notificationViewModel: NotificationViewModel by viewModel()
    private var navMenuList: ArrayList<NavigationModel> = ArrayList()
    lateinit var binding: FragmentNavigationBinding
    lateinit var navAdapter: NavAdapter
    var name = ""
    var profile = ""
    var loginUserId: Int = 0
    var isMuteNotifications: Boolean = false
    var isDndChecked:Boolean = false

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
        setUpObserver()
        setUpAdapter()
    }

    private fun callHelpDetailsApi() {
        binding.apply {
            if (requireActivity().isNetworkAvailable()) {
                toggleLoader(true)
                helpViewModel.callGetStaticDetailsApi()
            } else {
                showToast(getString(R.string.no_internet_connection))
            }
        }
    }

    private fun setUpObserver() {
        binding.apply {
            with(notificationViewModel){
                muteNotificationsResponseData.observe(requireActivity()){
                    toggleLoader(false)
                    if (it.success == true){
                        callCometChatEnableOrDisableNotificationsApi(isDndChecked)
                    }else{
                        navMenuList[3].isChecked = !isDndChecked
                        navAdapter.notifyItemChanged(3)
                        showToast(it.message.toString())
                    }
                }
            }
            with(helpViewModel) {
                helpDetailsResponseData.observe(requireActivity()) {
                    toggleLoader(false)
                    if (it.success == true) {
                        val inviteText = it.data[2].value
                        AppUtils.INSTANCE?.invite(requireContext(),inviteText.toString())
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

    override fun onResume() {
        super.onResume()
        getPreferenceData()
    }

    private fun getPreferenceData() {
        binding.apply {
            name = preferenceUtils.getValue(Constants.PreferenceKeys.name)
            profile = preferenceUtils.getValue(Constants.PreferenceKeys.avatar)
            loginUserId =
                preferenceUtils.getValue(Constants.PreferenceKeys.role_id).toIntOrNull() ?: 0
            isMuteNotifications =
                preferenceUtils.getValue(Constants.PreferenceKeys.mute_notifications, false)
            setUpNavMenuList()
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
                    profile = profile,
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
            if (loginUserId == LoginUserType.DOCTOR.value) {
                navMenuList.add(
                    NavigationModel(
                        navItemType = NavItemType.ITEM_DND,
                        title = getString(R.string.do_not_disturb),
                        icon = R.drawable.ic_dnd,
                        isChecked = isMuteNotifications
                    )
                )
                navMenuList.add(
                    NavigationModel(
                        navItemType = NavItemType.ITEM_MENU,
                        title = getString(R.string.invite),
                        icon = R.drawable.ic_sms_tracking
                    )
                )
            }
            navMenuList.add(
                NavigationModel(
                    navItemType = NavItemType.ITEM_SIGN_OUT,
                    title = getString(R.string.sign_out)
                )
            )
            if (::navAdapter.isInitialized) {
                navAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onClickClose(position: Int) {
        (context as HomeActivity).binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun onClickViewProfile(position: Int) {
        val i = Intent(requireContext(), ProfileViewDetailsActivity::class.java)
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
            getString(R.string.invite) -> {
                callHelpDetailsApi()
            }
        }
    }

    override fun onClickSignOut(position: Int) {
        showLogoutDialog()
    }

    override fun onClickDnd(position: Int, isChecked: Boolean) {
        isDndChecked = isChecked
        callOneSignalMuteNotificationsApi(isChecked)
    }

    private fun callOneSignalMuteNotificationsApi(isChecked: Boolean) {
        binding.apply {
            if (requireActivity().isNetworkAvailable()) {
                toggleLoader(true)
                notificationViewModel.callMuteNotificationsApi(
                    request =
                    MuteNotificationsRequestModel(mute_notifications = isChecked)
                )
            } else {
                showToast(getString(R.string.please_check_your_internet_connection))
            }
        }
    }


    private fun callCometChatEnableOrDisableNotificationsApi(isOneSignalMute: Boolean) {
        toggleLoader(true)
        var jsonObject = ""
        jsonObject = if (isOneSignalMute) {
            Gson().toJson(NotificationSettingsMuteRequestModel())
        } else {
            Gson().toJson(NotificationSettingsUnMuteRequestModel())
        }
        val body = JSONObject(jsonObject)

        CometChat.callExtension("push-notification", "POST", "/v1/user-settings", body,
            object : CometChat.CallbackListener<JSONObject?>() {
                override fun onSuccess(jsonObject: JSONObject?) {
                    toggleLoader(false)
                    navMenuList[NavMenu.DND_MENU.value].isChecked = isOneSignalMute

                    preferenceUtils.setValue(
                        Constants.PreferenceKeys.mute_notifications,
                        isOneSignalMute
                    )
                    CometChatSettings.enableSoundForCalls = !isOneSignalMute
                }

                override fun onError(e: CometChatException) {
                    toggleLoader(false)
                    navMenuList[NavMenu.DND_MENU.value].isChecked = isMuteNotifications
                    showToast(e.message.toString())
                    navAdapter.notifyDataSetChanged()
                }
            })
    }

    private fun toggleLoader(showLoader: Boolean) {
        binding.apply {
            toggleFadeView(
                root,
                contentLayout.layoutLoading,
                contentLayout.imageLoading,
                showLoader
            )
        }
    }


    private fun showLogoutDialog() {
        AlertDialog.Builder(requireActivity())
            .setMessage(getString(R.string.are_you_sure_you_want_to_logout))
            .setPositiveButton(
                getString(R.string.yes)
            ) { _, _ ->
                val token = preferenceUtils.getValue(Constants.FIREBASE_TOKEN)
                preferenceUtils.clear()
                preferenceUtils.setValue(Constants.FIREBASE_TOKEN,token)
                OneSignal.clearOneSignalNotifications()
                OneSignal.disablePush(true)
                BaseApplication.second = Constants.RESEND_OTP_SECOND
                CometChatUtils.logoutFromComet()
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