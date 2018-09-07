package com.allon.commonutilsproject

import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.allon.commonutilsproject.animation.AnimationActivity
import com.allon.commonutilsproject.fileprovider.FileProviderActivity
import com.allon.commonutilsproject.notification.NotificationActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_notification.setOnClickListener(this)
        file_provider.setOnClickListener(this)
        animator.setOnClickListener(this)
    }




    override fun onClick(v: View) {
       when(v.id) {
           R.id.tv_notification -> { NotificationActivity.start(this) }
           R.id.file_provider -> { FileProviderActivity.start(this) }
           R.id.animator -> { AnimationActivity.start(this) }
       }
    }
}
