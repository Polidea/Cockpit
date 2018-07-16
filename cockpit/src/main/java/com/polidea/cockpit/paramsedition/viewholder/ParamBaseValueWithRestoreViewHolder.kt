package com.polidea.cockpit.paramsedition.viewholder

import android.view.View
import android.widget.ImageButton
import com.polidea.cockpit.R

abstract class ParamBaseValueWithRestoreViewHolder<T : Any>(view: View) : ParamBaseValueViewHolder<T>(view) {

    var restoreClickListener: (() -> Unit)? = null

    private val restore: ImageButton = view.findViewById(R.id.cockpit_param_restore)

    init {
        restore.setOnClickListener { restoreClickListener?.invoke() }
    }
}