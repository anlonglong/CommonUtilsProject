package com.allon.commonutilsproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.allon.commonutilsproject.notification.NotificationActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_notification.setOnClickListener(this)
    }

    override fun onClick(v: View) {
       when(v.id) {
           R.id.tv_notification -> { NotificationActivity.start(this) }
       }
    }
}