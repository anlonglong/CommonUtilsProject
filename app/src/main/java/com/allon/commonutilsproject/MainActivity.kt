package com.allon.commonutilsproject

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.allon.commonutilsproject.animation.*
import com.allon.commonutilsproject.animation.drawableanimator.DrawableAnimationActivity
import com.allon.commonutilsproject.animation.fadeanimation.CrossFadeActivity
import com.allon.commonutilsproject.animation.layoutanimation.LayoutAnimationActivity
import com.allon.commonutilsproject.animation.revealanimation.RevealActivity
import com.allon.commonutilsproject.animation.transitionanimation.ShareElementActivity
import com.allon.commonutilsproject.animation.transitionanimation.TransitionAnimationActivity
import com.allon.commonutilsproject.animation.transitionanimation.TransitionAnimationByExplodeActivity
import com.allon.commonutilsproject.fileprovider.FileProviderActivity
import com.allon.commonutilsproject.notification.NotificationActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_notification.setOnClickListener(this)
        file_provider.setOnClickListener(this)
        file_provider.translationX = 0f
        animator.setOnClickListener(this)
        drawable_animator.setOnClickListener(this)
        fade_animator.setOnClickListener(this)
        btn_animator.setOnClickListener(this)
        three_animator.setOnClickListener(this)
        fragment_animator.setOnClickListener(this)
        reveal_animator.setOnClickListener(this)
        path_animator.setOnClickListener(this)
        enlarge_pic.setOnClickListener(this)
        transition.setOnClickListener(this)
        vertor_anomation.setOnClickListener(this)
    }




    override fun onClick(v: View) {
       when(v.id) {
           R.id.tv_notification -> { NotificationActivity.start(this) }
           R.id.file_provider -> { FileProviderActivity.start(this) }
           R.id.animator -> { LayoutAnimationActivity.start(this) }
           R.id.drawable_animator -> { DrawableAnimationActivity.start(this) }
           R.id.fade_animator -> { CrossFadeActivity.start(this) }
           R.id.three_animator -> { ThreeAnimation2SameEffectActivity.start(this) }
           R.id.fragment_animator -> { CardFlipActivity.start(this) }
           R.id.reveal_animator -> { RevealActivity.start(this) }
           R.id.path_animator -> { PathActivity.start(this) }
           R.id.enlarge_pic -> { EnlargeViewActivity.start(this) }
           R.id.transition -> { TransitionAnimationActivity.start(this) }
           R.id.vertor_anomation -> { VectorAnimatorActivity.start(this) }
       }
    }
}
