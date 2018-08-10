package com.polidea.cockpit.sample.sampleparams

import android.graphics.Color
import com.polidea.cockpit.cockpit.Cockpit
import com.polidea.cockpit.sample.Style
import com.polidea.cockpit.sample.model.Item
import java.text.NumberFormat
import java.util.*

abstract class SampleBasePresenter(open val sampleView: SampleBaseContract.View<*>,
                                   open val sampleModel: SampleBaseContract.Model) : SampleBaseContract.Presenter {

    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)

    override fun start() {
        initViews()
    }

    override fun stop() {
    }

    protected open fun initViews() {
        sampleView.setStyle(Style.forValue(Cockpit.getStyleSelectedValue()))
        sampleView.setTotalPriceFontSize(Cockpit.getTotalPriceFontSize().toFloat())
        sampleView.setHeadingText(Cockpit.getHeadingText())
        sampleView.setFooterText(Cockpit.getFooter())
        sampleView.setFooterTextColor(Color.parseColor(Cockpit.getFooterFontColor()))
        sampleView.showFooter(true)

        updateAll()
    }

    override fun infoClicked() {
        sampleView.showInfoDialog()
    }

    override fun checkoutClicked() {
        sampleModel.reset()
        updateAll()
    }

    protected fun updateAll() {
        sampleModel.getItems().forEach {
            sampleView.updateItem(it.name, currencyFormat.format(it.price), it.count.toString())
        }
        sampleView.updateTotalPrice(currencyFormat.format(sampleModel.getTotalPrice()))
    }

    override fun plusClicked(itemName: String) {
        updateItem(itemName) { item -> ++item.count }
    }

    override fun minusClicked(itemName: String) {
        updateItem(itemName) { item -> item.count = Math.max(item.count - 1, 0) }
    }

    private fun updateItem(itemName: String, operation: (Item) -> Unit) {
        val item = sampleModel.getItem(itemName) ?: return
        operation(item)
        sampleModel.updateItem(item)
        sampleView.updateItem(itemName, currencyFormat.format(item.price), item.count.toString())
        sampleView.updateTotalPrice(currencyFormat.format(sampleModel.getTotalPrice()))
    }
}