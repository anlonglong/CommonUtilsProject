package com.allon.commonutilsproject.animation

import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import com.allon.commonutilsproject.R
import kotlinx.android.synthetic.main.activity_animation.*
import android.animation.PropertyValuesHolder


class AnimationActivity : AppCompatActivity(), View.OnClickListener {

    var x: Int = 0
        set(value) {
            println("value ==  $value")
            field = value
        }

    companion object {
        fun start(ctx: Context) {
            ctx.startActivity(Intent(ctx, AnimationActivity::class.java))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation)
        hide.setOnClickListener(this)
        add.setOnClickListener(this)
        show.setOnClickListener(this)
        remove.setOnClickListener(this)

        //新增加view出现时的动画
        appending()

        //已存在view的动画
        changedAppending()
    }

    private fun changedAppending() {
        /*
        简单分析一下，LayoutTransition.APPEARING和LayoutTransition.DISAPPEARING的情况下直接使用属性动画来设置过渡动画效果即可，
        而对于LayoutTransition.CHANGE_APPEARING和LayoutTransition.CHANGE_DISAPPEARING必须使用PropertyValuesHolder所构造的动画才会有效果，
        不然无效，真特么恶心， ,但没想到更恶心的在后面,在测试效果时发现在构造动画时，”left”、”top”、”bottom”、”right”属性的变动是必须设置的,至少设置两个,
        不然动画无效,最坑爹的是我们即使这些属性不想变动!!!也得设置!!!我就问您恶不恶心!,那么我们不想改变这四个属性时该如何设置呢？
        这时只要传递的可变参数都一样就行如下面的(0,0)也可以是(100,100)即可(坑爹啊！测试半天才发现，
        一直在考虑代码有没有问题，最后发现时特么的也是醉了…….)：
         */
        val pvhLeft = PropertyValuesHolder.ofInt("left", 0, 0)
        val pvhTop = PropertyValuesHolder.ofInt("top", 0, 0)
        val pvhRight = PropertyValuesHolder.ofInt("right", 0, 0)
        val pvhBottom = PropertyValuesHolder.ofInt("bottom", 0, 0)
        val lt = LayoutTransition()
        lt.setDuration(LayoutTransition.CHANGE_APPEARING, 300)
        val animator = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.5f, 1f)
        val inAnim = ObjectAnimator.ofPropertyValuesHolder(
                container, pvhLeft, pvhRight, pvhTop, pvhBottom, animator).setDuration(lt.getDuration(LayoutTransition.CHANGE_APPEARING))
        lt.setAnimator(LayoutTransition.CHANGE_APPEARING, inAnim)
        container.layoutTransition = lt
    }

    private fun appending() {
        val animator = ObjectAnimator.ofFloat(null, View.SCALE_X, 0f, 0.4f, 0.8f, 1f)
        val lt = LayoutTransition()
        lt.setDuration(LayoutTransition.APPEARING, 1000)
        lt.setAnimator(LayoutTransition.APPEARING, animator)
        container.layoutTransition = lt
    }

    private var openAnim = true
    override fun onClick(v: View) {
        when (v.id) {
            R.id.hide -> {
                hide.visibility = View.GONE
            }
            R.id.show -> {
                hide.visibility = View.VISIBLE
            }
            R.id.remove -> {
                container.removeViewAt(0)
            }
            R.id.add -> {
                val btn = Button(this)
                btn.text = "新加的按钮"
                val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                layoutParams.gravity = Gravity.CENTER_HORIZONTAL
                btn.layoutParams = layoutParams
                container.addView(btn, 0)
            }
        }
    }
}
