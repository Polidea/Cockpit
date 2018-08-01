package com.polidea.cockpit.paramsedition

import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.type.*
import com.polidea.cockpit.exception.UnsupportedCockpitTypeException
import com.polidea.cockpit.extensions.isTypeOf

internal enum class ParamType {
    BOOL, INT, DOUBLE, STRING, LIST, ACTION, COLOR, RANGE_INT, RANGE_DOUBLE;

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
                    param.isTypeOf<CockpitRange<Int>>() -> RANGE_INT
                    param.isTypeOf<CockpitRange<Double>>() -> RANGE_DOUBLE
                    else -> throw UnsupportedCockpitTypeException(param.name, param.value::class.java)
                }
    }
}