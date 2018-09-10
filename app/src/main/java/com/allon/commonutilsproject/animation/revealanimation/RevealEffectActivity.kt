package com.allon.commonutilsproject.animation.revealanimation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AnimationUtils
import com.allon.commonutilsproject.R
import kotlinx.android.synthetic.main.activity_reveal_effect.*

class RevealEffectActivity : AppCompatActivity() {

    companion object {
        fun start(ctx: Context) {
            ctx.startActivity(Intent(ctx, RevealEffectActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reveal_effect)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.inflateMenu(R.menu.reveal_animation)


        toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.action_settings ->{
                    finish()
                    return@setOnMenuItemClickListener true
                }else ->  return@setOnMenuItemClickListener  false

            }
        }

        fab.setOnClickListener {
            doRevealAnimation()
        }

    }

    var flag = false

    private fun doRevealAnimation() {
       val fabLocation = IntArray(2)
        fab.getLocationInWindow(fabLocation)
        val cenX = fabLocation[0]+fab.measuredWidth/2
        val cenY = fabLocation[1]+fab.measuredHeight/2
        val radian = Math.hypot(view_puppet.width.toDouble(), view_puppet.height.toDouble()).toFloat()
        if (flag) {
            val animator = ViewAnimationUtils.createCircularReveal(view_puppet, cenX, cenY,  radian, 0f)
            animator.apply {
                duration = 1000L
                interpolator = AnimationUtils.loadInterpolator(this@RevealEffectActivity, android.R.interpolator.accelerate_decelerate)
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        view_puppet.visibility = View.INVISIBLE
                    }
                })
            }.start()
        }else {
            val animator = ViewAnimationUtils.createCircularReveal(view_puppet, cenX, cenY, 0f, radian)
            animator.apply {
                duration = 1000L
                interpolator = AnimationUtils.loadInterpolator(this@RevealEffectActivity, android.R.interpolator.accelerate_decelerate)
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator?) {
                        view_puppet.visibility = View.VISIBLE
                    }
                })
            }.start()
        }
        flag = !flag
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
