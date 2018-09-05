package com.allon.commonutilsproject.notification

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.allon.commonutilsproject.R

class ResultActivity : AppCompatActivity() {


    companion object {
        fun start(ctx: Context) {
            ctx.startActivity(Intent(ctx, ResultActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
    }
}
