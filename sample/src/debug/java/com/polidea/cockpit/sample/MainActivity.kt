package com.polidea.cockpit.sample

import android.view.View
import android.widget.Toast
import com.polidea.cockpit.cockpit.Cockpit
import com.polidea.cockpit.event.PropertyChangeListener

class MainActivity : VariantIndependentBaseMainActivity() {

    override fun initViews() {
        findViewById<View>(R.id.edit_values_button).setOnClickListener {
            Cockpit.showCockpit(this)
        }

        Cockpit.addOnFontSizeChangeListener(
                object : PropertyChangeListener<Int> {
                    override fun onValueChange(oldValue: Int, newValue: Int) {
                        Toast.makeText(this@MainActivity,
                                "fontSize changed, old value = $oldValue, new value = $newValue",
                                Toast.LENGTH_SHORT).show()
                    }
                })
    }
}