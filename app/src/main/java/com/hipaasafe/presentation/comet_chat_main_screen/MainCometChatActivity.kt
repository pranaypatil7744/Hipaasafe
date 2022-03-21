package com.hipaasafe.presentation.comet_chat_main_screen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.core.Call
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.core.CometChat.sendMediaMessage
import com.cometchat.pro.core.GroupMembersRequest
import com.cometchat.pro.core.MessagesRequest
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivityMainCometChatBinding
import com.hipaasafe.databinding.BottomSheetAttachmentBinding
import com.hipaasafe.databinding.BottomSheetMessageActionBinding
import com.hipaasafe.extension.Extensions
import com.hipaasafe.presentation.attach_document.AttachmentActivity
import com.hipaasafe.settings.CometChatFeatureRestriction
import com.hipaasafe.presentation.comet_chat_main_screen.adapter.AttachmentActionsAdapter
import com.hipaasafe.presentation.comet_chat_main_screen.adapter.MainChatAdapter
import com.hipaasafe.presentation.comet_chat_main_screen.adapter.MainChatAdapter.Companion.LEFT_AUDIO_MESSAGE
import com.hipaasafe.presentation.comet_chat_main_screen.adapter.MainChatAdapter.Companion.LEFT_FILE_MESSAGE
import com.hipaasafe.presentation.comet_chat_main_screen.adapter.MainChatAdapter.Companion.LEFT_IMAGE_MESSAGE
import com.hipaasafe.presentation.comet_chat_main_screen.adapter.MainChatAdapter.Companion.LEFT_LOCATION_CUSTOM_MESSAGE
import com.hipaasafe.presentation.comet_chat_main_screen.adapter.MainChatAdapter.Companion.LEFT_REPLY_TEXT_MESSAGE
import com.hipaasafe.presentation.comet_chat_main_screen.adapter.MainChatAdapter.Companion.LEFT_TEXT_MESSAGE
import com.hipaasafe.presentation.comet_chat_main_screen.adapter.MainChatAdapter.Companion.LEFT_VIDEO_MESSAGE
import com.hipaasafe.presentation.comet_chat_main_screen.adapter.MainChatAdapter.Companion.RIGHT_AUDIO_MESSAGE
import com.hipaasafe.presentation.comet_chat_main_screen.adapter.MainChatAdapter.Companion.RIGHT_FILE_MESSAGE
import com.hipaasafe.presentation.comet_chat_main_screen.adapter.MainChatAdapter.Companion.RIGHT_IMAGE_MESSAGE
import com.hipaasafe.presentation.comet_chat_main_screen.adapter.MainChatAdapter.Companion.RIGHT_LOCATION_CUSTOM_MESSAGE
import com.hipaasafe.presentation.comet_chat_main_screen.adapter.MainChatAdapter.Companion.RIGHT_REPLY_TEXT_MESSAGE
import com.hipaasafe.presentation.comet_chat_main_screen.adapter.MainChatAdapter.Companion.RIGHT_TEXT_MESSAGE
import com.hipaasafe.presentation.comet_chat_main_screen.adapter.MainChatAdapter.Companion.RIGHT_VIDEO_MESSAGE
import com.hipaasafe.presentation.comet_chat_main_screen.model.AttachmentMenuModel
import com.hipaasafe.presentation.comet_chat_main_screen.model.MembersWithColor
import com.hipaasafe.presentation.forward_message_screen.ForwardMessageActivity
import com.hipaasafe.presentation.home_screen.HomeActivity
import com.hipaasafe.presentation.image_viewer.ImageViewerActivity
import com.hipaasafe.presentation.upload_documents.UploadDocumentsActivity
import com.hipaasafe.presentation.view_documents.ViewDocumentsActivity
import com.hipaasafe.presentation.web_view.WebViewActivity
import com.hipaasafe.utils.*
import com.hipaasafe.utils.CometChatUtils.Companion.initiateCall
import com.hipaasafe.utils.enum.LoginUserType
import com.hipaasafe.utils.sticker_header.StickyHeaderDecoration
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class MainCometChatActivity : BaseActivity(),
    MainChatAdapter.MessageClickManager, AttachmentActionsAdapter.AttachmentActions,
    DialogUtils.DialogManager {
    companion object {
        private const val TAG = "CometChatMessageScreen"
        private const val LIMIT = 30
    }

    var isFromCreateGroup: Boolean = false
    var isFromMyTeam:Boolean = false

    lateinit var binding: ActivityMainCometChatBinding
    lateinit var attachmentBottomSheetDialog: BottomSheetDialog
    lateinit var bottomSheetMessageActionBinding: BottomSheetMessageActionBinding
    lateinit var mainChatAdapter: MainChatAdapter
    private var stickyHeaderDecoration: StickyHeaderDecoration? = null

    var msgList: ArrayList<BaseMessage> = ArrayList()
    var membersList: ArrayList<MembersWithColor> = ArrayList()
    var isGroup: Boolean = false
    var isTyping: Boolean = false
    var isImpMsg: Boolean = false
    var isUploading: Boolean = false
    private var isOngoingCall: Boolean = false
    private var selectedMsgPosition: Int? = null
    lateinit var menu: Menu
    var attachmentMenuList: ArrayList<AttachmentMenuModel> = ArrayList()
    private var isEdit: Boolean = false
    private var isReply: Boolean = false
    private var isReplyPrivately: Boolean = false
    private var isInProgress = false
    private var isNoMoreMessages = false
    private var linearLayoutManager: LinearLayoutManager? = null
    private var baseMessage: BaseMessage? = null
    var isSendBtnEnable: Boolean = false
    var id: String = ""
    var chatName: String? = ""
    var groupType: String? = ""
    var groupPassword: String? = ""
    var userDetails: String = ""
    var profilePicUrl: String? = ""
    var groupOwnerId: String? = ""
    var type: String = ""
    var onlineStatus: String? = ""
    var memberCount: Int? = 0
    var memberNames: String? = ""
    var isHasBlockedByMe: Boolean = false
    var isBlockedByMe: Boolean = false
    var isCameraClick: Boolean = false
    private val loggedInUser = CometChat.getLoggedInUser()
    private var messagesRequest //Used to fetch messages.
            : MessagesRequest? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null
    private val MIN_TIME: Long = 1000
    private val MIN_DIST: Long = 5
    var loginUserType:Int =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainCometChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferenceUtils = PreferenceUtils(this)
        setUpAttachmentMenuList()
        getIntentData()
        setUpView()
        setUpListener()
        setUpToolbar()
    }

    private fun setUpAttachmentMenuList() {
        attachmentMenuList.clear()
        attachmentMenuList.add(
            AttachmentMenuModel(
                menuName = getString(R.string.scan_document),
                menuIcon = R.drawable.att_scan_doc
            )
        )
        attachmentMenuList.add(
            AttachmentMenuModel(
                menuName = getString(R.string.camera),
                menuIcon = R.drawable.att_camera
            )
        )
        attachmentMenuList.add(
            AttachmentMenuModel(
                menuName = getString(R.string.gallery),
                menuIcon = R.drawable.att_gallary
            )
        )
        attachmentMenuList.add(
            AttachmentMenuModel(
                menuName = getString(R.string.files),
                menuIcon = R.drawable.att_file
            )
        )
        attachmentMenuList.add(
            AttachmentMenuModel(
                menuName = getString(R.string.audio),
                menuIcon = R.drawable.att_audio
            )
        )
        attachmentMenuList.add(
            AttachmentMenuModel(
                menuName = getString(R.string.location),
                menuIcon = R.drawable.att_location
            )
        )
    }

    private fun setUpView() {
        binding.apply {
            linearLayoutManager =
                LinearLayoutManager(this@MainCometChatActivity, LinearLayoutManager.VERTICAL, false)
            btnAttachMedia.visibility = VISIBLE
        }
    }

    private fun setUpListener() {
        binding.apply {
            toolbar.apply {
                btnBack.setOnClickListener {
                    if (isFromCreateGroup) {
                        startHomeActivityFromGroup()
                    } else {
                        finish()
                    }
                }
                tvChatName.setOnClickListener {
                    if (loginUserType != LoginUserType.PATIENT.value){
                        if (type == CometChatConstants.RECEIVER_TYPE_GROUP){
                            startViewDocumentActivity()
                        }
                    }
                }
                imgChatIcon.setOnClickListener {
                    if (loginUserType != LoginUserType.PATIENT.value){
                        if (type == CometChatConstants.RECEIVER_TYPE_GROUP){
                            startViewDocumentActivity()
                        }
                    }
                }
                // for audio call
                toolbarIcon1.setOnClickListener {
                    if (!isOngoingCall && !isBlockedByMe && !isHasBlockedByMe) {
                        if (isNetworkAvailable()) {
                            initiateAudioCall()
                        } else {
                            showToast(getString(R.string.no_internet_connection_please_try_again_later))
                        }
                    }
                }
                // for video call
                toolbarIcon2.setOnClickListener {
                    if (!isOngoingCall && !isBlockedByMe && !isHasBlockedByMe) {
                        if (isNetworkAvailable()) {
                            initiateVideoCall()
                        } else {
                            showToast(getString(R.string.no_internet_connection_please_try_again_later))
                        }
                    }
                }
                btnEditClose.setOnClickListener {
                    hideEditMsgLayout()
                }
                replyLayout.btnReplyClose.setOnClickListener {
                    hideReplyMsgLayout()
                }
                btnIsImpMsg.setOnClickListener {
//                    if (isImpMsg) {
//                        isImpMsg = false
//                        btnIsImpMsg.setImageResource(R.drawable.img_is_important_gray)
//                    } else {
//                        isImpMsg = true
//                        btnIsImpMsg.setImageResource(R.drawable.img_is_important)
//                    }
                }
                etEnterMsg.doOnTextChanged { text, start, before, count ->
                    if (text.toString().trim().isNotEmpty()) {
                        btnSendMsg.apply {
                            alpha = 1F
                            isEnabled = true
                            isClickable = true
                            isSendBtnEnable = true
                        }
                        sendTypingIndicator(false)
                    } else {
                        btnSendMsg.apply {
                            alpha = 0.5F
                            isEnabled = false
                            isClickable = false
                            isSendBtnEnable = false
                        }
                        sendTypingIndicator(true)
                    }
                }
                btnSendMsg.setOnClickListener {
                    val msg = etEnterMsg.text.toString().trim()
                    if (isNetworkAvailable()) {
                        if (msg.isNotEmpty()) {
                            when {
                                isEdit -> {
                                    baseMessage?.let { it1 -> editMessage(it1, msg) }
                                }
                                isReply -> {
                                    replyMessage(baseMessage, msg)
                                }
                                else -> {
                                    etEnterMsg.setText("")
                                    etEnterMsg.hint = getString(R.string.write_a_message)
                                    sendMessage(msg)
                                }
                            }
                        }
                    } else {
                        showToast(getString(R.string.no_internet_connection_please_try_again_later))
                    }
                }
                btnAttachMedia.setOnClickListener {
//                    if (isNetworkAvailable()) {
//                        openAttachmentBottomSheet()
//                    } else {
//                        showToast(getString(R.string.no_internet_connection_please_try_again_later))
//                    }
                    val i: Intent = if (loginUserType == LoginUserType.PATIENT.value){
                        Intent(this@MainCometChatActivity,AttachmentActivity::class.java)
                    }else{
                        Intent(this@MainCometChatActivity,UploadDocumentsActivity::class.java)
                    }
                    val b = Bundle()
                    b.putBoolean(Constants.IsForAttachDoc,true)
                    b.putBoolean(Constants.IS_FROM_MY_TEAM,isFromMyTeam)
                    b.putString(Constants.AttachmentSendTo,id)
                    i.putExtras(b)
                    attachmentResult.launch(i)
                }
                recyclerChatMessages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                    }

                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        if (!isNoMoreMessages && !isInProgress) {
                            if (linearLayoutManager?.findFirstVisibleItemPosition() == 10 || !recyclerChatMessages.canScrollVertically(
                                    -1
                                )
                            ) {
                                isInProgress = true
                                fetchMessage()
                            }
                        }
                    }
                })
            }
        }
    }
    private val attachmentResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                binding.apply {
                    val data = result.data?.getStringExtra(Constants.DocumentLink)
                    etEnterMsg.setText(data)
                }
            }
        }

    private fun startViewDocumentActivity() {
        val i = Intent(this, ViewDocumentsActivity::class.java)
        val b = Bundle()
        b.putString(Constants.CometChatConstant.NAME, chatName)
        b.putString(Constants.CometChatConstant.GUID,id)
        i.putExtras(b)
        startActivity(i)
    }

    private fun initiateVideoCall() {
        binding.apply {
            if (type == CometChatConstants.RECEIVER_TYPE_USER) {
                CometChatUtils.initiateCall(
                    this@MainCometChatActivity,
                    id,
                    CometChatConstants.RECEIVER_TYPE_USER,
                    CometChatConstants.CALL_TYPE_VIDEO
                )
            } else {
                // for meeting
//                val body = JSONObject()
//                body.put("sessionID", id)
//                body.put("group_name", chatName)
//                body.put("groupType", if (groupType == null) "" else groupType)
//                body.put("password", if (groupPassword == null) "" else groupPassword)
//                sendCustomMessage(Constants.CometChat.MEETING, body)

//                startGroupCallIntent(id,CometChatConstants.RECEIVER_TYPE_GROUP,CometChatConstants.CALL_TYPE_VIDEO)
                initiateGroupCall(
                    id,
                    CometChatConstants.RECEIVER_TYPE_GROUP,
                    CometChatConstants.CALL_TYPE_VIDEO
                )

            }
        }
    }

    private fun initiateGroupCall(recieverID: String?, receiverType: String?, callType: String?) {
        val call = Call((recieverID)!!, receiverType, callType)
        CometChat.initiateCall(call, object : CometChat.CallbackListener<Call>() {
            override fun onSuccess(call: Call) {
                AppUtils.INSTANCE?.startGroupCallIntent(
                    this@MainCometChatActivity,
                    (call.callReceiver as Group),
                    call.type,
                    true,
                    call.sessionId
                )
            }

            override fun onError(e: CometChatException) {
                Toast.makeText(
                    this@MainCometChatActivity,
                    e.details,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun initiateAudioCall() {
        binding.apply {
            if (type == CometChatConstants.RECEIVER_TYPE_USER) {
                CometChatUtils.initiateCall(
                    this@MainCometChatActivity,
                    id,
                    CometChatConstants.RECEIVER_TYPE_USER,
                    CometChatConstants.CALL_TYPE_AUDIO
                )
            } else {
                CometChatUtils.initiateCall(
                    this@MainCometChatActivity,
                    id,
                    CometChatConstants.RECEIVER_TYPE_GROUP,
                    CometChatConstants.CALL_TYPE_AUDIO
                )
            }
        }
    }

    private fun openAttachmentBottomSheet() {
        attachmentBottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_attachment, null)
        val binding = BottomSheetAttachmentBinding.bind(view)
        val attachmentActionsAdapter = AttachmentActionsAdapter(this, attachmentMenuList, this)
        binding.apply {
            recyclerAttachmentMenu.adapter = attachmentActionsAdapter
        }
        attachmentBottomSheetDialog.apply {
            setContentView(view)
            setCancelable(true)
            show()
        }

    }

    private val requestLocationPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            preferenceUtils.setValue(Constants.AskedPermission.IS_LOCATION_PERMISSION_ASKED, true)

            val totalPermissionsCount = permissions.entries.size
            var allowedPermissionCount = 0
            permissions.entries.forEach {
                val permissionName = it.key
                val isGranted = it.value
                if (isGranted) {
                    allowedPermissionCount += 1
                }
            }
            if (totalPermissionsCount == allowedPermissionCount) {
                initLocation()
                val provider: Boolean =
                    locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
                if (!provider) {
                    turnOnLocation()
                } else {
                    getLocation()
                }
            } else {
                //Do nothing as user has not allowed permissions for image
            }
        }


    private val requestImagePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            preferenceUtils.setValue(Constants.AskedPermission.IS_PROFILE_PERMISSION_ASKED, true)

            val totalPermissionsCount = permissions.entries.size
            var allowedPermissionCount = 0
            permissions.entries.forEach {
                val permissionName = it.key
                val isGranted = it.value
                if (isGranted) {
                    allowedPermissionCount += 1
                }
            }
            if (totalPermissionsCount == allowedPermissionCount) {
                if (isCameraClick) {
                    captureImage()
                } else {
                    pickMedia()
                }
            } else {
                //Do nothing as user has not allowed permissions for image
            }

        }

    private fun getPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val isPermissionAsked =
                preferenceUtils.getValue(
                    Constants.AskedPermission.IS_PROFILE_PERMISSION_ASKED,
                    false
                )
            if (isPermissionAsked) {
                //Returns true if user has previously denied permission
                //Returns false if user has selected dont ask again.
                val isPermissionDenied = ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CAMERA
                ) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                if (isPermissionDenied) {
                    requestImagePermissions.launch(
                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    )
                } else {
                    if (isCameraClick) {
                        DialogUtils.showPermissionDialog(
                            this,
                            getString(R.string.permissions_to_update_profile_image),
                            getString(R.string.allow_permission),
                            getString(R.string.go_to_settings),
                            getString(R.string.deny)
                        )
                    } else {
                        DialogUtils.showPermissionDialog(
                            this,
                            getString(R.string.permissions_to_update_profile_image),
                            getString(R.string.allow_permission),
                            getString(R.string.go_to_settings),
                            getString(R.string.deny)
                        )
                    }
                }
            } else {
                requestImagePermissions.launch(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
            }
        } else {
            //User has granted permissions
            if (isCameraClick) {
                captureImage()
            } else {
                pickMedia()
            }
        }
    }

    private val resultCaptureFromCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val bitmap = result.data?.extras?.get("data") as Bitmap
