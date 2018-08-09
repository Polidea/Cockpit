package com.polidea.cockpit.sample.model

import com.polidea.cockpit.sample.sampleparams.SampleBaseContract

class SampleModel : SampleBaseContract.Model {

    private var items: List<Item> = initialItems()

    companion object {
        private fun initialItems() = arrayListOf(
                Item(ModelConstants.ITEM_NAME_SHOES, 95.99, 1),
                Item(ModelConstants.ITEM_NAME_HAT, 4.99, 3),
                Item(ModelConstants.ITEM_NAME_BACKPACK, 40.45, 1))
    }

    override fun getItem(itemName: String): Item? {
        return items.find { it.name == itemName }?.copy()
    }

    override fun getItems(): List<Item> {
        return items.map { it.copy() }
    }

    override fun updateItem(item: Item) {
        items.find { it.name == item.name }?.let {
            it.count = item.count
        }
    }

    override fun getTotalPrice(): Double {
        return items.fold(0.0) { price, item ->
            price + item.count * item.price
        }
    }

    override fun reset() {
        items = initialItems()
    }
}