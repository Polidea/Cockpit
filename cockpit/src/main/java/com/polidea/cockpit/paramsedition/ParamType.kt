package com.polidea.cockpit.paramsedition

import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.type.*
import com.polidea.cockpit.exception.UnsupportedCockpitTypeException
import com.polidea.cockpit.extensions.isTypeOf

internal enum class ParamType {
    BOOL, INT, DOUBLE, STRING, LIST, ACTION, COLOR, RANGE_INT, RANGE_DOUBLE, READ_ONLY, STEP_INT, STEP_DOUBLE;

    companion object {
        fun getParamType(param: CockpitParam<Any>) =
                when {
                    param.isTypeOf<Boolean>() -> BOOL
                    param.isTypeOf<Int>() -> INT
                    param.isTypeOf<Double>() -> DOUBLE
                    param.isTypeOf<String>() -> STRING
                    param.isTypeOf<CockpitListType<Any>>() -> LIST
                    param.isTypeOf<CockpitAction>() -> ACTION
                    param.isTypeOf<CockpitColor>() -> COLOR
                    param.isTypeOf<CockpitRange<*>>() -> {
                        val range = param.value as CockpitRange<*>
                        if (range.value is Int)
                            RANGE_INT
                        else
                            RANGE_DOUBLE
                    }
                    param.isTypeOf<CockpitStep<*>>() -> {
                        val step = param.value as CockpitStep<*>
                        if (step.value is Int)
                            STEP_INT
                        else
                            STEP_DOUBLE
                    }
                    param.isTypeOf<CockpitReadOnly>() -> READ_ONLY
                    else -> throw UnsupportedCockpitTypeException(param.name, param.value::class.java)
                }
    }
}