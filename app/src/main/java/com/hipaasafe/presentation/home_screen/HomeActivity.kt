package com.hipaasafe.presentation.home_screen

import android.content.Intent
import android.os.Bundle
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivityHomeBinding
import com.hipaasafe.presentation.home_screen.home_fragment.HomeFragment
import com.hipaasafe.presentation.home_screen.home_fragment.HomeFragmentDoctor
import com.hipaasafe.presentation.home_screen.navigation_fragment.NavigationFragment
import com.hipaasafe.presentation.past_appointments.PastAppointmentsActivity
import com.hipaasafe.utils.AppUtils
import com.hipaasafe.utils.PreferenceUtils
import com.hipaasafe.utils.enum.LoginUserType


class HomeActivity : BaseActivity(),ToolbarActionListener {
    lateinit var binding: ActivityHomeBinding
    lateinit var toggle: ActionBarDrawerToggle
    var homeFragment = HomeFragment.newInstance()
    var homeFragmentDoctor = HomeFragmentDoctor.newInstance()
    var navFragment = NavigationFragment.newInstance()
    var loginUserId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferenceUtils = PreferenceUtils(this)
        getIntentData()
        getPreferenceData()
        setUpToolbar()
        setUpView()
    }

    private fun getIntentData() {
        binding.apply {

        }
    }

    private fun getPreferenceData() {
        binding.apply {
            loginUserId = preferenceUtils.getValue(Constants.PreferenceKeys.role_id).toIntOrNull()?:0
        }
    }

    fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.my_appointments)
            tvDate.visibility = VISIBLE
            tvDate.text = AppUtils.INSTANCE?.getCurrentDate()
            btnOne.apply {
                setImageResource(R.drawable.ic_past_history)
                setOnClickListener {
                    val i = Intent(this@HomeActivity,PastAppointmentsActivity::class.java)
                    startActivity(i)
                }
                visibility = VISIBLE
            }
            setSupportActionBar(homeToolbar)
            homeToolbar.contentInsetStartWithNavigation = 0
            toggle = ActionBarDrawerToggle(
                this@HomeActivity,
                binding.drawerLayout,
                homeToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )
            binding.drawerLayout.addDrawerListener(toggle)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
            homeToolbar.setNavigationOnClickListener {
               binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }
    }

    private fun setUpView() {
        if (loginUserId == LoginUserType.PATIENT.value){
            setFragment(homeFragment)
        }else{
            setFragment(homeFragmentDoctor)
        }
        setNav(navFragment)
    }

    private fun setNav(fragment: Fragment) {
        binding.apply {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(navLayout.id, fragment)
            transaction.commit()
        }
    }

    private fun setFragment(fragment: Fragment) {
        binding.apply {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(homeContainer.id, fragment)
            transaction.commit()
        }
    }

    override fun drawerItemSelected() {
        binding.drawerLayout.closeDrawer(GravityCompat.START,false)
    }

    private val TIME_INTERVAL =
        2000 // # milliseconds, desired time passed between two back presses.

    private var mBackPressed: Long = 0

//    override fun onBackPressed() {
//        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
//            super.onBackPressed()
//            return
//        } else {
//            Toast.makeText(baseContext, "Tap back button in order to exit", Toast.LENGTH_SHORT)
//                .show()
//        }
//        mBackPressed = System.currentTimeMillis()
//    }

}

interface ToolbarActionListener {
    fun drawerItemSelected()
}