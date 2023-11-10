package nik.borisov.delegateadapterwithexpandableitems.delegateadapter.core

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class CompositeAdapter(
    private val delegates: SparseArray<DelegateAdapter<DelegateAdapterItem, ViewHolder>>
) : ListAdapter<DelegateAdapterItem, ViewHolder>(DelegateAdapterItemDiffUtilCallback()) {

    private lateinit var recyclerView: RecyclerView
    private var expandedModel: DelegateAdapterItem? = null

    override fun getItemViewType(position: Int): Int {
        for (i in 0 until delegates.size()) {
            if (delegates[i].modelClass == getItem(position).javaClass) {
                return delegates.keyAt(i)
            }
        }
        throw NullPointerException("Can't get viewType for position $position")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return delegates[viewType].createViewHolder(parent)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return onBindViewHolder(holder, position, mutableListOf())
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val delegateAdapter = delegates[getItemViewType(position)]

        if (delegateAdapter != null) {
            val delegatePayloads = payloads.map {
                it as DelegateAdapterItem.Payloadable
            }

            when (delegateAdapter) {
                is DelegateAdapterSimple -> {

                    delegateAdapter.bindViewHolder(getItem(position), holder, delegatePayloads)
                }

                is DelegateAdapterWithExpandableItem -> {

                    delegateAdapter.bindViewHolder(
                        getItem(position),
                        holder,
                        delegatePayloads
                    ) { expandableContainer ->
                        setOnExpandItemClickListener(
                            delegateAdapter,
                            expandableContainer,
                            holder as ExpandableItemViewHolder,
                            position
                        )
                    }
                }
            }
        } else {
            throw NullPointerException("Can't find adapter for position $position")
        }
    }

    override fun onViewRecycled(holder: ViewHolder) {
        delegates[holder.itemViewType].onViewRecycled(holder)
        super.onViewRecycled(holder)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        delegates[holder.itemViewType].onViewDetachedFromWindow(holder)
        super.onViewDetachedFromWindow(holder)
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        delegates[holder.itemViewType].onViewAttachedToWindow(holder)
        super.onViewAttachedToWindow(holder)
    }

    private fun setOnExpandItemClickListener(
        delegateAdapter: DelegateAdapterWithExpandableItem<DelegateAdapterItem, ExpandableItemViewHolder>,
        clickableContainer: View,
        holder: ExpandableItemViewHolder,
        position: Int,
    ) {
        val model = currentList[position]

        delegateAdapter.expandItem(holder, model == expandedModel, animate = false)

        clickableContainer.setOnClickListener {
            when (expandedModel) {
                null -> {

                    // expand clicked view
                    delegateAdapter.expandItem(holder, expand = true, animate = true)
                    expandedModel = model
                }

                model -> {

                    // collapse clicked view
                    delegateAdapter.expandItem(holder, expand = false, animate = true)
                    expandedModel = null
                }

                else -> {

                    // collapse previously expanded view
                    val expandedModelPosition = currentList.indexOf(expandedModel!!)
                    val expandedDelegateAdapter =
                        delegates[getItemViewType(expandedModelPosition)] as? DelegateAdapterWithExpandableItem
                    val oldViewHolder =
                        recyclerView.findViewHolderForAdapterPosition(
                            expandedModelPosition
                        )
                    if (oldViewHolder != null) expandedDelegateAdapter?.expandItem(
                        oldViewHolder, expand = false, animate = true
                    )

                    // expand clicked view
                    delegateAdapter.expandItem(holder, expand = true, animate = true)
                    expandedModel = model
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Builder {

        private var count: Int = 0
        private val delegates: SparseArray<DelegateAdapter<DelegateAdapterItem, ViewHolder>> =
            SparseArray()

        fun add(delegateAdapter: DelegateAdapter<out DelegateAdapterItem, *>): Builder {
            delegates.put(
                count++,
                delegateAdapter as DelegateAdapter<DelegateAdapterItem, ViewHolder>
            )
            return this
        }

        fun build(): CompositeAdapter {
            require(count != 0) {
                "Register at least one adapter"
            }
            return CompositeAdapter(delegates)
        }
    }
}