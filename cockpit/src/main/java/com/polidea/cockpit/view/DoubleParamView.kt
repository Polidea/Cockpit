package com.polidea.cockpit.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import com.polidea.cockpit.R
import com.polidea.cockpit.exception.CockpitFormatException
import kotlinx.android.synthetic.main.cockpit_number_param_line.view.*

@SuppressLint("ViewConstructor")
class DoubleParamView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
                      override val paramName: String, override var value: Double) : ParamView<Double>, LinearLayout(context, attrs, defStyleAttr) {
    override fun getCurrentValue(): Double {
        try {
            return (getValueView() as EditText).text.toString().toDouble()
        } catch (e: NumberFormatException) {
            throw CockpitFormatException()
        }
    }

    override fun getValueView(): View {
        return cockpit_number_param_value as View
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.cockpit_number_param_line, this, true)
        cockpit_number_param_value.setText(value.toString())
        cockpit_number_param_name.text = paramName
    }
}