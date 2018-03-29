package com.polidea.cockpit.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import com.polidea.cockpit.R
import kotlinx.android.synthetic.main.cockpit_string_param_line.view.*

@SuppressLint("ViewConstructor")
class TextParamView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
                    override val paramName: String, override var value: String) : ParamView<String>, LinearLayout(context, attrs, defStyleAttr) {
    override fun getCurrentValue(): String {
        return (getValueView() as EditText).text.toString()
    }

    override fun getValueView(): View {
        return cockpit_string_param_value as View
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.cockpit_string_param_line, this, true)
        cockpit_string_param_value.setText(value)
        cockpit_string_param_name.text = paramName
    }
}