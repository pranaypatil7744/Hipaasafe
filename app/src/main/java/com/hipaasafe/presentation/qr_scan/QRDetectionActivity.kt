package com.hipaasafe.presentation.qr_scan

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.util.SparseArray
import android.view.SurfaceHolder
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.gson.GsonBuilder
import com.hipaasafe.Constants
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivityQrDetectionBinding
import com.hipaasafe.utils.AppUtils
import java.util.*


/**
 * https://code.tutsplus.com/tutorials/reading-qr-codes-using-the-mobile-vision-api--cms-24680
 * https://www.truiton.com/2016/09/android-example-programmatically-scan-qr-code-and-bar-code/
 */

class QRDetectionActivity : BaseActivity(){
//    private var mCameraSource by Delegates.notNull<CameraSource>()
//    private var barcodeDetector by Delegates.notNull<BarcodeDetector>()


    private var mCameraSource:CameraSource?=null
    private var barcodeDetector:BarcodeDetector?=null
    private val CAMERA_PERMISSION_REQUEST_CODE = 102
    lateinit var binding:ActivityQrDetectionBinding


    var detectedTextList = ArrayList<String>()
    var scannedInformationList = ArrayList<String>()

    var qrContacModel: QRContactResponseModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpToolbar()
        startCameraSource()
        setUpDetector()
    }

    private fun setUpDetector() {
        barcodeDetector?.setProcessor(object : Detector.Processor<Barcode> {
            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                try {
                    val barcodes: SparseArray<Barcode> = detections.detectedItems
                    binding.tvResult.post {

                        for (index in 0 until barcodes.size()) {
                            val rawValue = barcodes.valueAt(index).rawValue
                            val code = barcodes.valueAt(index)
                            val type = barcodes.valueAt(index).valueFormat
                            if (rawValue.contains("\n")) {
                                val split = rawValue.split("\n")
                                detectedTextList.addAll(split)
                            } else if (rawValue.contains("|")) {
                                val split = rawValue.split("|")
                                detectedTextList.addAll(split)
                            } else {
                                detectedTextList.add(rawValue)
                            }
                            if (type == Barcode.CONTACT_INFO) {
                                try {

                                    scannedInformationList.addAll(detectedTextList)
//                                        FirebaseCrashlytics.getInstance()
//                                            .setCustomKey(
//                                                Constants.OCR_TEXT,
//                                                detectedTextList.toString()
//                                            )
                                    var company = code.contactInfo.organization
                                    val website = AppUtils.INSTANCE?.extractWebsite(detectedTextList)
                                    val name = code.contactInfo.name.formattedName
                                    val mailingMsgName = code.contactInfo.name.formattedName
                                    val title = if(!TextUtils.isEmpty(code.contactInfo.title)){
                                        arrayListOf<String>(code.contactInfo.title?:"")
                                    }
                                    else{
                                        AppUtils.INSTANCE?.extractTitle(detectedTextList)

                                    }
                                    val jobTitle =
                                        if (title?.isNotEmpty() == true) title.first() else ""
                                    val emailList = code.contactInfo.emails
                                    val mobileList = code.contactInfo.phones

                                    var businessEmail = ""
                                    var personalEmail = ""
                                    var otherEmail = ""
                                    var mobile = ""
                                    var landline = ""
                                    var otherNumber = ""

                                    for (email in emailList) {
                                        val isPersonalEmail =
                                            AppUtils.INSTANCE?.isPersonalEmail(email.address)
                                                ?: false
                                        if (isPersonalEmail) {
                                            //its personal email
                                            personalEmail = email.address
                                        } else {
                                            //its business email
                                            businessEmail = email.address
                                        }

                                        if (!TextUtils.isEmpty(personalEmail) && !TextUtils.isEmpty(
                                                businessEmail
                                            )
                                        ) {
                                            //If its personal email and business email both are filled. than save next email in other email
                                            otherEmail = email.address
                                        }
                                    }

                                    for (phone in mobileList) {
                                        if (TextUtils.isEmpty(mobile)) {
                                            mobile = phone.number
                                            break
                                        }
                                        if (TextUtils.isEmpty(landline)) {
                                            landline = phone.number
                                            break
                                        }
                                        if (TextUtils.isEmpty(landline)) {
                                            otherNumber = phone.number
                                        }
                                    }

                                    if (TextUtils.isEmpty(company)) {
                                        company = AppUtils.INSTANCE?.extracCompany(
                                            detectedTextList,
                                            website?:"",
                                            businessEmail
                                        )

                                    }

                                    qrContacModel = QRContactResponseModel(
                                        companyName = company,
                                        name = name,
                                        mailingMsgName = mailingMsgName,
                                        jobTitle = jobTitle ?: "",
                                        businessEmail = businessEmail,
                                        personalEmail = personalEmail,
                                        otherEmail = otherEmail,
                                        mobile = mobile,
                                        landline = landline,
                                        otherNumber = otherNumber
                                    )

                                    callSetResult()
                                } catch (e: Exception) {
                                    scannedInformationList.addAll(detectedTextList)
                                    callSetResult()


                                }
                            } else {

                                scannedInformationList.addAll(detectedTextList)
                                callSetResult()

                            }
                        }


                    }
                } catch (e: Exception) {
//                    AppUtils.INSTANCE?.logMe("EXCEPTION_TAG", e.toString())
                }


            }

            override fun release() {

            }
        })
    }

    private fun releaseResources() {
        mCameraSource?.stop()
        mCameraSource?.release()
        barcodeDetector?.release()
        binding.cameraView.holder.surface.release()
    }

    private fun callSetResult() {
        releaseResources()
        val gson = GsonBuilder().create()
        val bundle = Bundle()
        bundle.putString(Constants.DETECTED_TEXT, gson.toJson(scannedInformationList))
        qrContacModel?.let {

            bundle.putSerializable(Constants.QR_MODEL, it)
        }
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun startCamera() {
        try {
            mCameraSource?.start(binding.cameraView.holder)

        } catch (e: SecurityException) {
//            TODO log here crashlytics.
        }


    }

//    private val requestCameraPermissions =
//        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
////            preferenceUtils.setValue(Constants.IS_VOICE_PERMISSION_ASKED, true)
//            if (isGranted) {
//                startCamera()
//            }
//
//
//        }

    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val isPermissionAsked = false
//            val isPermissionAsked = preferenceUtils.getValue(Constants.IS_IMAGE_PERMISSION_ASKED, false)
            if (isPermissionAsked) {
                //Returns true if user has previously denied permission
                //Returns false if user has selected dont ask again.
                val isPermissionDenied = ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CAMERA
                )
                if (isPermissionDenied) {
//                    requestCameraPermissions.launch(Manifest.permission.CAMERA)
                    requestCameraPermission()

                } else {
//                    DialogUtils.showPermissionDialog(
//                        this,
//                       "please_allow_a_permission_to_scan_card"
//                    )
                    showToast("please_allow_a_permission_to_scan_card")


                }
            } else {
//                requestCameraPermissions.launch(Manifest.permission.CAMERA)
                requestCameraPermission()
            }
        } else {
            //User has granted permissions
            startCamera()
        }
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            startCamera()
        }else{
            showToast("permission denied")
        }
    }


    private fun startCameraSource() {
        barcodeDetector = BarcodeDetector.Builder(this)
//            .setBarcodeFormats(Barcode.DATA_MATRIX or Barcode.QR_CODE)
            .setBarcodeFormats(Barcode.QR_CODE)
            .build()

        if (barcodeDetector?.isOperational == false) {
            showToast("dependencies_not_loaded_error")
//            AppUtils.INSTANCE?.logMe("OCR", "Dependencies are downloading....try after few moment")
            return

        }
        //  Init camera source to use high resolution and auto focus
        mCameraSource = CameraSource.Builder(this, barcodeDetector)
            .setFacing(CameraSource.CAMERA_FACING_BACK)
            .setRequestedPreviewSize(1024, 768)
            .setAutoFocusEnabled(true)
            .setRequestedFps(2.0f)
            .build()
        binding.cameraView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    requestPermissions()
                } catch (e: Exception) {
                    //TODO log here crashlytics
                    showToast("Error:  ${e.message}")
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {

                mCameraSource?.stop()
            }
        })
    }

    private fun setUpToolbar() {
//        toolbarTitle.text = getString(R.string.scan_qr)
//        toolbarLogo?.setOnClickListener {
//            finish()
//        }
    }
}