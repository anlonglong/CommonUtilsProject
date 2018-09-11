package com.allon.commonutilsproject.animation.transitionanimation

import android.app.Activity
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.allon.commonutilsproject.R
import kotlinx.android.synthetic.main.item_recycle.view.*

/**
 * @author  anlonglong on 2018/9/11.
 * Email： 940752944@qq.com
 */
class MyRecycleViewAdapter(val activity: Activity): RecyclerView.Adapter<MyRecycleViewAdapter.MyViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(View.inflate(parent.context, R.layout.item_recycle, null))
    }

    override fun getItemCount() = 10

    override fun onBindViewHolder(holder: MyRecycleViewAdapter.MyViewHolder, position: Int) {
        val ctx = holder.itemView.context
        holder.itemView.setOnClickListener {
            val activityOptionsCompat: ActivityOptionsCompat
            activityOptionsCompat = if (holder.adapterPosition % 2 == 0) {
                val pari1 = android.support.v4.util.Pair<View, String>(holder.itemView.imageview, ctx.getString(R.string.share_element_imageview))
                val pari2 = android.support.v4.util.Pair<View, String>(holder.itemView.header, ctx.getString(R.string.share_element_header))
                val pari3 = android.support.v4.util.Pair<View, String>(holder.itemView.tv_info, ctx.getString(R.string.share_element_tv_info))
                ActivityOptionsCompat.makeSceneTransitionAnimation(activity,pari1, pari2, pari3)
            }else {
                val pairs = TransitionHelper.createSafeTransitionParticipants(
                        activity, true,
                        android.support.v4.util.Pair(holder.itemView.imageview, activity.getString(R.string.share_element_imageview)),
                        android.support.v4.util.Pair(holder.itemView.header, activity.getString(R.string.share_element_header)),
                        android.support.v4.util.Pair(holder.itemView, activity.getString(R.string.share_element_tv_info)))
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity, pairs[0],pairs[1],pairs[2]
                )
            }
            ShareElementActivity.start(activity, activityOptionsCompat)
        }
    }

    inner class MyViewHolder(contentView: View): RecyclerView.ViewHolder(contentView)

}