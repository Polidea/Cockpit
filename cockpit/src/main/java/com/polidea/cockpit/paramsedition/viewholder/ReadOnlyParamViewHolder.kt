package com.polidea.cockpit.paramsedition.viewholder

import android.view.View
import android.widget.TextView
import com.polidea.cockpit.R
import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.type.CockpitReadOnly

internal class ReadOnlyParamViewHolder(view: View) : ParamBaseValueViewHolder<CockpitReadOnly>(view) {

    private val value: TextView = view.findViewById(R.id.cockpit_read_only_param_value)

    override fun displayParam(param: CockpitParam<CockpitReadOnly>) {
        super.displayParam(param)
        value.text = param.value.text
    }
}