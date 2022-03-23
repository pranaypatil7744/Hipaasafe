package com.hipaasafe.presentation.image_viewer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.print.PrintManager
import android.transition.Transition
import android.view.View.GONE
import android.view.View.VISIBLE
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivityImageViewerBinding
import com.hipaasafe.presentation.adapter.MyPrintDocumentAdapter
import com.hipaasafe.utils.ImageUtils
import com.hipaasafe.utils.isNetworkAvailable
import java.io.File
import java.io.FileOutputStream

class ImageViewerActivity : BaseActivity() {
    lateinit var binding:ActivityImageViewerBinding
    var docLink:String = ""
    var imgLink:String =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toggleLoader(true)
        getIntentData()
        setUpToolbar()
    }

    private fun getIntentData() {
        binding.apply {
            intent.extras?.run {
                docLink = getString(Constants.DocumentLink).toString()
                setUpView()
            }
        }
    }

    private fun setUpView() {
        binding.apply {
            if (isNetworkAvailable()){
                Glide.with(this@ImageViewerActivity)
                    .asBitmap()
                    .load(docLink)
                    .into(object :CustomTarget<Bitmap>(){
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                        ) {
                            imgDocument.setImageBitmap(resource)
                            createPdf(resource)
                            toggleLoader(false)
                            toolbar.btnOne.apply {
                                visibility = VISIBLE
                                setImageResource(R.drawable.ic_print)
                                setOnClickListener {
                                    sendFileForPrinting()
                                }
                            }
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {

                        }

                    })
            }else{
                showToast(getString(R.string.no_internet_connection))
            }
        }
    }

    private fun toggleLoader(showLoader: Boolean) {
        toggleFadeView(
            binding.root,
            binding.contentLoading.root,
            binding.contentLoading.imageLoading,
            showLoader
        )
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.view_document)
            tvDate.visibility = GONE
            divider.visibility = VISIBLE
            btnBack.visibility = VISIBLE
            btnBack.setOnClickListener {
                finish()
            }
        }
    }
    fun sendFileForPrinting() {
        val printManager: PrintManager =
            this.getSystemService(Context.PRINT_SERVICE) as PrintManager
        val jobName = this.getString(R.string.app_name) + "print"
        printManager.print(
            jobName,
            MyPrintDocumentAdapter(imgLink,isLocalPath = true), null
        )
    }
    private fun createPdf(bitmap:Bitmap){
        // Load JPG file into bitmap
//        val f: File = File("")
//        val bitmap: Bitmap = BitmapFactory.decodeFile(f.absolutePath)

        // Create a PdfDocument with a page of the same size as the image
        val document: PdfDocument = PdfDocument()
        val pageInfo: PdfDocument.PageInfo  = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page: PdfDocument.Page  = document.startPage(pageInfo)

        // Draw the bitmap onto the page
        val canvas: Canvas = page.canvas
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        document.finishPage(page)

        // Write the PDF file to a file
        val directoryPath: String  = "/data/user/0/com.hipaasafe/files/"
        imgLink = "$directoryPath/example.pdf"
        document.writeTo( FileOutputStream(imgLink))
        document.close()
    }
}