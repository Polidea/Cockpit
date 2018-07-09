package com.polidea.cockpit.paramsedition

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import com.polidea.cockpit.R
import com.polidea.cockpit.utils.getScreenHeight
import com.polidea.cockpit.utils.removeDimmedBackground

class CockpitDialog internal constructor() : BottomSheetDialogFragment(), ParamsEditionContract.View {

    private val peekHeight = getScreenHeight() / 2
    override lateinit var presenter: ParamsEditionContract.Presenter
    private lateinit var paramsEditionAdapter: ParamsEditionAdapter
    private lateinit var behavior: BottomSheetBehavior<View>

    override fun onStart() {
        super.onStart()
        removeDimmedBackground()
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        val view = View.inflate(context, R.layout.dialog_params_edition, null)
        dialog.setContentView(view)
        behavior = BottomSheetBehavior.from(view.parent as View)
        behavior.isHideable = true
        behavior.skipCollapsed = false
        behavior.peekHeight = peekHeight

        paramsEditionAdapter = ParamsEditionAdapter(presenter)
        view.findViewById<RecyclerView>(R.id.params_list).adapter = paramsEditionAdapter
        view.findViewById<Button>(R.id.restore_defaults).setOnClickListener { presenter.restoreAll() }
        view.findViewById<Button>(R.id.expand_collapse).setOnClickListener {
            if (behavior.state == BottomSheetBehavior.STATE_COLLAPSED)
                presenter.expand()
            else if (behavior.state == BottomSheetBehavior.STATE_EXPANDED)
                presenter.collapse()
        }

        presenter.start()
        retainInstance = true

        return dialog
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.stop()
    }

    override fun reloadParam(position: Int) {
        paramsEditionAdapter.reloadParam(position)
    }

    override fun reloadAll() {
        paramsEditionAdapter.reloadAll()
    }

    override fun expand() {
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun collapse() {
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    companion object {
        fun newInstance(): CockpitDialog {
            val instance = CockpitDialog()
            CockpitDialogPresenter(instance)
            return instance
        }
    }
}