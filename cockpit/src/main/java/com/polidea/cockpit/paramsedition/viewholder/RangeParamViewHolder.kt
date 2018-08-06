package com.polidea.cockpit.paramsedition.viewholder

import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import com.polidea.cockpit.R
import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.type.CockpitRange
import com.polidea.cockpit.mapper.CockpitRangeMapper

internal abstract class RangeParamViewHolder<T : Number>(view: View) : ParamBaseValueWithRestoreViewHolder<CockpitRange<T>>(view) {

    protected abstract fun doubleToValue(double: Double): T
    private val value: TextView = view.findViewById(R.id.cockpit_range_param_value)
    private val valueSelector: SeekBar = view.findViewById(R.id.cockpit_range_param_seek_bar)
    private lateinit var seekBarProgressToRange: (Int) -> CockpitRange<T>
    private val cockpitRangeMapper = CockpitRangeMapper<T>()

    init {
        valueSelector.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val range = seekBarProgressToRange(progress)
                value.text = range.value.toString()
                valueChangeListener?.invoke(range)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    override fun displayParam(param: CockpitParam<CockpitRange<T>>) {
        super.displayParam(param)
        seekBarProgressToRange = seekBarProgressToRangeFunction(param.value)
        valueSelector.max = getStepsCount(param.value)
        valueSelector.progress = rangeToSeekBarProgress(param.value)
    }

    private fun getStepsCount(range: CockpitRange<T>): Int =
            Math.ceil((range.max.toDouble() - range.min.toDouble()) / range.step.toDouble()).toInt()

    private fun rangeToSeekBarProgress(range: CockpitRange<T>): Int =
            Math.ceil(range.value.toDouble() / range.step.toDouble()).toInt()

    private fun seekBarProgressToRangeFunction(range: CockpitRange<T>): (Int) -> CockpitRange<T> = { progress ->
        var newValue = range.min.toDouble() + progress.toDouble() * range.step.toDouble()
        if (newValue > range.max.toDouble())
            newValue = range.max.toDouble()
        cockpitRangeMapper.wrap(range, doubleToValue(newValue))
    }
}