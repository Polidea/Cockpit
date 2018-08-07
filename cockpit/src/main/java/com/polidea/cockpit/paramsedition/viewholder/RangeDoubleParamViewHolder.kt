package com.polidea.cockpit.paramsedition.viewholder

import android.view.View

internal class RangeDoubleParamViewHolder(view: View) : RangeParamViewHolder<Double>(view) {

    override fun doubleToValue(double: Double) = double
}