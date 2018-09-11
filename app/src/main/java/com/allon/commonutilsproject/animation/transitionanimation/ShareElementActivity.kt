package com.allon.commonutilsproject.animation.transitionanimation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.allon.commonutilsproject.R
import kotlinx.android.synthetic.main.activity_share_element.*

class ShareElementActivity : AppCompatActivity() {

    companion object {
        fun start(ctx: Context, activityOptionsCompat: ActivityOptionsCompat) {
            ctx.startActivity(Intent(ctx, ShareElementActivity::class.java), activityOptionsCompat.toBundle())
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_element)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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

