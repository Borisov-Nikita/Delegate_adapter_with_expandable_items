package nik.borisov.delegateadapterwithexpandableitems.delegateadapter.impl

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import nik.borisov.delegateadapterwithexpandableitems.databinding.ItemDescriptionBinding
import nik.borisov.delegateadapterwithexpandableitems.delegateadapter.core.DelegateAdapterItem
import nik.borisov.delegateadapterwithexpandableitems.delegateadapter.core.DelegateAdapterWithExpandableItem
import nik.borisov.delegateadapterwithexpandableitems.delegateadapter.core.ExpandableItemViewHolder
import nik.borisov.delegateadapterwithexpandableitems.delegateadapter.impl.DescriptionAdapter.DescriptionViewHolder

class DescriptionAdapter :
    DelegateAdapterWithExpandableItem<DescriptionAdapterItem, DescriptionViewHolder>(
        DescriptionAdapterItem::class.java
    ) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = ItemDescriptionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DescriptionViewHolder(binding)
    }

    override fun setExpandProgress(viewHolder: DescriptionViewHolder, progress: Float) {
        super.setExpandProgress(viewHolder, progress)

        viewHolder.indicator.rotation = 180 * progress
    }

    class DescriptionViewHolder(
        private val binding: ItemDescriptionBinding
    ) : ExpandableItemViewHolder(binding.root) {

        override val expandableContainer = binding.root
        override val expandView = binding.descriptionTextView
        val indicator = binding.indicatorImageView

        override fun bind(item: DelegateAdapterItem) {
            if (item is DescriptionAdapterItem) {
                binding.titleTextView.text = itemView.context.getString(item.title)
                binding.descriptionTextView.text = itemView.context.getString(item.description)
            }
        }
    }
}