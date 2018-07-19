package com.polidea.cockpit.paramsedition.viewholder

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatEditText
import android.view.View
import com.polidea.cockpit.R
import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.type.CockpitColor
import com.polidea.cockpit.utils.TextWatcherAdapter
import java.lang.RuntimeException

internal class ColorParamViewHolder(view: View) : ParamBaseValueWithRestoreViewHolder<CockpitColor>(view) {

    private val value: AppCompatEditText = view.findViewById(R.id.cockpit_color_param_value)

    private val colorPreview: View = view.findViewById(R.id.cockpit_color_preview_view)

    init {
        value.addTextChangedListener(object : TextWatcherAdapter() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val colorValue = s?.toString() ?: ""
                try {
                    val color = Color.parseColor(colorValue)
                    valueChangeListener?.invoke(CockpitColor(colorValue))
                    showPreview(color)
                } catch (e: RuntimeException) {
                    showPreviewPlaceholder()
                }
            }
        })
    }

    override fun displayParam(param: CockpitParam<CockpitColor>) {
        super.displayParam(param)
        val colorValue = param.value.value
        value.setText(colorValue)
    }

    private fun showPreview(@ColorInt color: Int) {
        val drawable: GradientDrawable = ContextCompat.getDrawable(colorPreview.context, R.drawable.color_preview) as GradientDrawable
        drawable.mutate()
        drawable.setColor(color)
        colorPreview.background = drawable
    }

    private fun showPreviewPlaceholder() {
        colorPreview.background = ContextCompat.getDrawable(colorPreview.context, R.drawable.v_color_placeholder)
    }
}