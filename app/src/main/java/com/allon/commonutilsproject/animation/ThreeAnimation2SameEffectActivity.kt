package com.allon.commonutilsproject.animation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.allon.commonutilsproject.R
import com.allon.commonutilsproject.R.id.*
import kotlinx.android.synthetic.main.activity_three_animation2_same_effect.*

class ThreeAnimation2SameEffectActivity : AppCompatActivity(), View.OnClickListener {


    companion object {
        fun start(ctx:Context) {
            ctx.startActivity(Intent(ctx, ThreeAnimation2SameEffectActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_three_animation2_same_effect)
        object_animate.setOnClickListener(this)
        property_animate.setOnClickListener(this)
        view_animate.setOnClickListener(this)
    }


    override fun onClick(v: View) {
        when(v.id) {
            R.id.object_animate -> {
                val animatorX = ObjectAnimator.ofFloat(object_animate, "x", 100f)
                val animatorY = ObjectAnimator.ofFloat(object_animate, "y", 100f)
                val animatorSet = AnimatorSet()
                animatorSet.playTogether(animatorX, animatorY);
                animatorSet.start()
            }
            R.id.property_animate -> {
                val holderX = PropertyValuesHolder.ofFloat("x", 300f)
                val holderY = PropertyValuesHolder.ofFloat("y", 300f)
                ObjectAnimator.ofPropertyValuesHolder(property_animate,holderX,holderY).start()
            }
            R.id.view_animate -> {
             view_animate.animate().x(500f).y(500f)
            }
        }
    }

}
