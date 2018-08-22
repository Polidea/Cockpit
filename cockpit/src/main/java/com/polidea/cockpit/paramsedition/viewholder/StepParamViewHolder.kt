package com.polidea.cockpit.paramsedition.viewholder

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.polidea.cockpit.R
import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.type.CockpitStep

internal abstract class StepParamViewHolder<T : Number>(view: View) : ParamBaseValueWithRestoreViewHolder<CockpitStep<T>>(view) {

    protected abstract fun doubleToValue(double: Double): T
    private val value: TextView = view.findViewById(R.id.cockpit_step_param_value)
    private val incrementer: Button = view.findViewById(R.id.cockpit_step_param_increment)
    private val decrementer: Button = view.findViewById(R.id.cockpit_step_param_decrement)
    private lateinit var stepParam: CockpitStep<T>

    override fun displayParam(param: CockpitParam<CockpitStep<T>>) {
        super.displayParam(param)
        stepParam = param.value
        value.text = stepParam.value.toString()
        updateIncrementEnableability()
        updateDecrementEnableability()
        incrementer.setOnClickListener {
            updateParam(increment(stepParam))
        }
        decrementer.setOnClickListener {
            updateParam(decrement(stepParam))
        }
    }

    private fun updateParam(newStepParam: CockpitStep<T>) {
        value.text = newStepParam.value.toString()
        valueChangeListener?.invoke(newStepParam)
        stepParam = newStepParam
        updateIncrementEnableability()
        updateDecrementEnableability()
    }

    private fun updateIncrementEnableability() {
        stepParam.max?.let {
            incrementer.isEnabled = stepParam.value.toDouble() + stepParam.step.toDouble() <= it.toDouble()
        }
    }

    private fun updateDecrementEnableability() {
        stepParam.min?.let {
            decrementer.isEnabled = stepParam.value.toDouble() - stepParam.step.toDouble() >= it.toDouble()
        }
    }

    private fun increment(param: CockpitStep<T>): CockpitStep<T> {
        val newValue = param.value.toDouble() + param.step.toDouble()
        return param.copy(value = doubleToValue(newValue))
    }

    private fun decrement(param: CockpitStep<T>): CockpitStep<T> {
        val newValue = param.value.toDouble() - param.step.toDouble()
        return param.copy(value = doubleToValue(newValue))
    }
}