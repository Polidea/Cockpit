package com.polidea.cockpit.paramsedition

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.polidea.cockpit.Dismissable
import com.polidea.cockpit.R
import kotlinx.android.synthetic.main.fragment_params_edition.*


class ParamsEditionFragment : Fragment(), ParamsEditionContract.View {

    override lateinit var presenter: ParamsEditionContract.Presenter

    private lateinit var paramsEditionAdapter: ParamsEditionAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_params_edition, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        paramsEditionAdapter = ParamsEditionAdapter(presenter)
        params_list.adapter = paramsEditionAdapter
        cockpit_save_button.setOnClickListener { presenter.save() }
        cockpit_restore_defaults_button.setOnClickListener { presenter.restoreAll() }
        presenter.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.stop()
    }

    override fun reloadParam(position: Int) {
        paramsEditionAdapter.reloadParam(position)
    }

    override fun reloadAll() {
        paramsEditionAdapter.reloadAll()
    }

    override fun dismiss() {
        (activity as? Dismissable)?.dismiss()
    }

    companion object {
        fun newInstance() = ParamsEditionFragment()
    }
}
