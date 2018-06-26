package com.polidea.cockpit.sample

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import com.polidea.cockpit.cockpit.Cockpit
import com.polidea.cockpit.event.PropertyChangeListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : VariantIndependentBaseMainActivity() {

    private lateinit var onColorChangeListener: PropertyChangeListener<String>
    private lateinit var onFontSizeChangeListener: PropertyChangeListener<Int>
    private lateinit var onColorDescriptionChangeListener: PropertyChangeListener<String>
    private lateinit var onFooterChangeListener: PropertyChangeListener<String>
    private lateinit var onShowFooterChangeListener: PropertyChangeListener<Boolean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setOnChangeListeners()

        findViewById<View>(R.id.edit_values_button).setOnClickListener {
            Cockpit.showCockpit(this)
        }
    }

    override fun onDestroy() {
        removeOnChangeListeners()
        super.onDestroy()
    }

    private fun setOnChangeListeners() {

        onColorChangeListener = PropertyChangeListener { _, newColor ->
            val color = Color.parseColor(newColor)
            cockpit_textview.setTextColor(color)
            cockpit_color_textview.setTextColor(color)
        }
        Cockpit.addOnColorChangeListener(onColorChangeListener)

        onFontSizeChangeListener = PropertyChangeListener { _, newSize ->
            cockpit_textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, newSize.toFloat())
        }
        Cockpit.addOnFontSizeChangeListener(onFontSizeChangeListener)

        onColorDescriptionChangeListener = PropertyChangeListener { _, newColorDescription ->
            cockpit_color_textview.text = newColorDescription
        }
        Cockpit.addOnColorDescriptionChangeListener(onColorDescriptionChangeListener)

        onFooterChangeListener = PropertyChangeListener { _, newFooter ->
            footer_text_view.text = newFooter
        }
        Cockpit.addOnFooterChangeListener(onFooterChangeListener)

        onShowFooterChangeListener = PropertyChangeListener { _, isFooterVisible ->
            footer_container.visibility = if (isFooterVisible) View.VISIBLE else View.INVISIBLE
        }
        Cockpit.addOnShowFooterChangeListener(onShowFooterChangeListener)
    }

    private fun removeOnChangeListeners() {
        Cockpit.removeOnColorChangeListener(onColorChangeListener)
        Cockpit.removeOnFontSizeChangeListener(onFontSizeChangeListener)
        Cockpit.removeOnColorDescriptionChangeListener(onColorDescriptionChangeListener)
        Cockpit.removeOnFooterChangeListener(onFooterChangeListener)
        Cockpit.removeOnShowFooterChangeListener(onShowFooterChangeListener)
    }
}