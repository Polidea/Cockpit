package com.polidea.cockpit.paramsedition

internal data class ItemPosition(val groupIndex: Int,
                                 val subgroupIndex: Int = PARAM_INDEX_FOR_GROUP_ITEM,
                                 val paramIndex: Int = PARAM_INDEX_FOR_GROUP_ITEM) {

    fun isSectionPosition() = paramIndex == PARAM_INDEX_FOR_GROUP_ITEM && subgroupIndex == PARAM_INDEX_FOR_GROUP_ITEM
    fun isGroupPosition() = subgroupIndex != PARAM_INDEX_FOR_GROUP_ITEM
            && paramIndex == PARAM_INDEX_FOR_GROUP_ITEM

    companion object {
        private const val PARAM_INDEX_FOR_GROUP_ITEM = -1
    }
}