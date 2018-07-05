package com.polidea.cockpit.paramsedition.viewholder

import android.view.View

internal class IntParamViewHolder(view: View) : NumberParamViewHolder<Int>(view) {
    override fun convertStringToNumber(stringValue: String?) = stringValue?.toIntOrNull() ?: 0
}