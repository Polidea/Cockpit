package com.polidea.cockpit.paramsedition.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.polidea.cockpit.R
import com.polidea.cockpit.core.CockpitParam

abstract class ParamBaseViewHolder<T : Any>(protected val view: View) : RecyclerView.ViewHolder(view) {

    var valueChangeListener: ((T) -> Unit)? = null

    var restoreClickListener: (() -> Unit)? = null

    val name: TextView = view.findViewById(R.id.cockpit_param_name)

    private val restore: ImageButton? = view.findViewById(R.id.cockpit_param_restore)

    init {
        restore?.setOnClickListener { restoreClickListener?.invoke() }
    }

    open fun displayParam(param: CockpitParam<T>) {
        name.text = param.description ?: param.name
    }
}