package com.polidea.cockpit.paramsedition

import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.type.CockpitAction
import com.polidea.cockpit.exception.UnsupportedCockpitTypeException
import com.polidea.cockpit.utils.isTypeOf

enum class ParamType {
    BOOL, INT, DOUBLE, STRING, ACTION;

    companion object {
        fun getParamType(param: CockpitParam<Any>) =
                when {
                    param.isTypeOf<Boolean>() -> BOOL
                    param.isTypeOf<Int>() -> INT
                    param.isTypeOf<Double>() -> DOUBLE
                    param.isTypeOf<String>() -> STRING
                    param.isTypeOf<CockpitAction>() -> ACTION
                    else -> throw UnsupportedCockpitTypeException(param.name, param.value.javaClass)
                }

        fun fromOrdinal(ordinal: Int) = values()[ordinal]
    }
}