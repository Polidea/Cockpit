package com.polidea.cockpit.paramsedition

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import com.polidea.cockpit.R
import com.polidea.cockpit.extensions.removeDimmedBackground
import com.polidea.cockpit.paramsedition.layout.CockpitLayout

internal class CockpitDialog internal constructor() : AppCompatDialogFragment(), ParamsEditionContract.View {

    override lateinit var presenter: ParamsEditionContract.Presenter
    private lateinit var paramsEditionAdapter: ParamsEditionAdapter
    private lateinit var expandCollapse: ImageButton
    private lateinit var cockpitRoot: CockpitLayout
    private lateinit var actionBar: LinearLayout
    private lateinit var bottomButton: Button
    private var expanded = true

    override fun onStart() {
        super.onStart()
        removeDimmedBackground()
        dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun showColorPicker(itemPosition: ItemPosition, color: Int) {
        val colorPicker = ColorPickerDialog.newBuilder()
                .setDialogType(ColorPickerDialog.TYPE_CUSTOM)
                .setAllowPresets(false)
                .setColor(color)
                .setShowAlphaSlider(true)
                .create()
        colorPicker.setColorPickerDialogListener(object : ColorPickerDialogListener {
            override fun onDialogDismissed(dialogId: Int) {
            }

            override fun onColorSelected(dialogId: Int, color: Int) {
                presenter.newColorSelected(itemPosition, color)
            }
        })
        activity?.fragmentManager?.let {
            if (it.findFragmentByTag(colorPickerFragmentTag) == null)
                colorPicker.show(it, colorPickerFragmentTag)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val view = View.inflate(context, R.layout.dialog_params_edition, null)
        dialog.setContentView(view)
        setupViews(view)
        presenter.start()
        retainInstance = true

        return dialog
    }

    private fun animateExpandCollapseIcon(expanded: Boolean) {
        if (expanded)
            expandCollapse.animate().rotation(0f)
        else
            expandCollapse.animate().rotation(expandCollapseIconRotationDegrees)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupViews(view: View) {
        paramsEditionAdapter = ParamsEditionAdapter(presenter)
        view.findViewById<RecyclerView>(R.id.params_list).adapter = paramsEditionAdapter
        view.findViewById<ImageButton>(R.id.restore_defaults).setOnClickListener { presenter.restoreAll() }
        expandCollapse = view.findViewById(R.id.expand_collapse)
        expandCollapse.setOnClickListener {
            if (expanded)
                presenter.collapse()
            else
                presenter.expand()
        }

        actionBar = view.findViewById(R.id.action_bar)
        actionBar.setOnTouchListener { _, ev ->
            if (ev.action == MotionEvent.ACTION_DOWN) {
                cockpitRoot.startDrag = true
                true
            }
            false
        }

        bottomButton = view.findViewById(R.id.resize_handle)
        bottomButton.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_MOVE) {
                presenter.resize(bottomButton.y.toInt() + motionEvent.y.toInt())
                true
            }
            false
        }


        cockpitRoot = view.findViewById(R.id.cockpit_root)
        cockpitRoot.setDraggableView(R.id.cockpit_content)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.stop()
    }

    override fun reloadParam(itemPosition: ItemPosition) {
        paramsEditionAdapter.reloadParam(itemPosition)
    }

    override fun reloadAll() {
        paramsEditionAdapter.reloadAll()
    }

    override fun expand() {
        animateExpandCollapseIcon(true)
        cockpitRoot.expand()
        expanded = true
    }

    override fun collapse() {
        animateExpandCollapseIcon(false)
        cockpitRoot.collapse()
        expanded = false
    }

    override fun resize(height: Int) {
        cockpitRoot.resize(height)
    }

    companion object {
        private const val colorPickerFragmentTag = "CockpitColorPicker"
        private const val expandCollapseIconRotationDegrees = 180f

        fun newInstance(): CockpitDialog {
            val instance = CockpitDialog()
            instance.setStyle(AppCompatDialogFragment.STYLE_NORMAL, android.R.style.Theme_Panel)
            CockpitDialogPresenter(instance)
            return instance
        }
    }
}