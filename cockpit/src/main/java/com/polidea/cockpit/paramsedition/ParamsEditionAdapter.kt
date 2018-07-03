package com.polidea.cockpit.paramsedition

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.polidea.cockpit.R
import com.polidea.cockpit.paramsedition.viewholder.*
import com.polidea.cockpit.utils.paramType

class ParamsEditionAdapter(var presenter: ParamsEditionContract.Presenter) : RecyclerView.Adapter<ParamBaseViewHolder<*>>(), ParamsEditionContract.ParamView {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParamBaseViewHolder<*> {
        return when (ParamType.fromOrdinal(viewType)) {
            ParamType.BOOL -> BooleanParamViewHolder(inflateViewForHolder(R.layout.cockpit_boolean_param, parent))
                    .apply {
                        valueChangeListener = { v ->
                            presenter.onParamChange(adapterPosition, v)
                        }
                        restoreClickListener = { presenter.restore(adapterPosition) }
                    }
            ParamType.INT -> IntParamViewHolder(inflateViewForHolder(R.layout.cockpit_number_param, parent))
                    .apply {
                        valueChangeListener = { v ->
                            presenter.onParamChange(adapterPosition, v)
                        }
                        restoreClickListener = { presenter.restore(adapterPosition) }
                    }
            ParamType.DOUBLE -> DoubleParamViewHolder(inflateViewForHolder(R.layout.cockpit_number_param, parent))
                    .apply {
                        valueChangeListener = { v ->
                            presenter.onParamChange(adapterPosition, v)
                        }
                        restoreClickListener = { presenter.restore(adapterPosition) }
                    }
            ParamType.STRING -> StringParamViewHolder(inflateViewForHolder(R.layout.cockpit_string_param, parent))
                    .apply {
                        valueChangeListener = { v ->
                            presenter.onParamChange(adapterPosition, v)
                        }
                        restoreClickListener = { presenter.restore(adapterPosition) }
                    }
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
        }
    }

    override fun reloadAll() {
        notifyDataSetChanged()
    }

    override fun reloadParam(position: Int) {
        notifyItemChanged(position)
    }

    override fun getItemCount() = presenter.getParamsSize()

    override fun getItemViewType(position: Int) =
            presenter.getParamAt<Any>(position).paramType().ordinal
}