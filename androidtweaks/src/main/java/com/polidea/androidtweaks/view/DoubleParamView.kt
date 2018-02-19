package com.polidea.androidtweaks.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import com.polidea.androidtweaks.R
import com.polidea.androidtweaks.exception.TweakFormatException
import kotlinx.android.synthetic.main.number_param_line.view.*

@SuppressLint("ViewConstructor")
class DoubleParamView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
                      override val paramName: String, override var value: Double) : ParamView<Double>, LinearLayout(context, attrs, defStyleAttr) {
    override fun getCurrentValue(): Double {
        try {
            return (getValueView() as EditText).text.toString().toDouble()
        } catch (e: NumberFormatException) {
            throw TweakFormatException()
        }
    }

    override fun getValueView(): View {
        return number_param_value as View
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.number_param_line, this, true)
        number_param_value.setText(value.toString())
        number_param_name.text = paramName
    }
}