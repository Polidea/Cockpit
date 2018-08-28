package com.polidea.cockpit.paramsedition.viewholder

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.polidea.cockpit.R
import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.type.CockpitColor
import com.polidea.cockpit.paramsedition.view.ColorPreviewView
import com.polidea.cockpit.utils.TextWatcherAdapter
import java.lang.RuntimeException

internal class ColorParamViewHolder(view: View) : ParamBaseValueWithRestoreViewHolder<CockpitColor>(view) {

    private val value: TextView = view.findViewById(R.id.cockpit_color_param_value)

    private val colorPreview: ColorPreviewView = view.findViewById(R.id.cockpit_color_preview_view)

    var onColorEditionRequestListener: (() -> Unit)? = null

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
        view.setOnClickListener { onColorEditionRequestListener?.invoke() }
        value.text = param.value.value
    }
}