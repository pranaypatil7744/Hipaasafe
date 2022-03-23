package com.hipaasafe.presentation.adapter

import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import com.hipaasafe.utils.AppUtils
import com.hipaasafe.utils.TagName
import java.io.*
import java.net.URL


class MyPrintDocumentAdapter(val url:String) : PrintDocumentAdapter() {

    override fun onLayout(
        oldAttributes: PrintAttributes,
        newAttributes: PrintAttributes,
        cancellationSignal: CancellationSignal,
        callback:
        LayoutResultCallback,
        metadata: Bundle
    ) {

        if (cancellationSignal.isCanceled) {
            callback.onLayoutCancelled()
            return
        }
        val pdi = PrintDocumentInfo.Builder("chat_history.pdf")
            .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT).build()
        callback.onLayoutFinished(pdi, true)
    }

    override fun onWrite(
        pageRanges: Array<PageRange>,
        destination: ParcelFileDescriptor,
        cancellationSignal: CancellationSignal,
        callback:
        WriteResultCallback
    ) {
        lateinit var input: InputStream
        lateinit var output: OutputStream


        try {
//            val outputDir: File? = BaseApplication.applicationContext()
//                .getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) // context being the Activity pointer
//            val targetPdf2 = outputDir?.path + "/2.pdf"
            val targetPdf ="/data/user/0/com.hipaasafe/files/Screenshot_2022-03-19-20-37-33-134_com.instagram.android.jpg"

//            val policy = ThreadPolicy.Builder().permitAll().build()
//            StrictMode.setThreadPolicy(policy)
            input = URL(url).openStream()
//            input = FileInputStream(targetPdf)
            output = FileOutputStream(destination.fileDescriptor)

            val buf = ByteArray(24 * 16384)
            var bytesRead = 0

            while (input.read(buf).also { bytesRead = it } >= 0
                && !cancellationSignal.isCanceled) {
                output.write(buf, 0, bytesRead)
            }
            callback.onWriteFinished(arrayOf<PageRange>(PageRange.ALL_PAGES))

            input.close();
            output.close();

        } catch (ee: FileNotFoundException) {
            AppUtils.INSTANCE?.logMe(TagName.EXCEPTION_TAG, ee.localizedMessage)
        } catch (e: Exception) {
            AppUtils.INSTANCE?.logMe(TagName.EXCEPTION_TAG, e.localizedMessage)
        } finally {
            try {

            } catch (e: IOException) {
                AppUtils.INSTANCE?.logMe(TagName.EXCEPTION_TAG, e.localizedMessage)
            }
        }
    }
}