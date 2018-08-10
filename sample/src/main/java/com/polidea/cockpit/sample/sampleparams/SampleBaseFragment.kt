package com.polidea.cockpit.sample.sampleparams

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.polidea.cockpit.cockpit.Cockpit
import com.polidea.cockpit.sample.R
import com.polidea.cockpit.sample.Style
import com.polidea.cockpit.sample.model.ModelConstants
import com.polidea.cockpit.sample.shoppingcart.CartItemView
import kotlinx.android.synthetic.main.fragment_shopping_cart.*

abstract class SampleBaseFragment<T : SampleBaseContract.Presenter> : Fragment(), SampleBaseContract.View<T> {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_shopping_cart, container, false)

    override fun setStyle(style: Style) {
        styleWith(style)
    }

    private fun styleWith(style: Style) {
        context?.let { context ->
            colored_background_view.background = ContextCompat.getDrawable(context, style.backgroundDrawableResId)
            checkout_button.buttonBackground = ContextCompat.getDrawable(context, style.backgroundDrawableResId)
            checkout_button.successBackground = ContextCompat.getDrawable(context, style.successColorResId)
            setStatusBarColor(style.statusBarColorResId)
            listOf(shoes, hat, backpack).forEach {
                it.setNameFontColor(ContextCompat.getColor(context, style.itemNameTextColorResId))
                it.setBubbleImageDrawable(style.bubbleBackgroundDrawableResId)
            }
        }
    }

    private fun setStatusBarColor(statusBarColor: Int) {
        activity?.let {
            val window = it.window
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

            // finally change the color
            window.statusBarColor = ContextCompat.getColor(it, statusBarColor)
        }
    }

    override fun showFooter(isVisible: Boolean) {
        footer_container.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }

    override fun setFooterText(footerText: String) {
        footer_text_view.text = footerText
    }

    override fun setFooterTextColor(color: Int) {
        footer_text_view.setTextColor(color)
    }

    override fun setTotalPriceFontSize(textSize: Float) {
        total_price.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
    }

    override fun setHeadingText(headingText: String) {
        heading_text_view.text = headingText
    }

    override fun updateTotalPrice(price: String) {
        total_price.text = price
    }

    override fun updateItem(itemName: String, price: String, count: String) {
        when(itemName) {
            ModelConstants.ITEM_NAME_SHOES -> updateItem(shoes, itemName, price, count)
            ModelConstants.ITEM_NAME_HAT -> updateItem(hat, itemName, price, count)
            ModelConstants.ITEM_NAME_BACKPACK -> updateItem(backpack, itemName, price, count)
        }
    }

    private fun updateItem(view: CartItemView, name: String, price: String, count: String) {
        view.itemName = name
        view.setPrice(price)
        view.setCount(count)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.start()

        info.setOnClickListener { presenter.infoClicked() }
        checkout_button.setOnClickListener { presenter.checkoutClicked() }

        initializeCartItems()
    }

    private fun initializeCartItems() {
        shoes.setItemImageDrawable(R.drawable.shoes)
        shoes.setOnMinusClickListener(View.OnClickListener { presenter.minusClicked(ModelConstants.ITEM_NAME_SHOES) })
        shoes.setOnPlusClickListener(View.OnClickListener { presenter.plusClicked(ModelConstants.ITEM_NAME_SHOES) })

        hat.setItemImageDrawable(R.drawable.hat)
        hat.setOnMinusClickListener(View.OnClickListener { presenter.minusClicked(ModelConstants.ITEM_NAME_HAT) })
        hat.setOnPlusClickListener(View.OnClickListener { presenter.plusClicked(ModelConstants.ITEM_NAME_HAT) })

        backpack.setItemImageDrawable(R.drawable.backpack)
        backpack.setOnMinusClickListener(View.OnClickListener { presenter.minusClicked(ModelConstants.ITEM_NAME_BACKPACK) })
        backpack.setOnPlusClickListener(View.OnClickListener { presenter.plusClicked(ModelConstants.ITEM_NAME_BACKPACK) })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.stop()
    }

    override fun showInfoDialog() {
        context?.let {
            AlertDialog.Builder(it, Style.forValue(Cockpit.getStyleSelectedValue()).alertDialogStyleResId)
                    .setTitle(R.string.info_title)
                    .setMessage(R.string.info_message)
                    .setPositiveButton(android.R.string.ok) { _, _ -> }
                    .create()
                    .show()
        }
    }
}