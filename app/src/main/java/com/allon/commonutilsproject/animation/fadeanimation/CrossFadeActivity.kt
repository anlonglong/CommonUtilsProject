package com.allon.commonutilsproject.animation.fadeanimation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.allon.commonutilsproject.R
import kotlinx.android.synthetic.main.activity_cross_fade.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

/**
 * 淡入淡出的动画
 */
class CrossFadeActivity : AppCompatActivity() {

    //缓存动画的执行的时间
    private val animatorTime by lazy { resources.getInteger(android.R.integer.config_longAnimTime).toLong() }



    companion object {
        fun start(ctx: Context) {
            ctx.startActivity(Intent(ctx, CrossFadeActivity::class.java))
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cross_fade)
        initViewState()
    }

    private fun initViewState() {
         async {
             delay(2000)
             launch(UI) {
                  content.animate().alpha(1f).setDuration(animatorTime).setListener(null)
                  progress.animate().apply {
                      alpha(0f)
                      duration = animatorTime
                      setListener(object : AnimatorListenerAdapter(){
                          override fun onAnimationEnd(animation: Animator?) {
                              progress.visibility = View.GONE
                          }
                      })
                  }
             }
         }
    }
}
