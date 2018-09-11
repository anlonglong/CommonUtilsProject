package com.allon.commonutilsproject.animation.transitionanimation

import android.support.v4.view.ViewCompat.getTransitionName
import android.app.Activity
import android.support.annotation.NonNull
import android.view.View
import java.util.*


/**
 * @author  anlonglong on 2018/9/11.
 * Email： 940752944@qq.com
 */
object TransitionHelper {
    fun createSafeTransitionParticipants(activity: Activity,
                                         includeStatusBar: Boolean,  vararg otherParticipants: android.support.v4.util.Pair<View, String>): Array<android.support.v4.util.Pair<View, String>> {
        // Avoid system UI glitches as described here:
        // https://plus.google.com/+AlexLockwood/posts/RPtwZ5nNebb
        val decor = activity.window.decorView
        var statusBar: View? = null
        if (includeStatusBar) {
            statusBar = decor.findViewById(android.R.id.statusBarBackground)
        }
        val navBar = decor.findViewById<View>(android.R.id.navigationBarBackground)

        // Create pair of transition participants.
        val participants = ArrayList<android.support.v4.util.Pair<View, String>>(3)
        addNonNullViewToTransitionParticipants(statusBar, participants)
        addNonNullViewToTransitionParticipants(navBar, participants)
        // only add transition participants if there's at least one none-null element
        if (otherParticipants != null && !(otherParticipants.size == 1 && otherParticipants[0] == null)) {

            participants.addAll(otherParticipants)
        }
        return participants.toTypedArray()
    }

    private fun addNonNullViewToTransitionParticipants(view: View?, participants: MutableList<android.support.v4.util.Pair<View, String>>) {
        if (view == null) {
            return
        }
        participants.add(android.support.v4.util.Pair(view, view.transitionName))
    }

}