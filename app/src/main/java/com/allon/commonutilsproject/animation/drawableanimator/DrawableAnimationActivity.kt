package com.allon.commonutilsproject.animation.drawableanimator

import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.allon.commonutilsproject.R
import kotlinx.android.synthetic.main.activity_cross_fade.*
import kotlinx.android.synthetic.main.activity_drawable_animation.*

class DrawableAnimationActivity : AppCompatActivity(), View.OnClickListener {


    companion object {
        fun start(ctx: Context) {
            ctx.startActivity(Intent(ctx, DrawableAnimationActivity::class.java))
        }
    }

    private lateinit var mDrawableAnimator: AnimationDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawable_animation)
        drawable_animation_imag.setOnClickListener(this)
        mDrawableAnimator =  drawable_animation_imag.background as AnimationDrawable
    }

    override fun onClick(v: View) {
        if (mDrawableAnimator.isRunning) {
            //开启之前必须先停止
            mDrawableAnimator.stop()
        }
        mDrawableAnimator.start()
    }
}
