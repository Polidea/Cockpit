package com.polidea.cockpit.paramsedition

import com.polidea.cockpit.core.CockpitParam

internal interface ParamsModel {

    val displaySize: Int

    val paramsSize: Int

    val groupsSize: Int

    val groupNames: List<String?>

    fun getGroupName(groupIndex: Int): String?

    fun getGroupSize(groupIndex: Int): Int

    fun <T : Any> getParamAt(itemPosition: ItemPosition): CockpitParam<T>
}