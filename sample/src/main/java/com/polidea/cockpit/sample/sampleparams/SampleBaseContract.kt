package com.polidea.cockpit.sample.sampleparams

import android.support.annotation.ColorInt
import com.polidea.cockpit.sample.BasePresenter
import com.polidea.cockpit.sample.BaseView
import com.polidea.cockpit.sample.Style
import com.polidea.cockpit.sample.model.Item

interface SampleBaseContract {

    interface View<T : Presenter> : BaseView<T> {
        fun setStyle(style: Style)

        fun showFooter(isVisible: Boolean)

        fun setFooterText(footerText: String)

        fun setFooterTextColor(@ColorInt color: Int)

        fun setTotalPriceFontSize(textSize: Float)

        fun setHeadingText(headingText: String)

        fun showInfoDialog()

        fun updateTotalPrice(price: String)

        fun updateItem(itemName: String, price: String, count: String)
    }

    interface Presenter : BasePresenter {
        fun infoClicked()
        fun checkoutClicked()
        fun plusClicked(itemName: String)
        fun minusClicked(itemName: String)
    }

    interface Model {
        fun getItem(itemName: String): Item?
        fun getItems(): List<Item>
        fun updateItem(item: Item)
        fun getTotalPrice(): Double
        fun reset()
    }


}