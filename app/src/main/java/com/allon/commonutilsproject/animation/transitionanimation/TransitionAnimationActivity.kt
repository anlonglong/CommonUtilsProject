package com.allon.commonutilsproject.animation.transitionanimation

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.allon.commonutilsproject.R
import kotlinx.android.synthetic.main.activity_transition_animation.*

class TransitionAnimationActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View) {
           for (i in 0 until (v.parent as ViewGroup).childCount) {
               if (v == (v.parent as ViewGroup).getChildAt(i)) {
                   when(i) {
                       0 -> {
                           TransitionAnimationByExplodeActivity.startWithCode(this)
                       }
                       1 -> {
                           TransitionAnimationByExplodeActivity.startWithXml(this)
                       }
                   }
               }
           }
    }

    companion object {
        fun start(ctx: Context) {
            ctx.startActivity(Intent(ctx, TransitionAnimationActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transition_animation)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        for (i in 0 until viewgroup_transition.childCount) {
            viewgroup_transition.getChildAt(i).setOnClickListener(this)
        }
        rv_share_elements.layoutManager = GridLayoutManager(this,2)
        rv_share_elements.isNestedScrollingEnabled = false
        rv_share_elements.adapter = MyRecycleViewAdapter(this)

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
