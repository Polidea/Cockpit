package com.polidea.cockpit.paramsedition

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.polidea.cockpit.R
import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.type.*
import com.polidea.cockpit.paramsedition.viewholder.*
import java.lang.IllegalArgumentException

internal class ParamsEditionAdapter(private var displayModel: DisplayModel,
                                    private val presenter: ParamsEditionContract.Presenter) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ParamsEditionContract.ParamView {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ParamType.BOOL.ordinal -> BooleanParamViewHolder(inflateViewForHolder(R.layout.cockpit_boolean_param, parent)).configure()
            ParamType.INT.ordinal -> IntParamViewHolder(inflateViewForHolder(R.layout.cockpit_number_param, parent)).configure()
            ParamType.DOUBLE.ordinal -> DoubleParamViewHolder(inflateViewForHolder(R.layout.cockpit_number_param, parent)).configure()
            ParamType.STRING.ordinal -> StringParamViewHolder(inflateViewForHolder(R.layout.cockpit_string_param, parent)).configure()
            ParamType.LIST.ordinal -> ListParamViewHolder(inflateViewForHolder(R.layout.cockpit_list_param, parent)).configure()
            ParamType.ACTION.ordinal -> ActionParamViewHolder(inflateViewForHolder(R.layout.cockpit_action_param, parent)).configure()
            ParamType.COLOR.ordinal -> ColorParamViewHolder(inflateViewForHolder(R.layout.cockpit_color_param, parent)).configure()
            ParamType.RANGE_INT.ordinal -> RangeIntParamViewHolder(inflateViewForHolder(R.layout.cockpit_range_param, parent)).configure()
            ParamType.RANGE_DOUBLE.ordinal -> RangeDoubleParamViewHolder(inflateViewForHolder(R.layout.cockpit_range_param, parent)).configure()
            ParamType.READ_ONLY.ordinal -> ReadOnlyParamViewHolder(inflateViewForHolder(R.layout.cockpit_read_only_param, parent)).configure()
            ParamType.STEP_INT.ordinal -> StepIntParamViewHolder(inflateViewForHolder(R.layout.cockpit_step_param, parent)).configure()
            ParamType.STEP_DOUBLE.ordinal -> StepDoubleParamViewHolder(inflateViewForHolder(R.layout.cockpit_step_param, parent)).configure()
            GROUP_TYPE_ID -> GroupViewHolder(inflateViewForHolder(R.layout.subgroup, parent)).configure()
            SECTION_TYPE_ID -> SectionViewHolder(inflateViewForHolder(R.layout.cockpit_group_name, parent))
            PATH_TYPE_ID -> PathViewHolder(inflateViewForHolder(R.layout.path_button, parent)).configure() //TODO fix layout
            else -> throw IllegalArgumentException("View type $viewType does not match any known case")
        }
    }

    private fun inflateViewForHolder(@LayoutRes layoutId: Int, parent: ViewGroup) = LayoutInflater.from(parent.context)
            .inflate(layoutId, parent, false)

    override fun getItemCount(): Int = displayModel.items.size

    override fun getItemViewType(position: Int): Int {
        val displayedItem = displayModel.items[position]
        return when (displayedItem) {
            is DisplayItem.Param -> ParamType.getParamType(displayedItem.param).ordinal
            is DisplayItem.Group -> GROUP_TYPE_ID
            is DisplayItem.Section -> SECTION_TYPE_ID
            is DisplayItem.Path -> PATH_TYPE_ID
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val displayedItem = displayModel.items[position]
        when (displayedItem) {
            is DisplayItem.Param -> {
                when (ParamType.getParamType(displayedItem.param)) {
                    ParamType.BOOL -> (holder as BooleanParamViewHolder).displayParam(displayedItem.param as CockpitParam<Boolean>)
                    ParamType.INT -> (holder as IntParamViewHolder).displayParam(displayedItem.param as CockpitParam<Int>)
                    ParamType.DOUBLE -> (holder as DoubleParamViewHolder).displayParam(displayedItem.param as CockpitParam<Double>)
                    ParamType.STRING -> (holder as StringParamViewHolder).displayParam(displayedItem.param as CockpitParam<String>)
                    ParamType.LIST -> (holder as ListParamViewHolder).displayParam(displayedItem.param as CockpitParam<CockpitListType<Any>>)
                    ParamType.ACTION -> (holder as ActionParamViewHolder).displayParam(displayedItem.param as CockpitParam<CockpitAction>)
                    ParamType.COLOR -> (holder as ColorParamViewHolder).apply {
                        displayParam(displayedItem.param as CockpitParam<CockpitColor>)
                        onColorEditionRequestListener = { presenter.editColor(displayedItem.param.name) }
                    }
                    ParamType.RANGE_INT -> (holder as RangeIntParamViewHolder).displayParam(displayedItem.param as CockpitParam<CockpitRange<Int>>)
                    ParamType.RANGE_DOUBLE -> (holder as RangeDoubleParamViewHolder).displayParam(displayedItem.param as CockpitParam<CockpitRange<Double>>)
                    ParamType.READ_ONLY -> (holder as ReadOnlyParamViewHolder).displayParam(displayedItem.param as CockpitParam<CockpitReadOnly>)
                    ParamType.STEP_INT -> (holder as StepIntParamViewHolder).displayParam(displayedItem.param as CockpitParam<CockpitStep<Int>>)
                    ParamType.STEP_DOUBLE -> (holder as StepDoubleParamViewHolder).displayParam(displayedItem.param as CockpitParam<CockpitStep<Double>>)
                }
                ParamType.getParamType(displayedItem.param).ordinal
            }
            is DisplayItem.Group -> (holder as GroupViewHolder).display(displayedItem.displayName)
            is DisplayItem.Section -> (holder as SectionViewHolder).display(displayedItem.displayName)
        }
    }

    override fun display(model: DisplayModel) {
        displayModel = model
        notifyDataSetChanged()
    }

    override fun reloadParam(paramName: String) {
        val index = displayModel.items.indexOfFirst { it is DisplayItem.Param && it.param.name == paramName }
        notifyItemChanged(index)
    }

    override fun reloadAll() {
        notifyDataSetChanged()
    }

    private fun <T : Any> ParamBaseValueWithRestoreViewHolder<T>.configure(): ParamBaseValueWithRestoreViewHolder<T> {
        (this as ParamBaseValueViewHolder<T>).configure()
        restoreClickListener = {
            val item = displayModel.items[adapterPosition]
            if (item is DisplayItem.Param) {
                presenter.restore(item.param.name)
            }
        }
        return this
    }

    private fun <T : Any> ParamBaseValueViewHolder<T>.configure(): ParamBaseValueViewHolder<T> {
        valueChangeListener = {
            val item = displayModel.items[adapterPosition]
            if (item is DisplayItem.Param) {
                presenter.onParamChange(item.param.name, it)
            }
        }
        return this
    }

    private fun ActionParamViewHolder.configure(): ActionParamViewHolder {
        actionButtonClickListener = {
            val item = displayModel.items[adapterPosition]
            if (item is DisplayItem.Param) {
                presenter.requestAction(item.param.name)
            }
        }
        return this
    }

    private fun <T : Any> SelectionParamBaseViewHolder<T>.configure(): SelectionParamBaseViewHolder<T> {
        valueSelectedListener = {
            val item = displayModel.items[adapterPosition]
            if (item is DisplayItem.Param) {
                presenter.onParamValueSelected<T>(item.param.name, it)
            }
        }
        return this
    }

    private fun GroupViewHolder.configure(): GroupViewHolder {
        onGroupClickedListener = {
            val item = displayModel.items[adapterPosition]
            if (item is DisplayItem.Group) {
                presenter.onDisplayGroup(item.group)
            }
        }

        return this
    }

    private fun PathViewHolder.configure(): PathViewHolder {
        onPathClickedListener = {
            presenter.onPathClicked()
        }

        return this
    }

    companion object {
        private val SECTION_TYPE_ID: Int = ParamType.values().last().ordinal + 1
        private val GROUP_TYPE_ID: Int = ParamType.values().last().ordinal + 2
        private val PATH_TYPE_ID: Int = ParamType.values().last().ordinal + 3
    }
}