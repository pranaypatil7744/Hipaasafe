package com.hipaasafe.presentation.home_screen

import android.content.Intent
import android.os.Bundle
import android.view.View.VISIBLE
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.hipaasafe.R
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivityHomeBinding
import com.hipaasafe.presentation.home_screen.appointment_fragment.AppointmentFragment
import com.hipaasafe.presentation.home_screen.home_fragment.HomeFragment
import com.hipaasafe.presentation.home_screen.navigation_fragment.NavigationFragment
import com.hipaasafe.presentation.past_appointments.PastAppointmentsActivity
import com.hipaasafe.utils.AppUtils

class HomeActivity : BaseActivity(),ToolbarActionListener {
    lateinit var binding: ActivityHomeBinding
    lateinit var toggle: ActionBarDrawerToggle
    var homeFragment = HomeFragment.newInstance()
    var navFragment = NavigationFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpToolbar()
        setUpView()
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
        setFragment(homeFragment)
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

}

interface ToolbarActionListener {
    fun drawerItemSelected()
}