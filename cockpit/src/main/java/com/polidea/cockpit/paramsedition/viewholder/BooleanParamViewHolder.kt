package com.polidea.cockpit.paramsedition.viewholder

import android.view.View
import android.widget.CheckBox
import com.polidea.cockpit.R
import com.polidea.cockpit.core.CockpitParam

class BooleanParamViewHolder(view: View) : ParamBaseViewHolder<Boolean>(view) {

    private val selectionState: CheckBox = view.findViewById(R.id.cockpit_boolean_param_value)

    init {
        selectionState.setOnCheckedChangeListener { _, isChecked ->
            valueChangeListener?.invoke(isChecked)
        }
    }

    override fun displayParam(param: CockpitParam<Boolean>) {
        super.displayParam(param)
        selectionState.isChecked = param.value
    }
}