package com.polidea.cockpit.sample

import android.view.View
import com.polidea.cockpit.cockpit.Cockpit

class MainActivity : VariantIndependentBaseMainActivity() {

    override fun initViews() {
        findViewById<View>(R.id.edit_values_button).setOnClickListener {
            Cockpit.showCockpit(this)
        }
    }
}