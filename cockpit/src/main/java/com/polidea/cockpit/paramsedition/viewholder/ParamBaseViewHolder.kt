package com.polidea.cockpit.paramsedition.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.polidea.cockpit.R
import com.polidea.cockpit.core.CockpitParam

internal abstract class ParamBaseViewHolder<T : Any>(protected val view: View) : RecyclerView.ViewHolder(view) {

    val name: TextView = view.findViewById(R.id.cockpit_param_name)

    open fun displayParam(param: CockpitParam<T>) {
        name.text = param.description ?: param.name
    }
}