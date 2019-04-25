package com.polidea.cockpit.paramsedition

import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.CockpitParamGroup

internal interface ParamsModel {

    val topLevelGroups: Map<String?, CockpitParamGroup>

    fun <T : Any> getParam(paramName: String): CockpitParam<T>

    fun getGroup(groupName: String?): CockpitParamGroup

    fun <T : Any> setValue(paramName: String, newValue: T)

    fun <T : Any> selectValue(paramName: String, selectedItemIndex: Int)

    fun restoreValue(paramName: String)

    fun requestAction(paramName: String)

    fun restoreAll()

    fun save()
}