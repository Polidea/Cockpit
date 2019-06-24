package com.polidea.cockpit.paramsedition

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import com.polidea.cockpit.R
import com.polidea.cockpit.extensions.removeDimmedBackground
import com.polidea.cockpit.paramsedition.layout.CockpitLayout

internal class CockpitDialog internal constructor() : AppCompatDialogFragment(), ParamsEditionContract.View {

    override lateinit var presenter: ParamsEditionContract.Presenter
    private var alertDialog: AlertDialog? = null
    private lateinit var paramsEditionAdapter: ParamsEditionAdapter
    private lateinit var expandCollapse: ImageButton
    private lateinit var navigateBack: ImageButton
    private lateinit var navigateTop: ImageButton
    private lateinit var label: TextView
    private lateinit var cockpitRoot: CockpitLayout
    private lateinit var cockpitContent: LinearLayout
    private lateinit var actionBar: LinearLayout
    private lateinit var bottomButton: ImageButton
    private var expanded = true

    override fun onStart() {
        super.onStart()
        removeDimmedBackground()
        dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun showColorPicker(paramName: String, color: Int) {
        val colorPicker = ColorPickerDialog.newBuilder()
                .setDialogType(ColorPickerDialog.TYPE_CUSTOM)
                .setAllowPresets(false)
                .setColor(color)
                .setShowAlphaSlider(true)
                .setDialogStyle(R.style.ColorPickerDialogStyle)
                .create()
        colorPicker.setColorPickerDialogListener(object : ColorPickerDialogListener {
            override fun onDialogDismissed(dialogId: Int) {
            }

            override fun onColorSelected(dialogId: Int, color: Int) {
                presenter.newColorSelected(paramName, color)
            }
        })
        activity?.supportFragmentManager?.let {
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
        paramsEditionAdapter = ParamsEditionAdapter(DisplayModel(listOf()), presenter)
        view.findViewById<RecyclerView>(R.id.params_list).adapter = paramsEditionAdapter
        view.findViewById<ImageButton>(R.id.restore_defaults).setOnClickListener { presenter.restoreAll() }
        expandCollapse = view.findViewById(R.id.expand_collapse)
        expandCollapse.setOnClickListener {
            if (expanded) {
                presenter.collapse()
            } else {
                presenter.expand()
            }
        }

        navigateBack = view.findViewById(R.id.go_back)
        navigateBack.setOnClickListener { presenter.onNavigateBack() }

        navigateTop = view.findViewById(R.id.go_top)
        navigateTop.setOnClickListener { presenter.onNavigateToTop() }

        label = view.findViewById(R.id.label)

        actionBar = view.findViewById(R.id.action_bar)
        actionBar.setOnTouchListener { _, ev ->
            if (ev.action == MotionEvent.ACTION_DOWN) {
                cockpitRoot.startDrag = true
                true
            } else
                false
        }

        bottomButton = view.findViewById(R.id.resize_handle)
        bottomButton.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_MOVE) {
                presenter.requestResize(bottomButton.y.toInt() + motionEvent.y.toInt())
                true
            } else
                false
        }

        bottomButton.visibility = View.GONE

        cockpitRoot = view.findViewById(R.id.cockpit_root)
        cockpitContent = view.findViewById(R.id.cockpit_content)
        cockpitRoot.setDraggableView(R.id.cockpit_content)
    }

    override fun onPause() {
        super.onPause()
        alertDialog?.let {
            it.dismiss()
            alertDialog = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.stop()
    }

    override fun reloadParam(paramName: String) {
        paramsEditionAdapter.reloadParam(paramName)
    }

    override fun reloadAll() {
        paramsEditionAdapter.reloadAll()
    }

    override fun display(model: DisplayModel) {
        if (model.breadcrumb.descendants.isNotEmpty()) {
            if (model.label != null) label.text = model.label
            navigateTop.visibility = View.VISIBLE
            navigateBack.visibility = View.VISIBLE
        } else {
            label.setText(R.string.title)
            navigateBack.visibility = View.INVISIBLE
            navigateTop.visibility = View.GONE
        }
        paramsEditionAdapter.display(model)
    }

    override fun displayNavigationDialog(options: List<NavigationOption>) {
        val items = options.map { it.displayName }.toTypedArray()
        alertDialog = AlertDialog.Builder(context!!)
                .setTitle(getString(R.string.navigation_dialog_title))
                .setItems(items) { _, which: Int ->
                    presenter.onNavigationChosen(options[which])
                }.create().apply { show() }
    }

    override fun expand() {
        animateExpandCollapseIcon(true)
        cockpitRoot.expand()
        expanded = true

        setRectangleCorners()

        bottomButton.visibility = View.GONE
    }

    override fun collapse() {
        animateExpandCollapseIcon(false)
        cockpitRoot.collapse()
        expanded = false

        setRoundedCorners()

        bottomButton.visibility = View.VISIBLE
    }

    override fun resize(height: Int) {
        cockpitRoot.resize(height)
    }

    private fun setRoundedCorners() {
        actionBar.background = resources.getDrawable(R.drawable.rounded_action_bar, null)
        bottomButton.background = resources.getDrawable(R.drawable.rounded_resize_handle, null)
        cockpitContent.background = resources.getDrawable(R.drawable.rounded_corners_background, null)
    }

    private fun setRectangleCorners() {
        actionBar.background = resources.getDrawable(R.color.cockpit_peek, null)
        bottomButton.background = resources.getDrawable(R.color.cockpit_resize_handle, null)
        cockpitContent.background = resources.getDrawable(R.color.cockpit_content_background, null)
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