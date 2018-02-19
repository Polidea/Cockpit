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
class IntParamView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
                   override val paramName: String, override var value: Int) : ParamView<Int>, LinearLayout(context, attrs, defStyleAttr) {
    override fun getCurrentValue(): Int {
        try {
            return (getValueView() as EditText).text.toString().toInt()
        } catch (e: NumberFormatException) {
            throw TweakFormatException()
        }
    }

    override fun getValueView(): View {
        return number_param_value as View
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.number_param_line, this, true)
        (getValueView() as EditText).setText(value.toString())
        number_param_name.text = paramName
    }
}