package com.hipaasafe.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hipaasafe.BuildConfig
import com.hipaasafe.presentation.login.model.CountryModel
import com.onesignal.OneSignal
import java.io.IOException
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class AppUtils {

    lateinit var preferenceUtils: PreferenceUtils
    companion object {
        var INSTANCE: AppUtils? = null

        fun setInstance() {
            if (INSTANCE == null) {
                INSTANCE = AppUtils()
            }
        }
    }

    fun getCurrentDate(): String {
        val time = Calendar.getInstance().time
        val sdf = SimpleDateFormat("dd MMM yyyy")
        return sdf.format(time)
    }

    fun convertSecondToTime(millis: Long): String {
        val secs: Long = millis / 1000
        return String.format("%02d:%02d", (secs % 3600) / 60, secs % 60);

    }

    fun isValidEmail(email: String?) = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun isValidMobileNumber(s: String): Boolean {
        return if (s.isNotEmpty()) {
            val p = Pattern.compile("(0/91)?[6-9][0-9]{9}")
            val m = p.matcher(s)
            m.find() && m.group() == s
        } else {
            true
        }
    }

    fun getJsonDataFromAsset(context: Context, fileName: String): String? {

        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return ""
        }
        return jsonString
    }

    fun getPlayerId(): String? {
        var playerID: String? = ""

        val deviceState = OneSignal.getDeviceState()
        playerID = deviceState?.userId
        return playerID
    }

    fun openDialer(context: Context, infoMobile: String) {
        val i = Intent(Intent.ACTION_DIAL)
        i.data = Uri.parse("tel:" + infoMobile)
        context.startActivity(i)
    }

    fun openMailer(context: Context, mailTo: String) {
        val i = Intent(Intent.ACTION_SENDTO)
        i.data = Uri.parse("mailto: " + mailTo)
        context.startActivity(i)

    }

    fun getSpecialityWithLocationAndExperience(
        speciality: String?,
        location: String?,
        experience: String?
    ): String {
        var result = ""
        if (!TextUtils.isEmpty(speciality)) {
            result = "$speciality - "
        }
        if (!TextUtils.isEmpty(location)) {
            result = "$result$location - "
        }
        if (!TextUtils.isEmpty(experience) || !TextUtils.equals(experience, "0.0")) {
            result = "$result$experience yrs"
        }
        return result
    }

    fun getCountriesList(context: Context): ArrayList<CountryModel> {
        val countryCodeListString =
            INSTANCE?.getJsonDataFromAsset(context, "countries.json")
        val countryType: Type? = object : TypeToken<ArrayList<CountryModel?>>() {}.type
        return INSTANCE?.getDataFromJson(
            countryCodeListString ?: "",
            countryType
        ) as ArrayList<CountryModel>
    }
    fun getDataFromJson(stringData: String, objectType: Type?): Any {
        val gson = Gson()
        return gson.fromJson(stringData, objectType)
    }

    fun getUSIndex(countryList: ArrayList<CountryModel>): Int {
        for (i in 0 until countryList.size) {
            val country = countryList[i]
            if (country.dial_code.equals("+1")) {
                return i
            }
        }
        return 0
    }

    fun logMe(tag: String, message: String?) {
        if(BuildConfig.DEBUG){
            Log.e(tag, message ?: "")
        }
    }

    fun showView(view: View, duration: Long) {
        //Alpha 1 indicates completely visible
        view.animate().alpha(1.0f).duration = 400

        //        view.visibility = View.VISIBLE
        //            .setListener(object : AnimatorListenerAdapter() {
        //                override fun onAnimationEnd(animation: Animator) {
        //                    view.setVisibility(View.GONE)
        //                }
        //            })
    }

    fun hideView(view: View, duration: Long) {
        //alpha 0 indicates completely transaparent
        //        view.visibility = View.GONE
        view.animate().alpha(0f).duration = 400
        //            .setListener(object : AnimatorListenerAdapter() {
        //                override fun onAnimationEnd(animation: Animator) {
        //                    view.setVisibility(View.GONE)
        //                }
        //            })
    }

    fun showFadeView(view: View, duration: Long) {
        //        view.visibility = View.VISIBLE

        view.animate().alpha(1.0f).duration = duration
        //            .setListener(object : AnimatorListenerAdapter() {
        //                override fun onAnimationEnd(animation: Animator) {
        //                    view.setVisibility(View.GONE)
        //                }
        //            })
    }

    fun hideFadeView(view: View, duration: Long) {
        //        view.visibility = View.GONE

        view.animate().alpha(0.2f).duration = duration
        //            .setListener(object : AnimatorListenerAdapter() {
        //                override fun onAnimationEnd(animation: Animator) {
        //                    view.setVisibility(View.GONE)
        //                }
        //            })
    }

}