package com.polidea.cockpit.sample.sampleparams

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.polidea.cockpit.sample.R
import com.polidea.cockpit.sample.Style
import kotlinx.android.synthetic.main.fragment_shopping_cart.*

abstract class SampleBaseFragment<T : SampleBaseContract.Presenter> : Fragment(), SampleBaseContract.View<T> {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_shopping_cart, container, false)

    override fun setStyle(style: Style) {
        styleWith(style)
    }

    private fun styleWith(style: Style) {
        context?.let {
            colored_background_view.background = ContextCompat.getDrawable(it, style.backgroundDrawableResId)
            checkout_button.background = ContextCompat.getDrawable(it, style.backgroundDrawableResId)
            setStatusBarColor(style.statusBarColorResId)
        }
    }

    private fun setStatusBarColor(statusBarColor: Int) {
        activity?.let {
            val window = it.window
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.stop()
    }
}