package com.polidea.cockpit.paramsedition.viewholder

import android.view.View

internal class DoubleParamViewHolder(view: View) : NumberParamViewHolder<Double>(view) {
    override fun convertStringToNumber(stringValue: String?) = stringValue?.toDoubleOrNull() ?: .0
}