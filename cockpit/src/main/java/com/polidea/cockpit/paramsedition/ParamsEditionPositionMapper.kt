package com.polidea.cockpit.paramsedition

internal class ParamsEditionPositionMapper(private val paramsModel: ParamsModel) {

    fun toItemPosition(adapterPosition: Int): ItemPosition {
        var tmpPosition = 0
        for (groupIndex in 0 until paramsModel.groupsSize) {
            if (adapterPosition == tmpPosition)
                return ItemPosition(groupIndex)

            tmpPosition++
            val groupSize = paramsModel.getGroupSize(groupIndex)
            if (adapterPosition >= tmpPosition && adapterPosition < tmpPosition + groupSize) {
                return ItemPosition(groupIndex, adapterPosition - tmpPosition)
            }
            tmpPosition += groupSize
        }

        throw IllegalArgumentException("No item position for $adapterPosition adapter position")
    }

    fun toAdapterPosition(itemPosition: ItemPosition): Int {

        var adapterPosition = itemPosition.groupIndex

        for (tmpGroupIndex in 0 until itemPosition.groupIndex)
            adapterPosition += paramsModel.getGroupSize(tmpGroupIndex)

        if (!itemPosition.isGroupPosition())
            adapterPosition += itemPosition.paramIndex + 1

        return adapterPosition
    }
}