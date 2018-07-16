package com.polidea.cockpit.paramsedition.viewholder

import android.support.v7.widget.AppCompatEditText
import android.view.View
import com.polidea.cockpit.R
import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.utils.TextWatcherAdapter

class StringParamViewHolder(view: View) : ParamBaseValueWithRestoreViewHolder<String>(view) {

    private val value: AppCompatEditText = view.findViewById(R.id.cockpit_string_param_value)

    init {
        value.addTextChangedListener(object : TextWatcherAdapter() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                valueChangeListener?.invoke(s?.toString() ?: "")
            }
        })
    }

    override fun displayParam(param: CockpitParam<String>) {
        super.displayParam(param)
        value.setText(param.value)
    }
}