//                binding.imgDoc.setImageBitmap(bitmap)
                bitmap.let {
                    val imageAbsolutePath: String? = ImageUtils.INSTANCE?.saveImage(it, this)
                    imageAbsolutePath?.let { url ->
                        val file = File(url)
                        if (file.exists()) {
                            sendMediaMessage(
                                file = file,
                                filetype = CometChatConstants.MESSAGE_TYPE_IMAGE
                            )
                        } else {
                            showToast("")
                        }
                    } ?: run {
                        showToast(getString(R.string.something_went_wrong))

                    }
                } ?: run {
                    showToast(getString(R.string.something_went_wrong))

                }
            }
        }

    private val resultPickFromGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val uri = result.data?.data as Uri
                val file = MediaUtilsCometChat.getRealPath(this, uri)
                val cr = contentResolver
                val mimeType = cr?.getType(uri)
                Log.e(TAG, "onActivityResult:file $file")
                if (mimeType != null && mimeType.contains("image")) {
                    if (file.exists()) {
                        sendMediaMessage(
                            file,
                            CometChatConstants.MESSAGE_TYPE_IMAGE
                        )
                    } else {
                        showToast("")
                    }
                } else {
                    if (file.exists()) {
                        sendMediaMessage(
                            file,
                            CometChatConstants.MESSAGE_TYPE_VIDEO
                        )
                    } else {
                        showToast("")
                    }
                }
            }
        }

    private val resultAudioPicker =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                if (result.data != null) {
                    val url = result?.data?.data
                    sendMediaMessage(
                        MediaUtilsCometChat.getRealPath(this, url),
                        CometChatConstants.MESSAGE_TYPE_AUDIO
                    )
                }
            }
        }

    private val resultFilePicker =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                if (result.data != null) {
                    val url = result?.data?.data
                    sendMediaMessage(
                        MediaUtilsCometChat.getRealPath(this, url),
                        CometChatConstants.MESSAGE_TYPE_FILE
                    )
                }
            }
        }

    private fun captureImage() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultCaptureFromCamera.launch(intent)
    }

    private fun pickMedia() {
//        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.type = "image/* video/*"
        resultPickFromGallery.launch(galleryIntent)
    }

    private fun openFilePicker() {
        val intent = Intent()
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_MIME_TYPES, Constants.CometChatConstant.EXTRA_MIME_DOC)
        intent.action = Intent.ACTION_OPEN_DOCUMENT
        resultFilePicker.launch(intent)
    }

    private fun openAudioPicker() {
        val intent = Intent()
        intent.type = "audio/*"
        intent.action = Intent.ACTION_GET_CONTENT
        resultAudioPicker.launch(intent)
    }

    @SuppressLint("MissingPermission")
    private fun initLocation() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {}
            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        try {
            locationListener?.let {
                locationManager?.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_TIME,
                    MIN_DIST.toFloat(),
                    it
                )
            }
            locationListener?.let {
                locationManager?.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME,
                    MIN_DIST.toFloat(),
                    it
                )
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationProviderClient?.lastLocation?.addOnSuccessListener { location ->
            if (location != null) {
                val lon = location.longitude
                val lat = location.latitude
                val customData = JSONObject()
                try {
                    customData.put("latitude", lat)
                    customData.put("longitude", lon)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                initAlert(customData)
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.unable_to_get_location),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun initAlert(customData: JSONObject) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.share_location_alert))
        builder.setPositiveButton(getString(R.string.share)) { dialog, which ->
            sendCustomMessage(
                Constants.CometChatConstant.LOCATION,
                customData
            )
        }.setNegativeButton(getString(R.string.cancel)) { dialog, which -> dialog.dismiss() }
        builder.create()
        builder.show()
    }

    override fun onPause() {
        super.onPause()
        removeGroupListener()
        removeUserListener()
        removeMessageListener()
    }

    private fun removeMessageListener() {
        CometChat.removeMessageListener(TAG)
    }

    override fun onResume() {
        super.onResume()
        checkOnGoingCall()
        stickyHeaderDecoration?.let { binding.recyclerChatMessages.removeItemDecoration(it) }
        if (isUploading) {
            isUploading = false
        } else {
            messagesRequest = null
            binding.recyclerChatMessages.adapter = null
        }
        fetchMessage()
        addMessageListener()
        if (isGroup) {
            if (!CometChatFeatureRestriction.isGroupActionMessagesEnabled())
                addGroupListener()
            Thread { group }.start()
            Thread { member }.start()
        } else {
            CometChatFeatureRestriction.isUserPresenceEnabled(object :
                CometChatFeatureRestriction.OnSuccessListener {
                override fun onSuccess(p0: Boolean) {
                    if (p0) {
                        addUserListener()
                    }
                }
            })
            Thread(Runnable { user }).start()
        }
    }

    private fun checkOnGoingCall() {
        binding.apply {
            if (CometChat.getActiveCall() != null && CometChat.getActiveCall().callStatus == CometChatConstants.CALL_STATUS_ONGOING
                && CometChat.getActiveCall().sessionId != null
            ) {
                showOnGoingCallUi()
                tvOnGoingCall.apply {
                    setOnClickListener {
                        AppUtils.INSTANCE?.joinOnGoingCall(this@MainCometChatActivity)
                    }
                }
            } else {
                hideOnGoingCallUi()
            }
        }
    }

    private fun hideOnGoingCallUi() {
        binding.apply {
            isOngoingCall = false
            toolbar.apply {
                toolbarIcon1.isEnabled = true
                toolbarIcon1.alpha = 1F
                toolbarIcon2.isEnabled = true
                toolbarIcon2.alpha = 1F
                tvOnGoingCall.visibility = GONE
            }
        }
    }

    private fun showOnGoingCallUi() {
        binding.apply {
            isOngoingCall = true
            toolbar.apply {
                toolbarIcon1.isEnabled = false
                toolbarIcon1.alpha = 0.5F
                toolbarIcon2.isEnabled = false
                toolbarIcon2.alpha = 0.5F
                tvOnGoingCall.visibility = VISIBLE
            }
        }
    }

    private fun addMessageListener() {
        CometChat.addMessageListener(
            TAG,
            object : CometChat.MessageListener() {
                override fun onTextMessageReceived(message: TextMessage) {
                    Log.d(TAG, "onTextMessageReceived: $message")
                    onMessageReceived(message)
                }

                override fun onMediaMessageReceived(message: MediaMessage) {
                    Log.d(TAG, "onMediaMessageReceived: $message")
                    onMessageReceived(message)
                }

                override fun onCustomMessageReceived(message: CustomMessage?) {
                    Log.d(
                        TAG,
                        "onCustomMessageReceived: " + message.toString()
                    )
                    onMessageReceived(message!!)
                }

                override fun onTypingStarted(typingIndicator: TypingIndicator) {
                    Log.e(TAG, "onTypingStarted: $typingIndicator")
                    setTypingIndicator(typingIndicator, true)
                }

                override fun onTypingEnded(typingIndicator: TypingIndicator) {
                    Log.d(TAG, "onTypingEnded: $typingIndicator")
                    setTypingIndicator(typingIndicator, false)
                }

                override fun onTransientMessageReceived(transientMessage: TransientMessage?) {
                    setTransientMessage(transientMessage)
                }

                override fun onMessagesDelivered(messageReceipt: MessageReceipt) {
                    Log.d(TAG, "onMessagesDelivered: $messageReceipt")
                    setMessageReciept(messageReceipt)
                }

                override fun onMessagesRead(messageReceipt: MessageReceipt) {
                    Log.e(TAG, "onMessagesRead: $messageReceipt")
                    setMessageReciept(messageReceipt)
                }

                override fun onMessageEdited(message: BaseMessage) {
                    Log.d(TAG, "onMessageEdited: $message")
                    updateMessage(message)
                }

                override fun onMessageDeleted(message: BaseMessage) {
                    Log.d(TAG, "onMessageDeleted: ")
                    updateMessage(message)
                }
            })
    }

    private fun setTransientMessage(transientMessage: TransientMessage?) {

    }

    private fun setMessageReciept(messageReceipt: MessageReceipt) {
        if (::mainChatAdapter.isInitialized) {
            if (messageReceipt.receivertype == CometChatConstants.RECEIVER_TYPE_USER) {
                if (id != null && messageReceipt.sender.uid == id) {
                    if (messageReceipt.receiptType == MessageReceipt.RECEIPT_TYPE_DELIVERED) mainChatAdapter.setDeliveryReceipts(
                        messageReceipt
                    ) else mainChatAdapter.setReadReceipts(messageReceipt)
                }
            }
        }
    }

    private fun setTypingIndicator(typingIndicator: TypingIndicator, isShow: Boolean) {
        if (typingIndicator.receiverType.equals(
                CometChatConstants.RECEIVER_TYPE_USER,
                ignoreCase = true
            )
        ) {
            Log.e(TAG, "onTypingStarted: $typingIndicator")
            if (id != null && id.equals(
                    typingIndicator.sender.uid,
                    ignoreCase = true
                )
            ) typingIndicator(typingIndicator, isShow)
        } else {
            if (id != null && id.equals(
                    typingIndicator.receiverId,
                    ignoreCase = true
                )
            ) typingIndicator(typingIndicator, isShow)
        }
    }

    private fun typingIndicator(typingIndicator: TypingIndicator, show: Boolean) {
        isTyping = show
        if (::mainChatAdapter.isInitialized) {
            binding.toolbar.apply {
                if (show) {
                    if (typingIndicator.receiverType == CometChatConstants.RECEIVER_TYPE_USER) {
                        CometChatFeatureRestriction.isTypingIndicatorsEnabled(object :
                            CometChatFeatureRestriction.OnSuccessListener {
                            override fun onSuccess(p0: Boolean) {
                                if (p0) {
                                    tvLastActive.apply {
                                        if (!isHasBlockedByMe || !isBlockedByMe) {
                                            visibility = VISIBLE
                                            text = context.getString(R.string.is_typing)
                                        }
                                    }
                                    tvOnline.visibility = GONE
                                } else {
                                    setUserOnlineStatus()
                                }
                            }

                        })
                    } else {
                        CometChatFeatureRestriction.isTypingIndicatorsEnabled(object :
                            CometChatFeatureRestriction.OnSuccessListener {
                            override fun onSuccess(p0: Boolean) {
                                tvOnline.visibility = GONE
                                if (p0) {
                                    tvLastActive.apply {
                                        visibility = VISIBLE
                                        text =
                                            typingIndicator.sender.name + context.getString(R.string.is_typing)
                                    }
                                } else {
                                    tvLastActive.text = memberNames
                                }
                            }

                        })
                    }
                } else {
                    if (typingIndicator.receiverType == CometChatConstants.RECEIVER_TYPE_USER) {
                        if (typingIndicator.metadata == null) {
                            CometChatFeatureRestriction.isUserPresenceEnabled(object :
                                CometChatFeatureRestriction.OnSuccessListener {
                                override fun onSuccess(p0: Boolean) {
                                    setUserOnlineStatus()
                                }
                            })
                        }
                    } else {
                        tvLastActive.apply {
                            visibility = VISIBLE
                            text = memberNames
                        }
                    }
                }
            }
        }
    }

    private fun removeUserListener() {
        CometChat.removeUserListener(TAG)
    }

    private fun removeGroupListener() {
        CometChat.removeGroupListener(TAG)
    }

    private fun fetchMessage() {
        toggleLoader(true)
        if (messagesRequest == null) {
            if (type != null) {
                if (type == CometChatConstants.RECEIVER_TYPE_USER) {
                    //TODO set hideDeletedMessages = true if needed
                    messagesRequest = MessagesRequest.MessagesRequestBuilder()
                        .setLimit(LIMIT).setUID(id)
                        .hideReplies(true)
                        .hideDeletedMessages(false)
                        .setTypes(Constants.CometChatConstant.MessageRequest.messageTypesForUser)
                        .setCategories(Constants.CometChatConstant.MessageRequest.messageCategoriesForUser)
                        .build()
                } else {
                    messagesRequest =
                        if (!CometChatFeatureRestriction.isHideDeletedMessagesEnabled()) {
                            MessagesRequest.MessagesRequestBuilder()
                                .setLimit(LIMIT).setGUID(id)
                                .hideReplies(true)
                                .hideDeletedMessages(false)
                                .setTypes(Constants.CometChatConstant.MessageRequest.messageTypesForGroup)
                                .setCategories(Constants.CometChatConstant.MessageRequest.messageCategoriesForGroup)
                                .build()
                        } else {
                            //TODO set hideDeletedMessages = true if needed
                            MessagesRequest.MessagesRequestBuilder()
                                .setLimit(LIMIT).setGUID(id)
                                .hideReplies(true)
                                .hideDeletedMessages(false)
                                .hideMessagesFromBlockedUsers(true)
                                .setTypes(Constants.CometChatConstant.MessageRequest.messageTypesForGroup)
                                .setCategories(Constants.CometChatConstant.MessageRequest.messageCategoriesForGroup)
                                .build()
                        }
                }
            }
        }
        messagesRequest?.fetchPrevious(object : CometChat.CallbackListener<List<BaseMessage>>() {
            override fun onSuccess(baseMessages: List<BaseMessage>) {
                toggleLoader(false)
                for (i in baseMessages) {
                    Log.d(TAG, "onSuccess: basemsgtype " + i.type)
                }
                isInProgress = false
                initMessageAdapter(baseMessages)
                if (baseMessages.isNotEmpty()) {
                    val baseMessage = baseMessages[baseMessages.size - 1]
                    if (!isBlockedByMe && !isHasBlockedByMe) {
                        CometChat.markAsRead(baseMessage)
                    }
                }
                if (baseMessages.isEmpty()) {
                    isNoMoreMessages = true
                }
            }

            override fun onError(e: CometChatException) {
                toggleLoader(false)
                Log.d(TAG, "onError: " + e.message)
            }
        })
    }

    private fun initMessageAdapter(messageList: List<BaseMessage>) {
        binding.apply {
            if (::mainChatAdapter.isInitialized && recyclerChatMessages.adapter != null) {
                mainChatAdapter.updateList(messageList)
            } else {
                mainChatAdapter = MainChatAdapter(
                    this@MainCometChatActivity,
                    messageList as ArrayList<BaseMessage>,
                    membersList, this@MainCometChatActivity
                )
                recyclerChatMessages.adapter = mainChatAdapter
                mainChatAdapter.let {
                    stickyHeaderDecoration = StickyHeaderDecoration(it)
                }
                if (recyclerChatMessages.adapter != null && ::mainChatAdapter.isInitialized) {
                    stickyHeaderDecoration?.let { recyclerChatMessages.addItemDecoration(it, 0) }
                }
                scrollToBottom()
                mainChatAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun sendMessage(message: String) {
        val textMessage: TextMessage =
            if (type.equals(CometChatConstants.RECEIVER_TYPE_USER, ignoreCase = true)) TextMessage(
                id,
                message,
                CometChatConstants.RECEIVER_TYPE_USER
            ) else TextMessage(id, message, CometChatConstants.RECEIVER_TYPE_GROUP)
        sendTypingIndicator(true)
        textMessage.category = CometChatConstants.CATEGORY_MESSAGE
        textMessage.sender = loggedInUser
        textMessage.muid = System.currentTimeMillis().toString()
        val jsonObject = JSONObject()
        jsonObject.put(Constants.IsImportant, isImpMsg)
        textMessage.metadata = jsonObject
        if (mainChatAdapter != null) {
            MediaUtilsCometChat.playSendSound(this, R.raw.outgoing_message)
            mainChatAdapter.addMessage(textMessage)
            scrollToBottom()
        }
        CometChat.sendMessage(textMessage, object : CometChat.CallbackListener<TextMessage?>() {
            override fun onSuccess(textMessage: TextMessage?) {
                Log.e(TAG, "onSuccess: " + textMessage.toString())
                if (mainChatAdapter != null) {
                    mainChatAdapter.updateSentMessage(textMessage)
                }
            }

            override fun onError(e: CometChatException) {
                textMessage.sentAt = -1
                if (e.code.equals("ERROR_INTERNET_UNAVAILABLE", ignoreCase = true)) {
                    showToast(getString(R.string.please_check_your_internet_connection))
                } else if (!e.code.equals("ERR_BLOCKED_BY_EXTENSION", ignoreCase = true)) {
                    if (mainChatAdapter == null) {
                        Log.e(TAG, "onError: MessageAdapter is null")
                    } else {
                        textMessage.sentAt = -1
                        mainChatAdapter.updateSentMessage(textMessage)
                    }
                } else if (mainChatAdapter != null) {
                    mainChatAdapter.removeMessage(textMessage)
                }
            }
        })
    }

    private fun sendMediaMessage(file: File, filetype: String) {
        val progressDialog: ProgressDialog =
            ProgressDialog.show(this, "", "Sending Media Message")
        val mediaMessage: MediaMessage =
            if (type.equals(CometChatConstants.RECEIVER_TYPE_USER, ignoreCase = true)) MediaMessage(
                id,
                file,
                filetype,
                CometChatConstants.RECEIVER_TYPE_USER
            ) else MediaMessage(id, file, filetype, CometChatConstants.RECEIVER_TYPE_GROUP)
        val jsonObject = JSONObject()
        try {
            jsonObject.put("path", file.absolutePath)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        mediaMessage.metadata = jsonObject
        mediaMessage.sender = loggedInUser
        mediaMessage.muid = System.currentTimeMillis().toString()
        mediaMessage.category = CometChatConstants.CATEGORY_MESSAGE
        if (mainChatAdapter != null) {
            mainChatAdapter.addMessage(mediaMessage)
            scrollToBottom()
        }
        sendMediaMessage(mediaMessage, object : CometChat.CallbackListener<MediaMessage>() {
            override fun onSuccess(mediaMessage: MediaMessage) {
                isUploading = false
                progressDialog.dismiss()
                val path = file.absolutePath
                if (file.exists()) {
                    if (file.delete()) {
                        println("file Deleted :$path")
                    } else {
                        println("file not Deleted :$path")
                    }
                }
                Log.d(TAG, "sendMediaMessage onSuccess: $mediaMessage")
                mainChatAdapter.updateSentMessage(mediaMessage)
            }

            override fun onError(e: CometChatException) {
                isUploading = false
                progressDialog.dismiss()
                showToast(e.localizedMessage.toString())
            }
        })
    }

    private fun sendCustomMessage(customType: String, customData: JSONObject) {
        val progressDialog = ProgressDialog.show(this, "", getString(R.string.sending))
        val customMessage: CustomMessage =
            if (type.equals(
                    CometChatConstants.RECEIVER_TYPE_USER,
                    ignoreCase = true
                )
            ) CustomMessage(
                id,
                CometChatConstants.RECEIVER_TYPE_USER,
                customType,
                customData
            ) else CustomMessage(id, CometChatConstants.RECEIVER_TYPE_GROUP, customType, customData)
        val incrementCountObject = JSONObject()
        val pushNotificationObject = JSONObject()
        if (customType == Constants.CometChatConstant.LOCATION) {
            pushNotificationObject.put(
                "pushNotification",
                loggedInUser.name + " has shared his location"
            )
            customMessage.metadata = pushNotificationObject
        }
        if (customType == Constants.CometChatConstant.MEETING) {
            pushNotificationObject.put(
                "pushNotification",
                loggedInUser.name + " has started video call"
            )
            customMessage.metadata = pushNotificationObject
        }
        if (customType == Constants.CometChatConstant.STICKERS || customType == Constants.CometChatConstant.LOCATION) {
            incrementCountObject.put("incrementUnreadCount", true)
            customMessage.metadata = incrementCountObject
        }
        CometChat.sendCustomMessage(
            customMessage,
            object : CometChat.CallbackListener<CustomMessage>() {
                override fun onSuccess(customMessage: CustomMessage) {
                    progressDialog.dismiss()
                    if (mainChatAdapter != null) {
                        mainChatAdapter.addMessage(customMessage)
                        scrollToBottom()
                        if (customMessage.customData.has("sessionID"))
                            CometChatUtils.startVideoCallIntent(
                                this@MainCometChatActivity,
                                customMessage.customData.getString("sessionID")
                            )
                    }
                }

                override fun onError(e: CometChatException) {
                    progressDialog.dismiss()
                    showToast(e.localizedMessage.toString())
                }
            })
    }


    private fun sendTypingIndicator(isEnd: Boolean) {
        if (!isBlockedByMe || !isHasBlockedByMe) {
            if (isEnd) {
                if (type == CometChatConstants.RECEIVER_TYPE_USER) {
                    CometChat.endTyping(TypingIndicator(id, CometChatConstants.RECEIVER_TYPE_USER))
                } else {
                    CometChat.endTyping(TypingIndicator(id, CometChatConstants.RECEIVER_TYPE_GROUP))
                }
            } else {
                if (type == CometChatConstants.RECEIVER_TYPE_USER) {
                    CometChat.startTyping(
                        TypingIndicator(
                            id,
                            CometChatConstants.RECEIVER_TYPE_USER
                        )
                    )
                } else {
                    CometChat.startTyping(
                        TypingIndicator(
                            id,
                            CometChatConstants.RECEIVER_TYPE_GROUP
                        )
                    )
                }
            }
        }
    }

    private fun scrollToBottom() {
        binding.apply {
            if (mainChatAdapter != null && mainChatAdapter.itemCount > 0) {
                recyclerChatMessages.scrollToPosition(mainChatAdapter.itemCount - 1)
            }
        }
    }

    private fun addGroupListener() {
        binding.toolbar.apply {
            CometChat.addGroupListener(TAG, object : CometChat.GroupListener() {
                override fun onGroupMemberJoined(
                    action: Action,
                    joinedUser: User,
                    joinedGroup: Group
                ) {
                    super.onGroupMemberJoined(action, joinedUser, joinedGroup)
                    if (joinedGroup.guid == id) tvLastActive.text =
                        memberNames + "," + joinedUser.name
                    onMessageReceived(action)
                }

                override fun onGroupMemberLeft(action: Action, leftUser: User, leftGroup: Group) {
                    super.onGroupMemberLeft(action, leftUser, leftGroup)
                    Log.d(TAG, "onGroupMemberLeft: " + leftUser.name)
                    if (leftGroup.guid == id) {
                        if (memberNames != null) tvLastActive.text =
                            memberNames?.replace("," + leftUser.name, "")
                    }
                    onMessageReceived(action)
                }

                override fun onGroupMemberKicked(
                    action: Action,
                    kickedUser: User,
                    kickedBy: User,
                    kickedFrom: Group
                ) {
                    super.onGroupMemberKicked(action, kickedUser, kickedBy, kickedFrom)
                    Log.d(TAG, "onGroupMemberKicked: " + kickedUser.name)
                    if (kickedUser.uid == CometChat.getLoggedInUser().uid) {
                        if (this@MainCometChatActivity != null) this@MainCometChatActivity.finish()
                    }

                    if (kickedFrom.guid == id) tvLastActive.text =
                        memberNames?.replace("," + kickedUser.name, "")
                    onMessageReceived(action)
                }

                override fun onGroupMemberBanned(
                    action: Action,
                    bannedUser: User,
                    bannedBy: User,
                    bannedFrom: Group
                ) {
                    if (bannedUser.uid == CometChat.getLoggedInUser().uid) {
                        if (this@MainCometChatActivity != null) {
                            this@MainCometChatActivity.onBackPressed()
                            Toast.makeText(
                                this@MainCometChatActivity,
                                "You have been banned",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    onMessageReceived(action)
                }

                override fun onGroupMemberUnbanned(
                    action: Action,
                    unbannedUser: User,
                    unbannedBy: User,
                    unbannedFrom: Group
                ) {
                    onMessageReceived(action)
                }

                override fun onGroupMemberScopeChanged(
                    action: Action,
                    updatedBy: User,
                    updatedUser: User,
                    scopeChangedTo: String,
                    scopeChangedFrom: String,
                    group: Group
                ) {
                    onMessageReceived(action)
                }

                override fun onMemberAddedToGroup(
                    action: Action,
                    addedby: User,
                    userAdded: User,
                    addedTo: Group
                ) {
                    if (addedTo.guid == id) tvLastActive.text =
                        memberNames + "," + userAdded.name
                    onMessageReceived(action)
                }
            })
        }
    }

    private fun onMessageReceived(message: BaseMessage) {
        MediaUtilsCometChat.playSendSound(this, R.raw.incoming_message)
        if (message.receiverType == CometChatConstants.RECEIVER_TYPE_USER) {
            if (id != null && id.equals(message.sender.uid, ignoreCase = true)) {
                setMessage(message)
            } else if (id != null && id.equals(
                    message.receiverUid,
                    ignoreCase = true
                ) && message.sender.uid.equals(CometChat.getLoggedInUser().uid, ignoreCase = true)
            ) {
                setMessage(message)
            }
        } else {
            if (id != null && id.equals(message.receiverUid, ignoreCase = true)) {
                setMessage(message)
            }
        }
    }

    private fun setMessage(message: BaseMessage) {
        binding.apply {
            if (message.parentMessageId == 0) {
                if (mainChatAdapter != null) {
                    mainChatAdapter.addMessage(message)
                    checkSmartReply(message)
                    if (!isBlockedByMe && !isHasBlockedByMe) {
                        CometChat.markAsRead(message)
                    }
                    if (mainChatAdapter.itemCount - 1 - (recyclerChatMessages.layoutManager as LinearLayoutManager?)!!.findLastVisibleItemPosition() < 5) scrollToBottom()
                } else {
                    msgList.add(message)
                    initMessageAdapter(msgList)
                }
            }
        }
    }

    private fun updateMessage(message: BaseMessage) {
        mainChatAdapter.setUpdatedMessage(message)
    }

    private fun checkSmartReply(lastMessage: BaseMessage?) {
        if (lastMessage != null && lastMessage.sender.uid != loggedInUser.uid) {
            if (lastMessage.metadata != null) {
                getSmartReplyList(lastMessage)
            }
        }
    }

    private fun getSmartReplyList(baseMessage: BaseMessage) {
        val extensionList = Extensions.extensionCheck(baseMessage)
        if (extensionList != null && extensionList.containsKey("smartReply")) {
//            rvSmartReply!!.visibility = VISIBLE
            val replyObject = extensionList["smartReply"]
            val replyList: MutableList<String> = ArrayList()
            try {
                replyList.add(replyObject!!.getString("reply_positive"))
                replyList.add(replyObject.getString("reply_neutral"))
                replyList.add(replyObject.getString("reply_negative"))
            } catch (e: Exception) {
                Log.e(TAG, "onSuccess: " + e.message)
            }
//            setSmartReplyAdapter(replyList)
        } else {
//            rvSmartReply?.visibility = GONE
        }
    }

    private fun addUserListener() {
        binding.apply {
            if (!isGroup) {
                CometChat.addUserListener(TAG, object : CometChat.UserListener() {
                    override fun onUserOnline(user: User?) {
                        onlineStatus = user?.status
                        setUserOnlineStatus()
                    }

                    override fun onUserOffline(user: User?) {
                        onlineStatus = user?.status
                        setUserOnlineStatus()
                    }
                })
            }
        }
    }

    private val group: Unit
        get() {
            CometChat.getGroup(id, object : CometChat.CallbackListener<Group>() {
                override fun onSuccess(group: Group) {
                    binding.toolbar.apply {
                        val loginUser =
                            PreferenceUtils(this@MainCometChatActivity).getValue(Constants.PreferenceKeys.role_id)
                                .toIntOrNull()
                        chatName = if (loginUser == LoginUserType.PATIENT.value) {
                            group.name.toString().split("|").first()
                        } else {
                            group.name.toString().split("|").last()
                        }
                        profilePicUrl = group.icon
                        this@MainCometChatActivity.let {
                            ImageUtils.INSTANCE?.loadRemoteImageForGroupProfile(
                                imgChatIcon,
                                profilePicUrl
                            )
                        }

                        tvChatName.text = chatName
                        if (!group.owner.isNullOrEmpty()) {
                            getGroupOwner(group.owner)
                        }
                    }
                }

                override fun onError(e: CometChatException) {
                    Log.e(
                        "Group",
                        "onError: isUserPresenceEnabled " + e.localizedMessage
                    )
                }
            })
        }

    private fun getGroupOwner(ownerId: String) {
        CometChat.getUser(ownerId, object : CometChat.CallbackListener<User>() {
            override fun onSuccess(user: User?) {
                binding.apply {
                    groupOwnerId = user?.uid
                }
            }

            override fun onError(p0: CometChatException?) {
                Log.e("", p0?.code.toString())
            }

        })
    }

    private val user: Unit
        get() {
            CometChat.getUser(id, object : CometChat.CallbackListener<User>() {
                override fun onSuccess(user: User) {
                    binding.apply {
                        when {
                            user.isBlockedByMe -> {
                                isBlockedByMe = true
                            }
                            user.isHasBlockedMe -> {
                                isHasBlockedByMe = true
                                isBlockedByMe = false
                            }
                            else -> {
                                isHasBlockedByMe = false
                                isBlockedByMe = false
                                CometChat.isFeatureEnabled(
                                    Constants.CometChatConstant.chat_users_presence_enabled,
                                    object : CometChat.CallbackListener<Boolean>() {
                                        override fun onSuccess(p0: Boolean?) {
                                            if (p0 == true) {
                                                onlineStatus = user.status
                                                setUserOnlineStatus()
                                            }
                                        }

                                        override fun onError(p0: CometChatException?) {
                                            Log.e(
                                                "FeatureRestriction",
                                                "onError: isUserPresenceEnabled " + p0.toString()
                                            )
                                        }
                                    })
                            }
                        }
                        chatName = user.name
                        if (user.metadata != null) {
                            val specialty =
                                user.metadata.get(Constants.CometChatConstant.speciality)
                            val location = user.metadata.get(Constants.CometChatConstant.LOCATION)
                            val experience =
                                user.metadata.get(Constants.CometChatConstant.experience)
                            userDetails = "$specialty - $location - $experience Yrs"
                        }
                        toolbar.tvChatName.text = chatName
                    }
                }

                override fun onError(e: CometChatException) {
                    Toast.makeText(this@MainCometChatActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

    private val member: Unit
        get() {
            val groupMembersRequest =
                GroupMembersRequest.GroupMembersRequestBuilder(id).setLimit(100).build()
            groupMembersRequest.fetchNext(object :
                CometChat.CallbackListener<List<GroupMember>?>() {
                override fun onSuccess(list: List<GroupMember>?) {
                    binding.toolbar.apply {
                        membersList.clear()
                        var s = arrayOfNulls<String>(0)
                        if (list != null && list.isNotEmpty()) {
                            s = arrayOfNulls(list.size)
                            for (j in list.indices) {
                                s[j] = list[j].name
                            }
                        }

                        memberNames = s.joinToString(", ")
                        tvLastActive.apply {
                            text = memberNames
                            visibility = VISIBLE
                        }
                        tvOnline.visibility = GONE

                        for ((position, i) in list?.indices!!.withIndex()) {

                            val amperSandResult =
                                AppUtils.INSTANCE?.CHAT_USER_COLOR?.get(position % 10)
                            val newAmperSandResult = amperSandResult?.rem(10)
                            val color =
                                if (position < AppUtils.INSTANCE?.CHAT_USER_COLOR?.size!!) AppUtils.INSTANCE?.CHAT_USER_COLOR?.get(
                                    position
                                )
                                else if (amperSandResult!! < AppUtils.INSTANCE?.CHAT_USER_COLOR?.size!!) AppUtils.INSTANCE?.CHAT_USER_COLOR!![amperSandResult]
                                else {
                                    if (newAmperSandResult!! < 0) AppUtils.INSTANCE?.CHAT_USER_COLOR!![0] else AppUtils.INSTANCE?.CHAT_USER_COLOR!![newAmperSandResult!!]
                                }
                            membersList.add(MembersWithColor(list[i].uid, color))
                        }
                    }
                }

                override fun onError(e: CometChatException) {
                    Log.d("ERROR", "Group Member list fetching failed with exception: " + e.message)
                    Toast.makeText(this@MainCometChatActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

    private fun getIntentData() {
        binding.apply {
            intent.extras?.run {
                val name = getString(Constants.CometChatConstant.NAME)
                loginUserType =
                    PreferenceUtils(this@MainCometChatActivity).getValue(Constants.PreferenceKeys.role_id)
                        .toIntOrNull()?:0
                chatName = if (loginUserType == LoginUserType.PATIENT.value) {
                    name.toString().split("|").first()
                } else {
                    name.toString().split("|").last()
                }
                isFromMyTeam = getBoolean(Constants.IS_FROM_MY_TEAM,false)
                profilePicUrl = getString(Constants.CometChatConstant.AVATAR)
                type = getString(Constants.CometChatConstant.TYPE).toString()
                if (type == CometChatConstants.RECEIVER_TYPE_USER) {
                    isGroup = false
                    id = getString(Constants.CometChatConstant.UID).toString()
                    onlineStatus = getString(Constants.CometChatConstant.STATUS)
                } else {
                    isGroup = true
                    groupType = getString(Constants.CometChatConstant.GROUP_TYPE)
                    groupPassword = getString(Constants.CometChatConstant.GROUP_PASSWORD)
                    id = getString(Constants.CometChatConstant.GUID).toString()
                    memberCount = getInt(Constants.CometChatConstant.MEMBER_COUNT)
                }
            }
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            setSupportActionBar(toolbarChat)
            toolbarChat.overflowIcon =
                ContextCompat.getDrawable(this@MainCometChatActivity, R.drawable.ic_more)
            tvChatName.text = chatName
            toolbarIcon1.setImageResource(R.drawable.img_audio_call)
            toolbarIcon2.setImageResource(R.drawable.img_video_call)
            if (isGroup) {
                ImageUtils.INSTANCE?.loadRemoteImageForGroupProfile(imgChatIcon, profilePicUrl)
                tvLastActive.apply {
                    visibility = VISIBLE
                    text = memberNames
                }
            } else {
                if (!isBlockedByMe || !isHasBlockedByMe) {
                    ImageUtils.INSTANCE?.loadRemoteImageForProfile(imgChatIcon, profilePicUrl)
                    setUserOnlineStatus()
                } else {

                }
            }
        }
    }

    private fun setUserOnlineStatus() {
        binding.toolbar.apply {
            if (onlineStatus == CometChatConstants.USER_STATUS_ONLINE) {
                if (!isTyping) {
                    tvLastActive.visibility = GONE
                    tvOnline.apply {
                        if (!isBlockedByMe || !isHasBlockedByMe) {
                            visibility = VISIBLE
                            text = getString(R.string.online)
                        } else {
                            visibility = GONE
                        }
                    }
                }
            } else {
                tvOnline.visibility = GONE
                tvLastActive.apply {
                    if (!isHasBlockedByMe || !isBlockedByMe) {
                        visibility = VISIBLE
                        text = getString(R.string.offline)
                    } else {
                        visibility = GONE
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (isGroup) {
            menuInflater.inflate(R.menu.chat_menu, menu)
        } else {
            menuInflater.inflate(R.menu.chat_menu_personal, menu)
//            if (isBlockedByMe) {
//                menu.findItem(R.id.menu_block)?.title = getString(R.string.unblock)
//            } else {
//                menu.findItem(R.id.menu_block)?.title = getString(R.string.block)
//            }
        }
        this.menu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
//            R.id.menu_group_info -> {
//
//                true
//            }
//            R.id.menu_view_contact -> {
//                DialogUtils.showUserProfileDialog(
//                    this,
//                    chatName.toString(),
//                    profilePicUrl.toString(),
//                    userDetails,
//                    isBlockedByMe,
//                    this
//                )
//                true
//            }
//            R.id.menu_block -> {
//                if (isBlockedByMe) {
//                    callUnblockUserApi()
//                } else {
//
//                }
//                true
//            }
            R.id.menu_delete_group,R.id.menu_delete_chat_and_exit -> {
                DialogUtils.showConfirmationDialog(
                    this,
                    this,
                    title = getString(R.string.menu_clear_chat),
                    msg = getString(R.string.are_you_sure_you_want_to_clear_chat),
                    btnText = getString(R.string.yes),
                    icon = R.drawable.delete,
                    isDeleteGroup = true
                )
                true
            }
//            R.id.menu_delete_chat_and_exit -> {
//                DialogUtils.showConfirmationDialog(
//                    this,
//                    this,
//                    title = getString(R.string.delete_and_exit),
//                    msg = getString(R.string.are_you_sure_you_want_to_delete_chat),
//                    btnText = getString(R.string.yes),
//                    icon = R.drawable.delete,
//                    isDeleteGroup = true
//                )
//                true
//            }
//            R.id.menu_exit_group -> {
//                DialogUtils.showConfirmationDialog(
//                    this,
//                    this,
//                    title = getString(R.string.menu_exit_group),
//                    msg = getString(R.string.are_you_sure_you_want_to_exit_group),
//                    btnText = getString(R.string.yes),
//                    icon = R.drawable.ic_exit_group,
//                    isExitGroup = true
//                )
//                true
//            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun transferOwnerShip() {
        toggleLoader(true)
        CometChat.transferGroupOwnership(
            id,
            membersList[0].uid,
            object : CometChat.CallbackListener<String>() {
                override fun onSuccess(p0: String?) {
                    toggleLoader(false)
                    leaveGroup()
                }

                override fun onError(e: CometChatException?) {
                    toggleLoader(false)
                    showToast(e?.localizedMessage.toString())
                }
            })
    }

    private fun deleteGroup() {
        binding.apply {
            if (CometChatFeatureRestriction.isGroupDeletionEnabled()) {
                toggleLoader(true)
                CometChat.deleteGroup((id), object : CometChat.CallbackListener<String?>() {
                    override fun onSuccess(s: String?) {
                        toggleLoader(false)
                        startHomeActivityFromGroup()
                    }

                    override fun onError(e: CometChatException) {
                        toggleLoader(false)
                        showToast(e.message.toString())
                    }
                })
            }
        }
    }

    private fun makeAdmin() {
        binding.apply {
            toggleLoader(true)
            CometChat.updateGroupMemberScope(
                membersList[0].uid,
                id,
                CometChatConstants.SCOPE_ADMIN,
                object : CometChat.CallbackListener<String>() {
                    override fun onSuccess(s: String) {
                        toggleLoader(false)
                        leaveGroup()
                    }

                    override fun onError(e: CometChatException) {
                        toggleLoader(false)
                        showToast(e.localizedMessage.toString())
                    }
                })
        }
    }

    private fun leaveGroup() {
        if (CometChatFeatureRestriction.isJoinLeaveGroupsEnabled()) {
            toggleLoader(true)
            CometChat.leaveGroup((id), object : CometChat.CallbackListener<String?>() {
                override fun onSuccess(s: String?) {
                    toggleLoader(false)
                    startHomeActivityFromGroup()
                }

                override fun onError(e: CometChatException) {
                    toggleLoader(false)
                    showToast(e.message.toString())
                }
            })
        }
    }

    private fun startNewActivity() {
        binding.apply {
            val intent = Intent(this@MainCometChatActivity, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun callDeleteChatApi() {
        binding.apply {
            toggleLoader(true)
            CometChat.deleteConversation(
                id,
                type,
                object : CometChat.CallbackListener<String>() {
                    override fun onSuccess(p0: String?) {
                        toggleLoader(false)
                        finish()
                    }

                    override fun onError(e: CometChatException?) {
                        toggleLoader(false)
                        if (e?.code == "ERR_CONVERSATION_NOT_ACCESSIBLE") {
                            showToast(getString(R.string.no_chats_available_for_delete))
                        } else {
                            showToast(e?.message.toString())
                        }
                    }

                })
        }
    }

    private fun openMsgActionBottomSheet(baseMessage: BaseMessage, msgType: Int) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_message_action, null)
        bottomSheetMessageActionBinding = BottomSheetMessageActionBinding.bind(view)
        bottomSheetMessageActionBinding.apply {
            btnClose.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            hideEditMsgLayout()
            hideReplyMsgLayout()
            when (msgType) {
                LEFT_TEXT_MESSAGE -> {
                    tvReplyMsg.visibility = VISIBLE
                    tvCopyMsg.visibility = VISIBLE
                    tvForwardMsg.visibility = VISIBLE
//                    tvShareOnEfax.visibility = VISIBLE
                    tvShareMsg.visibility = VISIBLE
                    if (isGroup) {
                        // todo add send privately,reply privately
                    }
                }
                RIGHT_TEXT_MESSAGE -> {
                    tvReplyMsg.visibility = VISIBLE
                    tvCopyMsg.visibility = VISIBLE
                    tvForwardMsg.visibility = VISIBLE
//                    tvShareOnEfax.visibility = VISIBLE
                    tvShareMsg.visibility = VISIBLE
                    tvEditMsg.visibility = VISIBLE
                    tvDelete.visibility = VISIBLE
                    if (isGroup) {
                        // todo visible
                        tvInfo.visibility = GONE
                    }
                }
                LEFT_REPLY_TEXT_MESSAGE -> {
                    tvReplyMsg.visibility = VISIBLE
                    tvCopyMsg.visibility = VISIBLE
                    tvForwardMsg.visibility = VISIBLE
//                    tvShareOnEfax.visibility = VISIBLE
                    tvShareMsg.visibility = VISIBLE
                    if (isGroup) {
                        // todo add send privately,reply privately
                    }
                }

                RIGHT_REPLY_TEXT_MESSAGE -> {
                    tvReplyMsg.visibility = VISIBLE
                    tvCopyMsg.visibility = VISIBLE
                    tvForwardMsg.visibility = VISIBLE
//                    tvShareOnEfax.visibility = VISIBLE
                    tvShareMsg.visibility = VISIBLE
                    tvDelete.visibility = VISIBLE
                    if (isGroup) {
                        // todo visible
                        tvInfo.visibility = GONE
                    }
                }

                LEFT_IMAGE_MESSAGE -> {
                    tvReplyMsg.visibility = VISIBLE
                    tvForwardMsg.visibility = VISIBLE
//                    tvShareOnEfax.visibility = VISIBLE
                    tvShareMsg.visibility = VISIBLE
                    if (isGroup) {
                        // todo add send privately,reply privately
                    }
                }
                RIGHT_IMAGE_MESSAGE -> {
                    tvReplyMsg.visibility = VISIBLE
                    tvForwardMsg.visibility = VISIBLE
//                    tvShareOnEfax.visibility = VISIBLE
                    tvShareMsg.visibility = VISIBLE
                    tvDelete.visibility = VISIBLE
                    if (isGroup) {
                        // todo visible
                        tvInfo.visibility = GONE
                    }
                }
                LEFT_VIDEO_MESSAGE, LEFT_AUDIO_MESSAGE -> {
                    tvReplyMsg.visibility = VISIBLE
                    tvForwardMsg.visibility = VISIBLE
                    tvShareMsg.visibility = VISIBLE
                    if (isGroup) {
                        // todo add send privately,reply privately
                    }
                }
                RIGHT_VIDEO_MESSAGE, RIGHT_AUDIO_MESSAGE -> {
                    tvReplyMsg.visibility = VISIBLE
                    tvForwardMsg.visibility = VISIBLE
                    tvShareMsg.visibility = VISIBLE
                    tvDelete.visibility = VISIBLE
                    if (isGroup) {
                        // todo visible
                        tvInfo.visibility = GONE
                    }
                }
                LEFT_FILE_MESSAGE -> {
                    tvReplyMsg.visibility = VISIBLE
                    tvForwardMsg.visibility = VISIBLE
//                    tvShareOnEfax.visibility = VISIBLE
                    tvShareMsg.visibility = VISIBLE
                    if (isGroup) {
                        // todo add send privately,reply privately
                    }
                }
                RIGHT_FILE_MESSAGE -> {
                    tvReplyMsg.visibility = VISIBLE
                    tvForwardMsg.visibility = VISIBLE
//                    tvShareOnEfax.visibility = VISIBLE
                    tvShareMsg.visibility = VISIBLE
                    tvDelete.visibility = VISIBLE
                    if (isGroup) {
                        // todo visible
                        tvInfo.visibility = GONE
                    }
                }
                LEFT_LOCATION_CUSTOM_MESSAGE -> {
                    tvReplyMsg.visibility = VISIBLE
                    tvForwardMsg.visibility = VISIBLE
                    if (isGroup) {
                        // todo add send privately,reply privately
                    }
                }
                RIGHT_LOCATION_CUSTOM_MESSAGE -> {
                    tvReplyMsg.visibility = VISIBLE
                    tvForwardMsg.visibility = VISIBLE
                    tvDelete.visibility = VISIBLE
                    if (isGroup) {
                        // todo visible
                        tvInfo.visibility = GONE
                    }
                }
            }

            tvEditMsg.setOnClickListener {
                if (baseMessage != null && baseMessage.type == CometChatConstants.MESSAGE_TYPE_TEXT) {
                    isEdit = true
                    isReply = false
                    hideReplyMsgLayout()
                    showEditMsgLayout()
                    bottomSheetDialog.dismiss()
                }
            }
            tvReplyMsg.setOnClickListener {
                isEdit = false
                isReply = true
                hideEditMsgLayout()
                showReplyMsgLayout()
                bottomSheetDialog.dismiss()
            }
            tvCopyMsg.setOnClickListener {
                val message = (baseMessage as TextMessage).text

                val clipboardManager =
                    getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("MessageAdapter", message)
                clipboardManager.setPrimaryClip(clipData)
                Toast.makeText(
                    this@MainCometChatActivity,
                    resources.getString(R.string.text_copied),
                    Toast.LENGTH_LONG
                ).show()
                bottomSheetDialog.dismiss()
            }
            tvDelete.setOnClickListener {
                if (!isHasBlockedByMe && !isBlockedByMe) {
                    deleteMessage(baseMessage)
                    bottomSheetDialog.dismiss()
                }
            }
            tvShareOnEfax.setOnClickListener {
                bottomSheetDialog.dismiss()
                if (baseMessage.type == CometChatConstants.MESSAGE_TYPE_IMAGE ||
                    baseMessage.type == CometChatConstants.MESSAGE_TYPE_TEXT ||
                    baseMessage.type == CometChatConstants.MESSAGE_TYPE_FILE
                ) {
//                    if (!isHasBlockedByMe) {
//                        DialogUtils.showEFaxDialog(
//                            this@MainCometChatActivity,
//                            this@MainCometChatActivity,
//                            baseMessage
//                        )
//                    }
                }
            }
            tvShareMsg.setOnClickListener {
                bottomSheetDialog.dismiss()
                shareMessage()
            }
            tvForwardMsg.setOnClickListener {
                bottomSheetDialog.dismiss()
                forwardMessage()
            }
        }
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.setCancelable(true)
        if (!isBlockedByMe && !isHasBlockedByMe) {
            bottomSheetDialog.show()
        }
    }

    private fun forwardMessage() {
        binding.apply {
            val intent = Intent(this@MainCometChatActivity, ForwardMessageActivity::class.java)
            val b = Bundle()
            if (baseMessage?.category == CometChatConstants.CATEGORY_MESSAGE) {
                b.putString(
                    Constants.CometChatConstant.MESSAGE_CATEGORY,
                    CometChatConstants.CATEGORY_MESSAGE
                )
                if (baseMessage?.type == CometChatConstants.MESSAGE_TYPE_TEXT) {
                    b.putString(
                        CometChatConstants.MESSAGE_TYPE_TEXT,
                        (baseMessage as TextMessage).text
                    )
                    b.putString(
                        Constants.CometChatConstant.TYPE,
                        CometChatConstants.MESSAGE_TYPE_TEXT
                    )
                } else if (baseMessage?.type == CometChatConstants.MESSAGE_TYPE_IMAGE ||
                    baseMessage?.type == CometChatConstants.MESSAGE_TYPE_AUDIO ||
                    baseMessage?.type == CometChatConstants.MESSAGE_TYPE_VIDEO ||
                    baseMessage?.type == CometChatConstants.MESSAGE_TYPE_FILE
                ) {
                    b.putString(
                        Constants.CometChatConstant.FILE_NAME,
                        (baseMessage as MediaMessage).attachment.fileName
                    )
                    b.putString(
                        Constants.CometChatConstant.FILE_URL,
                        (baseMessage as MediaMessage).attachment.fileUrl
                    )
                    b.putString(
                        Constants.CometChatConstant.FILE_MIME_TYPE,
                        (baseMessage as MediaMessage).attachment.fileMimeType
                    )
                    b.putString(
                        Constants.CometChatConstant.FILE_EXTENSION,
                        (baseMessage as MediaMessage).attachment.fileExtension
                    )
                    b.putInt(
                        Constants.CometChatConstant.FILE_SIZE,
                        (baseMessage as MediaMessage).attachment.fileSize
                    )
                    b.putString(Constants.CometChatConstant.TYPE, baseMessage?.type)
                }
            } else if (baseMessage?.category == CometChatConstants.CATEGORY_CUSTOM) {
                b.putString(
                    Constants.CometChatConstant.MESSAGE_CATEGORY,
                    CometChatConstants.CATEGORY_CUSTOM
                )
                b.putString(
                    Constants.CometChatConstant.TYPE,
                    Constants.CometChatConstant.LOCATION
                )
                try {
                    b.putDouble(
                        Constants.CometChatConstant.LOCATION_LATITUDE,
                        (baseMessage as CustomMessage).customData.getDouble("latitude")
                    )
                    b.putDouble(
                        Constants.CometChatConstant.LOCATION_LONGITUDE,
                        (baseMessage as CustomMessage).customData.getDouble("longitude")
                    )
                } catch (e: java.lang.Exception) {
                    Log.e(TAG, "startForwardMessageActivityError: " + e.message)
                }
            }
            b.putString(Constants.CometChatConstant.UID, id)
            intent.putExtras(b)
            startActivity(intent)
        }
    }

    override fun onEFaxContinueClick(faxNo: String, baseMessage: BaseMessage) {
//        val progressDialog = ProgressDialog.show(this, "", getString(R.string.sending_e_fax))

        val msgType = baseMessage.type
        var url: String? = null
        var msg: String? = null
        if (msgType == CometChatConstants.MESSAGE_TYPE_TEXT) {
            msg = (baseMessage as TextMessage).text
        } else {
            url = (baseMessage as MediaMessage).attachment.fileUrl
        }
//        if (isNetworkAvailable()) {
//            eFaxViewModel.callSendEFaxFromChatApi(
//                request = SendFaxesRequestModel(
//                    efax_file_url = url,
//                    efax_number = faxNo,
//                    efax_text = msg,
//                    sp_uid = id,
//                    name = chatName,
//                    is_group = isGroup
//                )
//            ).observe(this, {
//                progressDialog.dismiss()
//                showToast(it.message.toString())
//            })
//        } else {
//            showToast(getString(R.string.no_internet_connection_please_try_again_later))
//        }
    }

//    override fun clickOnBlockUnblock() {
//        if (isBlockedByMe) {
//            callUnblockUserApi()
//        } else {
//            DialogUtils.showConfirmationDialog(
//                this,
//                this,
//                title = getString(R.string.are_you_sure),
//                msg = getString(R.string.you_won_t_be_able_to_send_and_receive_messages_from_this_person),
//                btnText = getString(R.string.block),
//                icon = R.drawable.ic_block, isBlock = true
//            )
//        }
//    }

    override fun onBlockUser() {
        callBlockUserApi()
    }

    override fun onDeleteGroup() {
//        callDeleteChatApi()
        callDeleteConversionApi()
    }

    private fun callDeleteConversionApi() {
        binding.apply {
            CometChat.deleteConversation(
                id,
                type,
                object : CometChat.CallbackListener<String>() {
                    override fun onSuccess(p0: String?) {
                        finish()
                    }

                    override fun onError(p0: CometChatException?) {
                        showToast(p0?.message.toString())
                    }

                })
        }
    }

    override fun onExitGroup() {
        if (groupOwnerId == loggedInUser.uid) {
            if (memberCount ?: 0 > 1) {
                transferOwnerShip()
            } else {
                deleteGroup()
            }
        } else {
            leaveGroup()
        }
    }

    private fun shareMessage() {
        if (baseMessage != null && baseMessage?.type == CometChatConstants.MESSAGE_TYPE_TEXT) {
            val shareIntent = Intent()
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_TITLE, resources.getString(R.string.app_name))
            shareIntent.putExtra(Intent.EXTRA_TEXT, (baseMessage as TextMessage).text)
            shareIntent.type = "text/plain"
            val intent =
                Intent.createChooser(shareIntent, resources.getString(R.string.share_message))
            startActivity(intent)
            baseMessage = null
        } else if (baseMessage != null && baseMessage?.type == CometChatConstants.MESSAGE_TYPE_IMAGE) {
            val mediaName = (baseMessage as MediaMessage).attachment.fileName
            Glide.with(this).asBitmap()
                .load((baseMessage as MediaMessage).attachment.fileUrl)
                .into(object : SimpleTarget<Bitmap?>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap?>?
                    ) {
                        val path = MediaStore.Images.Media.insertImage(
                            contentResolver,
                            resource,
                            mediaName,
                            null
                        )
                        val shareIntent = Intent()
                        shareIntent.action = Intent.ACTION_SEND
                        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path))
                        shareIntent.type = (baseMessage as MediaMessage).attachment.fileMimeType
                        val intent = Intent.createChooser(
                            shareIntent,
                            resources.getString(R.string.share_message)
                        )
                        startActivity(intent)
                        baseMessage = null
                    }
                })
        } else if (baseMessage is MediaMessage) {
            val mediaMessage = baseMessage as MediaMessage
            if (mediaMessage.attachment != null) {
                val share = Intent(Intent.ACTION_SEND)
                share.type = "text/plain"
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

                share.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.app_name))
                share.putExtra(Intent.EXTRA_TEXT, mediaMessage.attachment.fileUrl)
                startActivity(Intent.createChooser(share, "Share file link"))
                baseMessage = null
            }
        }
    }

    private fun editMessage(baseMessage: BaseMessage, message: String) {
        binding.apply {
            val textMessage: TextMessage = if (baseMessage.receiverType.equals(
                    CometChatConstants.RECEIVER_TYPE_USER,
                    ignoreCase = true
                )
            ) TextMessage(
                baseMessage.receiverUid,
                message,
                CometChatConstants.RECEIVER_TYPE_USER
            ) else TextMessage(
                baseMessage.receiverUid,
                message,
                CometChatConstants.RECEIVER_TYPE_GROUP
            )
            sendTypingIndicator(true)
            textMessage.id = baseMessage.id
            val jsonObject = JSONObject()
            jsonObject.put(Constants.IsImportant, isImpMsg)
            textMessage.metadata = jsonObject
            CometChat.editMessage(textMessage, object : CometChat.CallbackListener<BaseMessage>() {
                override fun onSuccess(message: BaseMessage) {
                    if (mainChatAdapter != null) {
                        Log.e(TAG, "onSuccess: $message")
                        mainChatAdapter.setUpdatedMessage(message)
                        selectedMsgPosition?.let { recyclerChatMessages.scrollToPosition(it) }
                        hideEditMsgLayout()
                    }
                }

                override fun onError(e: CometChatException) {
                    Log.d(TAG, "onError: " + e.message)
                    showToast(e.localizedMessage.toString())
                }
            })
        }
    }

    private fun replyMessage(baseMessage: BaseMessage?, message: String) {
        try {
            val textMessage: TextMessage = if (type.equals(
                    CometChatConstants.RECEIVER_TYPE_USER,
                    ignoreCase = true
                )
            ) TextMessage(id, message, CometChatConstants.RECEIVER_TYPE_USER) else TextMessage(
                id,
                message,
                CometChatConstants.RECEIVER_TYPE_GROUP
            )
            textMessage.category = CometChatConstants.CATEGORY_MESSAGE
            textMessage.sender = loggedInUser
            textMessage.muid = System.currentTimeMillis().toString()
            val jsonObject = JSONObject()
            var replyObject = JSONObject()

            jsonObject.put(Constants.IsImportant, isImpMsg)
            textMessage.metadata = jsonObject

            if (isReplyPrivately)
                jsonObject.put("replyToMessage", baseMessage?.rawMessage)
            else
                jsonObject.put("reply-message", baseMessage?.rawMessage)

            textMessage.metadata = jsonObject
            sendTypingIndicator(true)
            if (mainChatAdapter != null) {
                MediaUtilsCometChat.playSendSound(
                    this,
                    R.raw.outgoing_message
                )
                hideReplyMsgLayout()
                mainChatAdapter.addMessage(textMessage)
                scrollToBottom()
            }
            CometChat.sendMessage(textMessage, object : CometChat.CallbackListener<TextMessage?>() {
                override fun onSuccess(textMessage: TextMessage?) {
                    if (mainChatAdapter != null) {
                        hideReplyMsgLayout()
                        mainChatAdapter.updateSentMessage(textMessage)
                    }
                }

                override fun onError(e: CometChatException) {
                    Log.e(TAG, "onError: " + e.message)
                    textMessage.sentAt = -1
                    if (e.code.equals("ERROR_INTERNET_UNAVAILABLE", ignoreCase = true)) {
                        showToast(
                            getString(R.string.please_check_your_internet_connection)
                        )
                    } else if (!e.code.equals("ERR_BLOCKED_BY_EXTENSION", ignoreCase = true)) {
                        if (mainChatAdapter == null) {
                            Log.e(TAG, "onError: MessageAdapter is null")
                        } else {
                            textMessage.sentAt = -1
                            mainChatAdapter.updateSentMessage(textMessage)
                        }
                    } else if (mainChatAdapter != null) {
                        mainChatAdapter.removeMessage(textMessage)
                    }
                }
            })
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "replyMessage: " + e.message)
        }
    }

    private fun showEditMsgLayout() {
        binding.apply {
            layoutEditMsg.visibility = VISIBLE
            val oldMsg = (baseMessage as TextMessage).text.toString()
            tvEditMsg.text = oldMsg
            etEnterMsg.setText(oldMsg)
            btnAttachMedia.visibility = GONE
        }
    }

    private fun hideEditMsgLayout() {
        binding.apply {
            isEdit = false
            layoutEditMsg.visibility = GONE
            etEnterMsg.setText("")
            etEnterMsg.hint = getString(R.string.write_a_message)
            btnAttachMedia.visibility = VISIBLE
        }
    }

    private fun showReplyMsgLayout() {
        binding.replyLayout.apply {
            binding.layoutReplyMsg.visibility = VISIBLE
            btnReplyClose.visibility = VISIBLE
            binding.btnAttachMedia.visibility = GONE
            if (baseMessage != null) {
                tvReplyMsgTitle.text =
                    if (baseMessage?.sender?.uid == loggedInUser.uid) getString(R.string.you) else baseMessage?.sender?.name
                imgReplyMsg.visibility = VISIBLE
                when (baseMessage?.type) {
                    CometChatConstants.MESSAGE_TYPE_TEXT -> {
                        tvReplyMsg.text = (baseMessage as TextMessage).text
                        imgReplyMsg.visibility = GONE
                    }
                    CometChatConstants.MESSAGE_TYPE_IMAGE -> {
                        tvReplyMsg.text = getString(R.string.message_image)
                        ImageUtils.INSTANCE?.loadRemoteImage(
                            imgReplyMsg,
                            (baseMessage as MediaMessage).attachment.fileUrl
                        )
                    }
                    CometChatConstants.MESSAGE_TYPE_AUDIO -> {
                        val fileSize =
                            AppUtils.INSTANCE?.getFileSize((baseMessage as MediaMessage).attachment.fileSize)
                        tvReplyMsg.text = getString(R.string.message_audio) + ":" + fileSize
                        imgReplyMsg.visibility = GONE
                    }
                    CometChatConstants.MESSAGE_TYPE_VIDEO -> {
                        tvReplyMsg.text = resources.getString(R.string.message_video)
                        ImageUtils.INSTANCE?.loadRemoteImage(
                            imgReplyMsg,
                            (baseMessage as MediaMessage).attachment.fileUrl
                        )
                    }
                    CometChatConstants.MESSAGE_TYPE_FILE -> {
                        val fileSize =
                            AppUtils.INSTANCE?.getFileSize((baseMessage as MediaMessage).attachment.fileSize)
                        tvReplyMsg.text = getString(R.string.message_file) + ":" + fileSize
                        imgReplyMsg.visibility = GONE
                    }
                    Constants.CometChatConstant.LOCATION -> {
                        try {
                            val jsonObject = (baseMessage as CustomMessage).customData
                            val location = AppUtils.INSTANCE?.getAddress(
                                this@MainCometChatActivity, jsonObject.getDouble("latitude"),
                                jsonObject.getDouble("longitude")
                            )
                            tvReplyMsg.text =
                                getString(R.string.custom_message_location) + ":" + location
                            imgReplyMsg.visibility = GONE
                        } catch (e: Exception) {
                            Log.e(TAG, "replyMessageError: " + e.message)
                        }
                    }
                    Constants.CometChatConstant.STICKERS -> {
                        tvReplyMsg.text = resources.getString(R.string.custom_message_sticker)
                        try {
                            imgReplyMsg.visibility = VISIBLE
                            ImageUtils.INSTANCE?.loadRemoteImage(
                                imgReplyMsg,
                                (baseMessage as CustomMessage).customData.getString("url")
                            )
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    private fun hideReplyMsgLayout() {
        binding.apply {
            isReply = false
            layoutReplyMsg.visibility = GONE
            etEnterMsg.setText("")
            etEnterMsg.hint = getString(R.string.write_a_message)
            btnAttachMedia.visibility = VISIBLE
        }
    }

    private fun deleteMessage(baseMessage: BaseMessage?) {
        toggleLoader(true)
        CometChat.deleteMessage(
            baseMessage!!.id,
            object : CometChat.CallbackListener<BaseMessage?>() {
                override fun onSuccess(baseMessage: BaseMessage?) {
                    toggleLoader(false)
                    if (mainChatAdapter != null) baseMessage?.let {
                        mainChatAdapter.setUpdatedMessage(
                            it
                        )
                    }
                }

                override fun onError(e: CometChatException) {
                    toggleLoader(false)
                    Log.d(TAG, "onError: " + e.message)
                    showToast(e.message.toString())
                }
            })
    }

    private fun callBlockUserApi() {
        binding.apply {
            val blockList: ArrayList<String> = ArrayList()
            blockList.clear()
            blockList.add(id)
            toggleLoader(true)
            CometChat.blockUsers(blockList,
                object : CometChat.CallbackListener<HashMap<String?, String?>?>() {
                    override fun onSuccess(p0: HashMap<String?, String?>?) {
                        toggleLoader(false)
                        isBlockedByMe = true
//                        handleBlockedUi()
                        invalidateOptionsMenu()
                    }

                    override fun onError(p0: CometChatException?) {
                        toggleLoader(false)
                        showToast(p0?.localizedMessage.toString())
                    }

                })
        }
    }


    private fun callUnblockUserApi() {
        binding.apply {
            val uids = ArrayList<String?>()
            uids.add(id)
            toggleLoader(true)
            CometChat.unblockUsers(
                uids,
                object : CometChat.CallbackListener<HashMap<String?, String?>?>() {
                    override fun onSuccess(stringStringHashMap: HashMap<String?, String?>?) {
                        toggleLoader(false)
                        isBlockedByMe = false
//                        handleBlockedUi()
                        invalidateOptionsMenu()
                    }

                    override fun onError(e: CometChatException) {
                        toggleLoader(false)
                        showToast(e.localizedMessage.toString())
                    }
                })
        }
    }

    private fun scanDoc() {

//        val f = File(Environment.getExternalStorageDirectory(), "tessdata")
//        if (!f.exists()) {
//            f.mkdirs()
//        }
//        val filePath: String = "/storage/emulated/0/tessdata/eng.traineddata"
//        val file = File(filePath)
//
//        if (!file.exists()) {
//            try {
//                val fileNew = File("/storage/emulated/0/tessdata/", "eng.traineddata")
//                val myInput = context!!.resources.openRawResource(R.raw.eng)
//                val os: OutputStream = FileOutputStream(fileNew)
//                val buffer: ByteArray = ByteArray(1024)
//                var length: Int
//                while (myInput.read(buffer).also { length = it } > 0) {
//                    os.write(buffer, 0, length)
//                }
//                os.flush()
//                os.close()
//                myInput.close()
//
//            } catch (e: IOException) {
//                Log.w("ExternalStorage", "Error writing $file", e)
//            }
//        }
//        val ocr = ScanConfiguration.OcrConfiguration()
//        ocr.languages = listOf("eng")
//        ocr.languagesDirectory = File("/storage/emulated/0/tessdata/")

//        val scanConfiguration = ScanConfiguration().apply {
//            multiPage = true
//            jpegQuality = 30
//            defaultFilter = ScanConfiguration.Filter.NONE
////            ocrConfiguration = ocr
//        }
//        ScanFlow.scanWithConfiguration(this, scanConfiguration)
    }

    private fun toggleLoader(showLoader: Boolean) {
        toggleFadeView(
            binding.root,
            binding.contentLoading.layoutLoading,
            binding.contentLoading.imageLoading,
            showLoader
        )
    }

    private val locationOnOffResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            locationManager =
                getSystemService(LOCATION_SERVICE) as LocationManager
            if (locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true) {
                getLocation()
            } else {
                Toast.makeText(this, getString(R.string.gps_disabled), Toast.LENGTH_SHORT)
                    .show()
            }
        }

    private fun turnOnLocation() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.turn_on_gps))
        builder.setPositiveButton(getString(R.string.on)) { dialog, which ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            locationOnOffResult.launch(intent)
        }.setNegativeButton(getString(R.string.cancel)) { dialog, which -> dialog.dismiss() }
        builder.create()
        builder.show()
    }

    private fun getLocationPermission() {
        binding.apply {
            if (ContextCompat.checkSelfPermission(
                    this@MainCometChatActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    this@MainCometChatActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val isPermissionAsked =
                    preferenceUtils.getValue(
                        Constants.AskedPermission.IS_LOCATION_PERMISSION_ASKED,
                        false
                    )
                if (isPermissionAsked) {
                    //Returns true if user has previously denied permission
                    //Returns false if user has selected dont ask again.
                    val isPermissionDenied = ActivityCompat.shouldShowRequestPermissionRationale(
                        this@MainCometChatActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) ||
                            ActivityCompat.shouldShowRequestPermissionRationale(
                                this@MainCometChatActivity,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                    if (isPermissionDenied) {
                        requestLocationPermission.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    } else {
                        DialogUtils.showPermissionDialog(
                            this@MainCometChatActivity,
                            getString(R.string.grant_location_permission),
                            getString(R.string.allow_permission),
                            getString(R.string.go_to_settings),
                            getString(R.string.deny)
                        )
                    }
                } else {
                    requestLocationPermission.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            } else {
                //User has granted permissions
                initLocation()
                val provider: Boolean? =
                    locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)
                if (provider == false) {
                    turnOnLocation()
                } else {
                    getLocation()
                }
            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        //TODO handle this
//        try {
//            val result = ScanFlow.getScanResultFromActivityResult(data)
//            result.pdfFile?.let { sendMediaMessage(it, CometChatConstants.MESSAGE_TYPE_FILE) }
//
//        } catch (e: Exception) {
//            val error = e.localizedMessage
//            // There was an error during the scan flow. Check the exception for more details.
//        }
//    }

    override fun clickOnTextMsg(position: Int, baseMessage: BaseMessage) {
        this.baseMessage = baseMessage
//        startActivity(
//            Intent(
//                Intent.ACTION_VIEW,
//                Uri.parse((baseMessage as MediaMessage).attachment.fileUrl)
//            )
//        )
    }

    override fun clickOnImageMsg(position: Int, baseMessage: BaseMessage) {
        this.baseMessage = baseMessage
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse((baseMessage as MediaMessage).attachment.fileUrl)
            )
        )
    }

    override fun clickOnVideoMsg(position: Int, baseMessage: BaseMessage) {
        this.baseMessage = baseMessage
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse((baseMessage as MediaMessage).attachment.fileUrl)
            )
        )
    }

    override fun clickOnFileMsg(position: Int, baseMessage: BaseMessage) {
        this.baseMessage = baseMessage
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse((baseMessage as MediaMessage).attachment.fileUrl)
            )
        )
    }

    override fun clickOnMapMsg(position: Int, baseMessage: BaseMessage) {
        this.baseMessage = baseMessage
        val latitude = (baseMessage as CustomMessage).customData.getDouble("latitude")
        val longitude = baseMessage.customData.getDouble("longitude")
        val label = AppUtils.INSTANCE?.getAddress(
            this,
            latitude,
            longitude
        )
        AppUtils.INSTANCE?.openInMap(this, latitude, longitude, label.toString())
    }

    override fun clickOnAudioMsg(position: Int, baseMessage: BaseMessage) {
        this.baseMessage = baseMessage
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse((baseMessage as MediaMessage).attachment.fileUrl)
            )
        )
    }

    override fun clickOnReplyMsg(position: Int, baseMessage: BaseMessage) {
        this.baseMessage = baseMessage
    }

    override fun clickOnAttachmentLink(position: Int, link: String) {
        val extension = link.split(".").last()
        if (extension == "pdf"){
            val i = Intent(this,WebViewActivity::class.java)
            val b = Bundle()
            b.putString(Constants.DocumentLink,link)
            i.putExtras(b)
            startActivity(i)
        }else{
            val i = Intent(this,ImageViewerActivity::class.java)
            val b = Bundle()
            b.putString(Constants.DocumentLink,link)
            i.putExtras(b)
            startActivity(i)
        }
    }

    override fun longClickOnTextMsg(position: Int, baseMessage: BaseMessage, msgType: Int) {
        this.baseMessage = baseMessage
        this.selectedMsgPosition = position
        openMsgActionBottomSheet(baseMessage, msgType)
    }

    override fun longClickOnImageMsg(position: Int, baseMessage: BaseMessage, msgType: Int) {
        this.baseMessage = baseMessage
        this.selectedMsgPosition = position
        openMsgActionBottomSheet(baseMessage, msgType)
    }

    override fun longClickOnVideoMsg(position: Int, baseMessage: BaseMessage, msgType: Int) {
        this.baseMessage = baseMessage
        this.selectedMsgPosition = position
        openMsgActionBottomSheet(baseMessage, msgType)
    }

    override fun longClickOnFileMsg(position: Int, baseMessage: BaseMessage, msgType: Int) {
        this.baseMessage = baseMessage
        this.selectedMsgPosition = position
        openMsgActionBottomSheet(baseMessage, msgType)
    }

    override fun longClickOnMapMsg(position: Int, baseMessage: BaseMessage, msgType: Int) {
        this.baseMessage = baseMessage
        this.selectedMsgPosition = position
        openMsgActionBottomSheet(baseMessage, msgType)
    }

    override fun longClickOnAudioMsg(position: Int, baseMessage: BaseMessage, msgType: Int) {
        this.baseMessage = baseMessage
        this.selectedMsgPosition = position
        openMsgActionBottomSheet(baseMessage, msgType)
    }

    override fun longClickOnReplyMsg(position: Int, baseMessage: BaseMessage, msgType: Int) {
        this.baseMessage = baseMessage
        this.selectedMsgPosition = position
        openMsgActionBottomSheet(baseMessage, msgType)
    }

    override fun clickOnAttachmentMenu(position: Int) {
        attachmentBottomSheetDialog.dismiss()
        when (attachmentMenuList[position].menuName) {
            getString(R.string.scan_document) -> {
                isUploading = true
                scanDoc()
            }
            getString(R.string.camera) -> {
                isUploading = true
                isCameraClick = true
                getPermission()
            }
            getString(R.string.gallery) -> {
                isUploading = true
                isCameraClick = false
                getPermission()
            }
            getString(R.string.files) -> {
                isUploading = true
                openFilePicker()
            }
            getString(R.string.audio) -> {
                isUploading = true
                openAudioPicker()
            }
            getString(R.string.location) -> {
                getLocationPermission()
            }
        }
    }

    override fun onBackPressed() {
        if (isFromCreateGroup) {
            startHomeActivityFromGroup()
        } else {
            finish()
        }
    }

    private fun startHomeActivityFromGroup() {
        val intent = Intent(this, HomeActivity::class.java)
        val b = Bundle()
        b.putBoolean(Constants.IS_FROM_GROUP_CHAT, true)
        intent.putExtras(b)
        startActivity(intent)
        finish()
    }

}