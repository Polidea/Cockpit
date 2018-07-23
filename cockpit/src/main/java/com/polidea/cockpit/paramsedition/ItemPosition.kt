package com.polidea.cockpit.paramsedition

internal data class ItemPosition(val groupIndex: Int, val paramIndex: Int = PARAM_INDEX_FOR_GROUP_ITEM) {

    fun isGroupPosition() = paramIndex == PARAM_INDEX_FOR_GROUP_ITEM

    companion object {
        private const val PARAM_INDEX_FOR_GROUP_ITEM = -1
    }
}