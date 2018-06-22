package com.polidea.cockpit.sample

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import com.polidea.cockpit.cockpit.Cockpit
import com.polidea.cockpit.listener.CockpitParamChangeListener
import kotlinx.android.synthetic.main.activity_main.*

abstract class VariantIndependentBaseMainActivity : AppCompatActivity() {

    abstract fun initViews()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()

        Cockpit.setOnFontSizeChangeListener(
                object : CockpitParamChangeListener<Int> {
                    override fun onValueChange(oldValue: Int, newValue: Int) {
                        Toast.makeText(this@VariantIndependentBaseMainActivity,
                                "fontSize changed, old value = $oldValue, new value = $newValue",
                                Toast.LENGTH_SHORT).show()
                    }
                })
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

