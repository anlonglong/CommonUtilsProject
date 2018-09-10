package com.allon.commonutilsproject.animation.transitionanimation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.transition.*
import android.view.MenuItem
import android.view.animation.AnimationUtils
import com.allon.commonutilsproject.R
import kotlinx.android.synthetic.main.activity_share_element.*

class TransitionAnimationByExplodeActivity : AppCompatActivity() {

    companion object {
        val EXPLODE_TYPE = "explode type"
        val EXPLODE_CODE = "explode code"
        val EXPLODE_XML = "explode xml"

        fun startWithCode(ctx: Activity) {

            val intent = Intent(ctx, TransitionAnimationByExplodeActivity::class.java)
            intent.putExtra(EXPLODE_TYPE, EXPLODE_CODE)
            ctx.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(ctx).toBundle())
        }

        fun startWithXml(ctx: Activity) {

            val intent = Intent(ctx, TransitionAnimationByExplodeActivity::class.java)
            intent.putExtra(EXPLODE_TYPE, EXPLODE_XML)
            ctx.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(ctx).toBundle())
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_element)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val type = intent.extras.getString(EXPLODE_TYPE)
        if (type == EXPLODE_CODE) {
            val expolde = Explode()
            expolde.duration = 500L
            window.enterTransition = expolde
        }else{
            val transition = TransitionInflater.from(this).inflateTransition(R.transition.explode)
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
