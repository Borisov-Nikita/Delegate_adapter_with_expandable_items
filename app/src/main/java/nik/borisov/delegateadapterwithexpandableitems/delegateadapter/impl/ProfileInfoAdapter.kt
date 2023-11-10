package nik.borisov.delegateadapterwithexpandableitems.delegateadapter.impl

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import nik.borisov.delegateadapterwithexpandableitems.databinding.ItemProfileInfoBinding
import nik.borisov.delegateadapterwithexpandableitems.delegateadapter.core.DelegateAdapterItem
import nik.borisov.delegateadapterwithexpandableitems.delegateadapter.core.DelegateAdapterWithExpandableItem
import nik.borisov.delegateadapterwithexpandableitems.delegateadapter.core.ExpandableItemViewHolder
import nik.borisov.delegateadapterwithexpandableitems.delegateadapter.impl.ProfileInfoAdapter.ProfileInfoViewHolder

class ProfileInfoAdapter(
    private val onMessageClickListener: (link: String) -> Unit
) : DelegateAdapterWithExpandableItem<ProfileInfoAdapterItem, ProfileInfoViewHolder>(
    ProfileInfoAdapterItem::class.java
) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = ItemProfileInfoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProfileInfoViewHolder(binding, onMessageClickListener)
    }

    override fun setExpandProgress(viewHolder: ProfileInfoViewHolder, progress: Float) {
        super.setExpandProgress(viewHolder, progress)

        viewHolder.indicator.rotation = 180 * progress
    }

    class ProfileInfoViewHolder(
        private val binding: ItemProfileInfoBinding,
        private val onMessageClickListener: (link: String) -> Unit
    ) : ExpandableItemViewHolder(binding.root) {

        override val expandableContainer = binding.root
        override val expandView = binding.descriptionTextView
        val indicator = binding.indicatorImageView

        override fun bind(item: DelegateAdapterItem) {
            if (item is ProfileInfoAdapterItem) {
                binding.nameTextView.text = itemView.context.getString(item.name)
                binding.descriptionTextView.text = itemView.context.getString(item.description)
                binding.messageImageView.setOnClickListener {
                    onMessageClickListener(itemView.context.getString(item.link))
                }
            }
        }
    }
}