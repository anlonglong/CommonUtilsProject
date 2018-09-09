package com.allon.commonutilsproject.animation

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.allon.commonutilsproject.R
import kotlinx.android.synthetic.main.activity_card_flip.*

class CardFlipActivity : AppCompatActivity() {

    companion object {
        fun start(ctx: Context) {
            ctx.startActivity(Intent(ctx, CardFlipActivity::class.java))
        }
    }


    private var mShowingBack: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_flip)

        if (null == savedInstanceState) {
             supportFragmentManager.beginTransaction().add(R.id.container, CardFrontFragment()).commit()
        }

        flip.setOnClickListener {  flipCard() }

    }

    /*
                         alpha      rotationY        alpha
     * step1. left_out              0 ~ 180°         1 ~ 0
     * step2. right_in   1 ~ 0      0 ~ 180°         0 ~ 1
     * step3. right_out             -180° ~ 0        1 ~ 0
     * step4. left_in    1 ~ 0      -180° ~ 0        0 ~ 1
     *
     * 其中，两个alpha值都是在旋转动画执行到一半时间的时候执行的，是通过startOffset设置的
     */
    private fun flipCard() {

        mShowingBack = !mShowingBack
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.animator.card_flip_right_in,
                                     R.animator.card_flip_right_out,
                                     R.animator.card_flip_left_in,
                                     R.animator.card_flip_left_out)
                .replace(R.id.container,if(mShowingBack) CardBackFragment() else CardFrontFragment() ).commit()
    }



    class CardFrontFragment: Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.fragment_card_front, container, false);
        }
    }

    class CardBackFragment: Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.fragment_card_back, container, false);
        }
    }


}
