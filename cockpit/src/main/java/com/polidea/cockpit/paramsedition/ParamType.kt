package com.polidea.cockpit.paramsedition

import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.utils.isTypeOf

enum class ParamType {
    BOOL, INT, DOUBLE, STRING;

    companion object {
        fun getParamType(param: CockpitParam<Any>) =
                when {
                    param.isTypeOf<Boolean>() -> BOOL
                    param.isTypeOf<Int>() -> INT
                    param.isTypeOf<Double>() -> DOUBLE
                    param.isTypeOf<String>() -> STRING
                    else -> STRING
                }

        fun fromOrdinal(ordinal: Int) = values()[ordinal]
    }
}