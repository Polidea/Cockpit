package com.polidea.cockpit.paramsedition

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.polidea.cockpit.R
import com.polidea.cockpit.paramsedition.viewholder.*

class ParamsEditionAdapter(var presenter: ParamsEditionContract.Presenter) : RecyclerView.Adapter<ParamBaseViewHolder<*>>(), ParamsEditionContract.ParamView {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParamBaseViewHolder<*> {
        return when (ParamType.fromOrdinal(viewType)) {
            ParamType.BOOL -> BooleanParamViewHolder(inflateViewForHolder(R.layout.cockpit_boolean_param, parent)).configure()
            ParamType.INT -> IntParamViewHolder(inflateViewForHolder(R.layout.cockpit_number_param, parent)).configure()
            ParamType.DOUBLE -> DoubleParamViewHolder(inflateViewForHolder(R.layout.cockpit_number_param, parent)).configure()
            ParamType.STRING -> StringParamViewHolder(inflateViewForHolder(R.layout.cockpit_string_param, parent)).configure()
            ParamType.ACTION -> ActionParamViewHolder(inflateViewForHolder(R.layout.cockpit_action_param, parent)).configure()
        }
    }

    private fun inflateViewForHolder(@LayoutRes layoutId: Int, parent: ViewGroup) = LayoutInflater.from(parent.context)
            .inflate(layoutId, parent, false)

    override fun onBindViewHolder(holder: ParamBaseViewHolder<*>, position: Int) {
        when (ParamType.fromOrdinal(getItemViewType(position))) {
            ParamType.BOOL -> (holder as BooleanParamViewHolder).displayParam(presenter.getParamAt(position))
            ParamType.INT -> (holder as IntParamViewHolder).displayParam(presenter.getParamAt(position))
            ParamType.DOUBLE -> (holder as DoubleParamViewHolder).displayParam(presenter.getParamAt(position))
            ParamType.STRING -> (holder as StringParamViewHolder).displayParam(presenter.getParamAt(position))
            ParamType.ACTION -> (holder as ActionParamViewHolder).displayParam(presenter.getParamAt(position))
        }
    }

    override fun reloadAll() {
        notifyDataSetChanged()
    }

    override fun reloadParam(position: Int) {
        notifyItemChanged(position)
    }

    override fun getItemCount() = presenter.getParamsSize()

    override fun getItemViewType(position: Int): Int {
        val param = presenter.getParamAt<Any>(position)
        return ParamType.getRowType(param).ordinal
    }

    private fun <T : Any> ParamBaseValueViewHolder<T>.configure(): ParamBaseValueViewHolder<T> {
        valueChangeListener = { presenter.onParamChange(adapterPosition, it) }
        restoreClickListener = { presenter.restore(adapterPosition) }
        return this
    }

    private fun ActionParamViewHolder.configure(): ActionParamViewHolder {
        actionButtonClickListener = { presenter.requestAction(adapterPosition) }
        return this
    }
}