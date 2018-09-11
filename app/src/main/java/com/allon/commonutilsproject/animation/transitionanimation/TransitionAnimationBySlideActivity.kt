package com.allon.commonutilsproject.animation.transitionanimation

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.transition.Slide
import android.transition.TransitionInflater
import android.view.MenuItem
import com.allon.commonutilsproject.R
import kotlinx.android.synthetic.main.activity_share_element.*

class TransitionAnimationBySlideActivity : AppCompatActivity() {

    companion object {

        val SLIDE_TYPE = "slide type"
        val SLIDE_CODE = "slide code"
        val SLIDE_XML = "slide xml"

        fun startWithCode(ctx: Activity) {

            val intent = Intent(ctx, TransitionAnimationBySlideActivity::class.java)
            intent.putExtra(SLIDE_TYPE, SLIDE_CODE)
            ctx.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(ctx).toBundle())
        }

        fun startWithXml(ctx: Activity) {

            val intent = Intent(ctx, TransitionAnimationBySlideActivity::class.java)
            intent.putExtra(SLIDE_TYPE, SLIDE_XML)
            ctx.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(ctx).toBundle())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_element)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val type = intent.extras.getString(SLIDE_TYPE)
        if (type == SLIDE_CODE) {
            val slide = Slide()
            slide.duration = 500L
            window.enterTransition = slide
        }else{
            val transition = TransitionInflater.from(this).inflateTransition(R.transition.slide)
            window.enterTransition = transition

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                supportFinishAfterTransition()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
