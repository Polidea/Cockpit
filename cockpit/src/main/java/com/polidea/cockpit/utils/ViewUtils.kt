package com.polidea.cockpit.utils

import android.view.View
import android.view.ViewGroup
import com.polidea.cockpit.view.ParamView

object ViewUtils {
    fun getFlatChildren(parentView: View): ArrayList<ParamView<*>> {
        val children: ArrayList<ParamView<*>> = ArrayList()

        if (parentView is ViewGroup) {
            val childCount = parentView.childCount

            for (i in 0 until childCount) {
                children.add(parentView.getChildAt(i) as ParamView<*>)
            }
        }

        return children
    }
}