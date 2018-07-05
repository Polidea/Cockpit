package com.polidea.cockpit.sample.sampleparams

import android.graphics.Typeface
import android.support.annotation.ColorInt
import com.polidea.cockpit.sample.BasePresenter
import com.polidea.cockpit.sample.BaseView

interface SampleBaseContract {

    interface View<T : Presenter> : BaseView<T> {
        fun showFooter(isVisible: Boolean)

        fun setFooterText(footerText: String)

        fun setTextColor(@ColorInt color: Int)

        fun setFontSize(textSize: Float)

        fun setColorDescription(description: String)

        fun setTypeface(typeface: Typeface)
    }

    interface Presenter : BasePresenter
}