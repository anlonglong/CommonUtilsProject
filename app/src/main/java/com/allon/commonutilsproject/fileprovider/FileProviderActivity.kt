package com.allon.commonutilsproject.fileprovider

import android.Manifest
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.allon.commonutilsproject.R
import kotlinx.android.synthetic.main.activity_file_provider.*
import android.provider.MediaStore
import com.cyou.xiyou.cyou.common.fileprovider7.FileProvider7
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class FileProviderActivity : AppCompatActivity() {

    private val REQUEST_CODE_TAKE_PHOTO = 0x110
    private val REQ_PERMISSION_CODE_SDCARD = 0X111
    private val REQ_PERMISSION_CODE_TAKE_PHOTO = 0X112

    private var mCurrentPhotoPath: String? = null

    companion object {
        fun start(ctx: Context) {
            ctx.startActivity(Intent(ctx, FileProviderActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_provider)
        take_pic.setOnClickListener {  hasTakePhotoPermission(); }
    }

    private fun hasTakePhotoPermission() {
        if (ContextCompat.checkSelfPermission(this.applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != 0) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQ_PERMISSION_CODE_TAKE_PHOTO)
        } else {
            takePhoto()
        }
    }


    private fun takePhoto() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            val filename = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINA)
                    .format(Date()) + ".png"
            val file = File(Environment.getExternalStorageDirectory(), filename)
            mCurrentPhotoPath = file.absolutePath
            val fileUri = FileProvider7.getUriForFile(this, file)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PHOTO)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
            image.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQ_PERMISSION_CODE_TAKE_PHOTO) {
            takePhoto()
        }
    }
}
