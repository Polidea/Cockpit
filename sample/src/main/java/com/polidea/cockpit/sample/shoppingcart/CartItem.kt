package com.polidea.cockpit.sample.shoppingcart

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import com.polidea.cockpit.sample.R
import kotlinx.android.synthetic.main.cart_item.view.*
import java.text.NumberFormat
import java.util.*


class CartItem @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var itemName: String
        get() = cart_item_name.text.toString()
        set(value) {
            cart_item_name.text = value
        }

    var itemPrice: Double = 0.0
        set(value) {
            val numberFormat = NumberFormat.getCurrencyInstance(Locale.US)
            cart_item_price.text = numberFormat.format(value)
        }

    var imageResource: Int = R.drawable.shoes
        set(value) {
            cart_item_image.setImageDrawable(ContextCompat.getDrawable(context, value))
        }

    var nameFontColor: Int
        get() = cart_item_name.currentTextColor
        set(value) {
            cart_item_name.setTextColor(value)
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.cart_item, this)
    }
}