package com.allon.commonutilsproject.animation

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Animatable
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import com.allon.commonutilsproject.R
import kotlinx.android.synthetic.main.activity_vector_animator.*

class VectorAnimatorActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View) {
        if (v is ImageView) {
            if (v.drawable is Animatable) {
                (v.drawable as Animatable).start()
            }
        }
    }

    companion object {
        fun start(ctx: Context) {
            ctx.startActivity(Intent(ctx, VectorAnimatorActivity::class.java))
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vector_animator)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        iv_puppet1.setOnClickListener(this)
        iv_puppet2.setOnClickListener(this)
        iv_puppet2.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.drawable_animated_vector))
        val drawable = iv_puppet3.drawable
        if (drawable is AnimatedVectorDrawable) {
            drawable.registerAnimationCallback(@RequiresApi(Build.VERSION_CODES.M)
            object : Animatable2.AnimationCallback (){

                override fun onAnimationStart(drawable: Drawable?) {
                    super.onAnimationStart(drawable)
                    (drawable as AnimatedVectorDrawable).unregisterAnimationCallback(this)
                }
            })
        }

        iv_puppet3.setOnClickListener(this)


    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
