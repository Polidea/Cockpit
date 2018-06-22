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
                    override val paramName: String) : ParamView<String>, LinearLayout(context, attrs, defStyleAttr) {

    override var value: String = ""
        get() {
            return cockpit_string_param_value.text.toString()
        }
        set(value) {
            field = value
            cockpit_string_param_value.setText(value)
        }

    override fun getValueView(): View = cockpit_string_param_value

    override fun getRestoreButton(): View = cockpit_string_param_restore_button

    init {
        LayoutInflater.from(context).inflate(R.layout.cockpit_string_param_line, this, true)
        cockpit_string_param_name.text = paramName
        cockpit_string_param_name.isSelected = true
    }
}