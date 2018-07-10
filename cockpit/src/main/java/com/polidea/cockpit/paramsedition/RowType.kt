package com.polidea.cockpit.paramsedition

import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.ParamType
import com.polidea.cockpit.exception.UnsupportedCockpitTypeException
import com.polidea.cockpit.utils.isTypeOf

enum class RowType {
    BOOL, INT, DOUBLE, STRING, ACTION;

    companion object {
        fun getRowType(param: CockpitParam<Any>) =
                when {
                    param.type == ParamType.ACTION -> ACTION
                    param.isTypeOf<Boolean>() -> BOOL
                    param.isTypeOf<Int>() -> INT
                    param.isTypeOf<Double>() -> DOUBLE
                    param.isTypeOf<String>() -> STRING
                    else -> throw UnsupportedCockpitTypeException(param.name, param.value.javaClass)
                }

        fun fromOrdinal(ordinal: Int) = values()[ordinal]
    }
}