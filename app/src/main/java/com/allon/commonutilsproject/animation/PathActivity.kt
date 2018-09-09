package com.allon.commonutilsproject.animation

import android.content.Context
import android.content.Intent
import android.graphics.Path
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.PathInterpolator
import com.allon.commonutilsproject.R
import kotlinx.android.synthetic.main.activity_path.*
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AnimationUtils


class PathActivity : AppCompatActivity() {

    companion object {
        fun start(ctx: Context) {
            ctx.startActivity(Intent(ctx, PathActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_path)
        btn_path.setOnClickListener {
            archTo()
        }
    }

    private fun archTo() {
        val path = Path()
        path.arcTo(0f, 0f, 1000f, 1000f, 270f, -180f, true)
        //val pathInterpolator = PathInterpolator(path)
        val animation = ObjectAnimator.ofFloat(view, View.X, View.Y, path)
        val interpolator = AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_linear_in)
        animation.interpolator = interpolator
        animation.duration = 2000L
        animation.start()
    }
}
