package com.polidea.cockpit.sample.shoppingcart

import android.content.Context
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import com.polidea.cockpit.sample.R
import kotlinx.android.synthetic.main.cart_item.view.*


class CartItemView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var itemName: String
        get() = cart_item_name.text.toString()
        set(value) {
            cart_item_name.text = value
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.cart_item, this)
    }

    fun setItemImageDrawable(@DrawableRes drawableResId: Int) {
        cart_item_image.setImageDrawable(getDrawable(drawableResId))
    }

    fun setBubbleImageDrawable(@DrawableRes drawableResId: Int) {
        item_count_bubble_background.background = getDrawable(drawableResId)
    }

    fun setNameFontColor(@ColorInt color: Int) {
        cart_item_name.setTextColor(color)
    }

    fun setCount(count: String) {
        item_count_number.text = count
    }

    fun setPrice(price: String) {
        cart_item_price.text = price
    }

    fun setOnPlusClickListener(listener: OnClickListener?) {
        add_button.setOnClickListener(listener)
    }

    fun setOnMinusClickListener(listener: OnClickListener?) {
        remove_button.setOnClickListener(listener)
    }
    private fun getDrawable(drawableResId: Int) = ContextCompat.getDrawable(context, drawableResId)
}