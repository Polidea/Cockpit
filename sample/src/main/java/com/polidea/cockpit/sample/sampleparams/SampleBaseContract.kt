package com.polidea.cockpit.sample.sampleparams

import android.support.annotation.ColorInt
import com.polidea.cockpit.sample.BasePresenter
import com.polidea.cockpit.sample.BaseView

interface SampleBaseContract {

    interface View<T : Presenter> : BaseView<T> {
        fun showFooter(isVisible: Boolean)

        fun setFooterText(footerText: String)

        fun setFooterTextColor(@ColorInt color: Int)

        fun setTotalPriceFontSize(textSize: Float)
        fun setHeadingText(headingText: String)
    }

    interface Presenter : BasePresenter
}