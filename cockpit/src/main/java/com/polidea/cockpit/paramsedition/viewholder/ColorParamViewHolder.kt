package com.polidea.cockpit.paramsedition.viewholder

import android.graphics.Color
import android.support.v7.widget.AppCompatEditText
import android.view.View
import com.polidea.cockpit.R
import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.type.CockpitColor
import com.polidea.cockpit.paramsedition.view.ColorPreviewView
import com.polidea.cockpit.utils.TextWatcherAdapter
import java.lang.RuntimeException

internal class ColorParamViewHolder(view: View) : ParamBaseValueWithRestoreViewHolder<CockpitColor>(view) {

    private val value: AppCompatEditText = view.findViewById(R.id.cockpit_color_param_value)

    private val colorPreview: ColorPreviewView = view.findViewById(R.id.cockpit_color_preview_view)

    init {
        value.addTextChangedListener(object : TextWatcherAdapter() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val colorValue = s?.toString() ?: ""
                try {
                    val color = Color.parseColor(colorValue)
                    valueChangeListener?.invoke(CockpitColor(colorValue))
                    colorPreview.showColor(color)
                } catch (e: RuntimeException) {
                    colorPreview.showPlaceholder()
                }
            }
        })
    }

    override fun displayParam(param: CockpitParam<CockpitColor>) {
        super.displayParam(param)
        val colorValue = param.value.value
        value.setText(colorValue)
    }
}