package com.polidea.cockpit.paramsedition.viewholder

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.polidea.cockpit.R
import com.polidea.cockpit.core.CockpitParam

class StringParamViewHolder(view: View) : ParamBaseViewHolder<String>(view) {

    private val value: EditText = view.findViewById(R.id.cockpit_string_param_value)

    init {
        value.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

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