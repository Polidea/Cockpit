package com.polidea.cockpit.sample

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.View
import com.polidea.cockpit.cockpit.Cockpit
import com.polidea.cockpit.sample.util.initViews
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    override fun onResume() {
        super.onResume()
        displayMainActivity()
    }

    private fun displayMainActivity() {
        cockpit_textview.setTextColor(Color.parseColor(Cockpit.getColor()))
        cockpit_textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, Cockpit.getFontSize().toFloat())
        cockpit_color_textview.text = Cockpit.getColorDescription()
        cockpit_color_textview.setTextColor(Color.parseColor(Cockpit.getColor()))
        footer_text_view.text = Cockpit.getFooter()
        if (Cockpit.getShowFooter()) {
            footer_container.visibility = View.VISIBLE
        } else {
            footer_container.visibility = View.INVISIBLE
        }
    }
}

