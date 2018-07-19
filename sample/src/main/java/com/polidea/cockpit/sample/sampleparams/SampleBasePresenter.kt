package com.polidea.cockpit.sample.sampleparams

import android.graphics.Typeface
import com.polidea.cockpit.cockpit.Cockpit

abstract class SampleBasePresenter(open val sampleView: SampleBaseContract.View<*>) : SampleBaseContract.Presenter {

    override fun start() {
        initViews()
    }

    override fun stop() {
    }

    protected open fun initViews() {
        sampleView.setColorDescription(Cockpit.getColorDescription())
        sampleView.setFooterText(Cockpit.getFooter())
        sampleView.setFontSize(Cockpit.getFontSize().toFloat())
        sampleView.showFooter(true)
        sampleView.setTypeface(Typeface.create(Cockpit.getFontListSelectedValue(), Typeface.NORMAL))
        sampleView.setTextColor(Cockpit.getColor().color)
    }
}