package com.polidea.cockpit.paramsedition.viewholder

import androidx.appcompat.widget.SwitchCompat
import android.view.View
import com.polidea.cockpit.R
import com.polidea.cockpit.core.CockpitParam

internal class BooleanParamViewHolder(view: View) : ParamBaseValueViewHolder<Boolean>(view) {

    private val selectionState: SwitchCompat = view.findViewById(R.id.cockpit_boolean_param_value)

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