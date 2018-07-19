package com.polidea.cockpit.paramsedition

import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.type.CockpitAction
import com.polidea.cockpit.core.type.CockpitColor
import com.polidea.cockpit.core.type.CockpitListType
import com.polidea.cockpit.exception.UnsupportedCockpitTypeException
import com.polidea.cockpit.extensions.isTypeOf

internal enum class ParamType {
    BOOL, INT, DOUBLE, STRING, LIST, ACTION, COLOR;

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
                    else -> throw UnsupportedCockpitTypeException(param.name, param.value::class.java)
                }
    }
}