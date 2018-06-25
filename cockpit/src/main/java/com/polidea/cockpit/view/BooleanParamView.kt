package com.polidea.cockpit.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import com.polidea.cockpit.R
import kotlinx.android.synthetic.main.cockpit_boolean_param_line.view.*

@SuppressLint("ViewConstructor")
class BooleanParamView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
                       override val paramName: String, override val description: String?,
                       override val group: String?) : ParamView<Boolean>, LinearLayout(context, attrs, defStyleAttr) {

    override var value: Boolean = false
        get() {
            return (getValueView() as CheckBox).isChecked
        }
        set(value) {
            field = value
            cockpit_boolean_param_value.isChecked = value
        }

    override fun getValueView(): View = cockpit_boolean_param_value

    override fun getRestoreButton(): View = cockpit_boolean_param_restore_button

    init {
        LayoutInflater.from(context).inflate(R.layout.cockpit_boolean_param_line, this, true)
        cockpit_boolean_param_name.text = description ?: paramName
        cockpit_boolean_param_name.isSelected = true
    }
}