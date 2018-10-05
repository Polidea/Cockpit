package com.polidea.cockpit.paramsedition.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.polidea.cockpit.R
import com.polidea.cockpit.core.CockpitParam

internal abstract class ParamBaseViewHolder<T : Any>(protected val view: View) : RecyclerView.ViewHolder(view) {

    val name: TextView = view.findViewById(R.id.cockpit_param_name)

    open fun displayParam(param: CockpitParam<T>) {
        name.text = param.description ?: param.name
    }
}