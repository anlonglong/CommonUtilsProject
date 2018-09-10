package com.allon.commonutilsproject.animation.revealanimation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import com.allon.commonutilsproject.R
import kotlinx.android.synthetic.main.activity_reveal.*

class RevealActivity : AppCompatActivity() {

    companion object {
        fun start(ctx: Context) {
            ctx.startActivity(Intent(ctx, RevealActivity::class.java))
        }
    }


    var show:Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reveal)

        reveal_animator.setOnClickListener {
            if (show) {
                revealAnimation(show, content)
            }else {
                revealAnimation(show, content)
            }
            show = !show
        }

        reveal_effect.setOnClickListener {
            RevealEffectActivity.start(this)
        }



    }

    /**
     *  可以用来做view的显示或者隐藏动画，动画开始执行的点在view是中点，也可以是用户手指触摸的屏幕的点【这个此方法不能实现，需要再次封装】
     *  isShow:true   执行从不见到可见的过度动画
     *         false  执行从可见到不可见的过度动画
     */
    fun revealAnimation(isShow: Boolean, view: View) {
        val x = view.width / 2
        val y = view.height / 2
        val radius = Math.hypot(x.toDouble(), y.toDouble()).toFloat()
        val startRadius = if (isShow) 0f else radius
        val endRadius = if (isShow) radius else 0f
        val animator = ViewAnimationUtils.createCircularReveal(view, x, y, startRadius, endRadius)
        animator.duration = 300L
        if (isShow) {
            view.visibility = View.VISIBLE
        }else{
            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    view.visibility = View.INVISIBLE
                }
            })
        }
        animator.start()
    }
}
