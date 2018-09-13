package com.allon.commonutilsproject.animation.objectanimation

import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.allon.commonutilsproject.R
import kotlinx.android.synthetic.main.activity_object_animation.*

class ObjectAnimationActivity : AppCompatActivity() {

    companion object {
        fun start(ctx: Context) {
            ctx.startActivity(Intent(ctx, ObjectAnimationActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_object_animation)

        val transition = LayoutTransition()
        transition.setAnimator(LayoutTransition.CHANGE_APPEARING, transition.getAnimator(LayoutTransition.CHANGE_APPEARING))

        image.setOnClickListener { view ->
            ObjectAnimator.ofFloat(image, "all", 0f, 900f).apply {
                duration = 500L
                addUpdateListener {
                    //如果操作对象的该属性方法里面，比如上例的setRotationX如果内部没有调用view的重绘方法，
                    // 可以在更新值的回调函数里面手动调用
                    val value = it.animatedValue as Float
                    println("values = $value")
                    /*
                     * 在2018-09-11晚上看张鸿洋的有关属性动画的讲解博客的时候，在介绍ObjectAnimation这个类的时候
                     * 提了一问题，让用ObjectAnimation实现一个抛物线动画的效果，就想着，这几天在看Android的动画框架，
                     * 想自己试着写一写，看是否真真的掌握了Android的动画框架，于是就有了这个鞋抛物线动画的想法，最主要的
                     * 目的其实是像检查一下自己最近的学习成果，一直在看google的官方案例，只是把重点放在了动画的效果上，
                     * 给自己一个大概的印象，以防自己以后看到了类似的效果有个印象，但是好像忘记了编程最本质的东西，上手去练，
                     * 动手去写，于是今天就来实现一下，中间坎坷还是通多的。
                     * 首先从数学的角度，轨迹是抛物线的方程的运动轨迹最起码是二次函数吧，一次函数的运行轨迹是直线，刚开始
                     * 就再栽在这里了，用一次函数去描绘轨迹，总事无法看到曲线的路径。
                     * V1
                     * 刚开始想的是，给X轴和Y轴一个速度，然后以时间为变量来实现抛物线动画，在300毫秒内，得到0 - 1之间的
                     * 时间值，然后得到X轴和Y轴运动的距离，设置到View的X和Y值上面，
                     *  view.y = Vy * value
                     *  view.x = Vx * value
                     *  这样的到的运动轨迹是直线，因为它在X轴和Y轴的运动的轨迹是时间的一次函数，一次函数的图像特征是直线
                     *  V2
                     *  于是就想到了初中的抛物线方程，他的图像是抛物线形状的，[y = aX2 + bx + c],现在的问题就是求方程中
                     *  a,b,c常量的值了,其实Android中做抛物线动画的话，抛物线的方程可以简化成y = aX2，过原点就可以了。
                     *   下面的 表达式中a = 1f/2 * 0.003f , 这个值是返回试出来的，并不是解方程出来呢的，乘以0.003是为了
                     *   y的值不至于太大，太大的话view就转到屏幕以外了。
                     *   view.y = 1f/2 * value* value * 0.003f
                     *    view.x = value
                     */
                    //下面的曲线方程经过（900，900）这个点，带入y = aX2 => a  = 1/900
                    view.y = 1f/900 * value* value
                    view.x = value
                }
                start()
            }
        }

    }
}
