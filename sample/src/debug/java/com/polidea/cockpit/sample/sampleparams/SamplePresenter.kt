package com.polidea.cockpit.sample.sampleparams

import android.graphics.Color
import android.graphics.Typeface
import com.polidea.cockpit.cockpit.Cockpit
import com.polidea.cockpit.event.ActionRequestCallback
import com.polidea.cockpit.event.PropertyChangeListener
import com.polidea.cockpit.event.SelectionChangeListener
import com.polidea.cockpit.sample.BuildConfig

class SamplePresenter(override val sampleView: SampleContract.View)
    : SampleBasePresenter(sampleView), SampleContract.Presenter {

    private lateinit var onColorChangeListener: PropertyChangeListener<String>
    private lateinit var onFontSizeChangeListener: PropertyChangeListener<Int>
    private lateinit var onColorDescriptionChangeListener: PropertyChangeListener<String>
    private lateinit var onFooterChangeListener: PropertyChangeListener<String>
    private lateinit var onShowFooterChangeListener: PropertyChangeListener<Boolean>
    private lateinit var onDebugDescriptionChangeListener: PropertyChangeListener<String>
    private lateinit var versionActionRequestCallback: ActionRequestCallback
    private lateinit var fontSelectedListener: SelectionChangeListener<String>

    init {
        sampleView.presenter = this
    }

    override fun start() {
        super.start()
        setOnChangeListeners()
        setCallbacks()
        setOnValueSelectedListener()
    }

    private fun setCallbacks() {
        versionActionRequestCallback = ActionRequestCallback { sampleView.showMessage(BuildConfig.VERSION_NAME) }
        Cockpit.addVersionActionRequestCallback(versionActionRequestCallback)
    }

    override fun stop() {
        removeOnChangeListeners()
        removeCallbacks()
        removeOnValueSelectedListeners()
    }

    private fun removeCallbacks() {
        Cockpit.removeVersionActionRequestCallback(versionActionRequestCallback)
    }

    private fun removeOnValueSelectedListeners() {
        Cockpit.removeFontListSelectionChangeListener(fontSelectedListener)
    }

    override fun shakeDetected() {
        sampleView.showCockpitUi()
    }

    override fun initViews() {
        super.initViews()
        sampleView.setDebugDescription(Cockpit.getDebugDescription())
    }

    private fun setOnValueSelectedListener() {
        fontSelectedListener = SelectionChangeListener { selectedValue ->
            sampleView.setTypeface(Typeface.create(selectedValue, Typeface.NORMAL))
        }
        Cockpit.addFontListSelectionChangeListener(fontSelectedListener)
    }

    private fun setOnChangeListeners() {
        onColorChangeListener = PropertyChangeListener { _, newColor ->
            sampleView.setTextColor(Color.parseColor(newColor))
        }
        Cockpit.addOnColorChangeListener(onColorChangeListener)

        onFontSizeChangeListener = PropertyChangeListener { _, newSize ->
            sampleView.setFontSize(newSize.toFloat())
        }
        Cockpit.addOnFontSizeChangeListener(onFontSizeChangeListener)

        onColorDescriptionChangeListener = PropertyChangeListener { _, newColorDescription ->
            sampleView.setColorDescription(newColorDescription)
        }
        Cockpit.addOnColorDescriptionChangeListener(onColorDescriptionChangeListener)

        onFooterChangeListener = PropertyChangeListener { _, newFooter ->
            sampleView.setFooterText(newFooter)
        }
        Cockpit.addOnFooterChangeListener(onFooterChangeListener)

        onShowFooterChangeListener = PropertyChangeListener { _, isFooterVisible ->
            sampleView.showFooter(isFooterVisible)
        }
        Cockpit.addOnShowFooterChangeListener(onShowFooterChangeListener)

        onDebugDescriptionChangeListener = PropertyChangeListener { _, newDescription ->
            sampleView.setDebugDescription(newDescription)
        }
        Cockpit.addOnDebugDescriptionChangeListener(onDebugDescriptionChangeListener)
    }

    private fun removeOnChangeListeners() {
        Cockpit.removeOnColorChangeListener(onColorChangeListener)
        Cockpit.removeOnFontSizeChangeListener(onFontSizeChangeListener)
        Cockpit.removeOnColorDescriptionChangeListener(onColorDescriptionChangeListener)
        Cockpit.removeOnFooterChangeListener(onFooterChangeListener)
        Cockpit.removeOnShowFooterChangeListener(onShowFooterChangeListener)
        Cockpit.removeOnDebugDescriptionChangeListener(onDebugDescriptionChangeListener)
    }
}