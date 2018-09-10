package com.allon.commonutilsproject.animation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.allon.commonutilsproject.R
import kotlinx.android.synthetic.main.activity_enlarge_view.*
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.animation.DecelerateInterpolator
import android.animation.ObjectAnimator
import android.animation.AnimatorSet
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.graphics.Rect
import android.view.View
import android.widget.ImageView


class EnlargeViewActivity : AppCompatActivity() {
    /**
     * 1.ImageButton是小版本的，能点击的，点击后显示大版本的ImageView。

    2.ImageView是大版本的，可以显示ImageButton点击后的样式。

    3.ImageView一开始是不可见的（invisible），当ImageButton点击后，它会实现zoom动画，就像从ImageButton上扩大显示出来。
     */

    companion object {
        fun start(ctx: Context) {
            ctx.startActivity(Intent(ctx, EnlargeViewActivity::class.java))
        }
    }

    private var mShortAnimationDuration: Int = 5000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enlarge_view)
        mShortAnimationDuration = resources.getInteger(
                android.R.integer.config_shortAnimTime)
        thumb_button_1.setOnClickListener {
            zoomImageFromThumb(thumb_button_1, R.drawable.pic);
        }
    }


    private var mCurrentAnimator: AnimatorSet? = null


    /*
    你需要把从正常大小的view到扩大以后的view这个过程作成动画。

    1.指定想要zoom的图片给ImageView。（理想情况下，这个bitmap的大小不应该比屏幕大）

    2.计算这个ImageView的开始和结束位置

    3.把四个点和缩放大小的属性同时作成动画，从开始的状态到结束的状态。这四个动画被添加到AnimatorSet中，方便他们同时执行。

    4.当用户再次点击屏幕时，动画要执行回去。一样道理，给ImageView一个View.OnClickListener,然后隐藏ImageView。
     */
    private fun zoomImageFromThumb(thumbView: View, imageResId: Int) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator?.cancel()
        }

        // 加载高分辨率的图片
        val expandedImageView = findViewById<View>(
                R.id.expanded_image) as ImageView
        expanded_image.setImageResource(imageResId)

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        // 计算开始和结束位置的图片范围
        val startBounds = Rect()
        val finalBounds = Rect()
        val globalOffset = Point()

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        // 开始的范围就是ImageButton的范围，
        // 结束的范围是容器（FrameLayout）的范围
        // getGlobalVisibleRect(Rect)得到的是view相对于整个硬件屏幕的Rect
        // 即绝对坐标，减去偏移，获得动画需要的坐标，即相对坐标
        // getGlobalVisibleRect(Rect,Point)中，Point获得的是view在它在
        // 父控件上的坐标与在屏幕上坐标的偏移
        thumbView.getGlobalVisibleRect(startBounds)
        findViewById<View>(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset)
        startBounds.offset(-globalOffset.x, -globalOffset.y)
        finalBounds.offset(-globalOffset.x, -globalOffset.y)

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        // 下面这段逻辑其实就是保持纵横比
        // 如果结束图片的宽高比比开始图片的宽高比大
        // 就是结束时“视觉上”拉宽了（压扁了）图片
        val startScale: Float
        if ((finalBounds.width().toFloat()).div(finalBounds.height()) > (startBounds.width().toFloat()) / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (startBounds.height().toFloat()) / finalBounds.height()
            val startWidth = (startScale * finalBounds.width()).toInt()
            val deltaWidth = (startWidth.toFloat() - startBounds.width().toFloat()) / 2
            startBounds.left -= deltaWidth.toInt()
            startBounds.right += deltaWidth.toInt()
        } else {
            // Extend start bounds vertically
            startScale = (startBounds.width().toFloat()) / finalBounds.width()
            val startHeight = startScale * finalBounds.height()
            val deltaHeight = (startHeight - startBounds.height().toFloat()) / 2
            startBounds.top -= deltaHeight.toInt()
            startBounds.bottom += deltaHeight.toInt()
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        // 隐藏小的图片，展示大的图片。当动画开始的时候，
        // 要把大的图片发在小的图片的位置上
        //小的设置透明
        thumbView.alpha = 0f
        expandedImageView.visibility = View.VISIBLE

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.pivotX = 0f
        expandedImageView.pivotY = 0f

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        val set = AnimatorSet()
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left.toFloat(), finalBounds.left.toFloat()))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top.toFloat(), finalBounds.top.toFloat()))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView,
                        View.SCALE_Y, startScale, 1f))
        set.duration = mShortAnimationDuration.toLong()
        set.interpolator = DecelerateInterpolator()
        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mCurrentAnimator = null
            }

            override fun onAnimationCancel(animation: Animator) {
                mCurrentAnimator = null
            }
        })
        set.start()
        mCurrentAnimator = set

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        // 再次点击返回小的图片，就是上面扩大的反向动画。即预览完成
        expandedImageView.setOnClickListener {
            if (mCurrentAnimator != null) {
                mCurrentAnimator?.cancel()
            }

            // Animate the four positioning/sizing properties in parallel,
            // back to their original values.
            val set = AnimatorSet()
            set.play(ObjectAnimator
                    .ofFloat(expandedImageView, View.X, startBounds.left.toFloat()))
                    .with(ObjectAnimator
                            .ofFloat(expandedImageView,
                                    View.Y, startBounds.top.toFloat()))
                    .with(ObjectAnimator
                            .ofFloat(expandedImageView,
                                    View.SCALE_X, startScale))
                    .with(ObjectAnimator
                            .ofFloat(expandedImageView,
                                    View.SCALE_Y, startScale))
            set.duration = mShortAnimationDuration.toLong()
            set.interpolator = DecelerateInterpolator()
            set.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    thumbView.alpha = 1f
                    expandedImageView.visibility = View.GONE
                    mCurrentAnimator = null
                }

                override fun onAnimationCancel(animation: Animator) {
                    thumbView.alpha = 1f
                    expandedImageView.visibility = View.GONE
                    mCurrentAnimator = null
                }
            })
            set.start()
            mCurrentAnimator = set
        }
    }


}
