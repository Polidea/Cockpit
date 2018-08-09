package com.polidea.cockpit.sample.shoppingcart

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.polidea.cockpit.sample.R


class CartItem : Fragment() {
    private var itemName: String = ""
    private var itemPrice: Double = 0.0
    private var imageResource: Int = R.drawable.shoes

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.cart_item, container, false)

    companion object {
        fun newInstance(itemName: String, itemPrice: Double, imageResource: Int): CartItem {
            val instance = CartItem()
            instance.itemName = itemName
            instance.itemPrice = itemPrice
            instance.imageResource = imageResource
            return instance
        }
    }
}