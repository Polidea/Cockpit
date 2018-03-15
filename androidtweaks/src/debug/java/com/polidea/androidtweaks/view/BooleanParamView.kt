package com.polidea.androidtweaks.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import com.polidea.androidtweaks.R
import kotlinx.android.synthetic.debug.boolean_param_line.view.*

@SuppressLint("ViewConstructor")
class BooleanParamView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
                       override val paramName: String, override var value: Boolean) : ParamView<Boolean>, LinearLayout(context, attrs, defStyleAttr) {
    override fun getCurrentValue(): Boolean {
        return (getValueView() as CheckBox).isChecked
    }

    override fun getValueView(): View {
        return boolean_param_value as View
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.boolean_param_line, this,true)
        boolean_param_value.isChecked = value
        boolean_param_name.text = paramName
    }
}