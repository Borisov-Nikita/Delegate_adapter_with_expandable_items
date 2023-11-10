package nik.borisov.delegateadapterwithexpandableitems.delegateadapter.impl

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import nik.borisov.delegateadapterwithexpandableitems.databinding.ItemBaseInfoBinding
import nik.borisov.delegateadapterwithexpandableitems.delegateadapter.core.DelegateAdapterItem
import nik.borisov.delegateadapterwithexpandableitems.delegateadapter.core.DelegateAdapterSimple
import nik.borisov.delegateadapterwithexpandableitems.delegateadapter.impl.BaseInfoAdapter.BaseInfoViewHolder

class BaseInfoAdapter :
    DelegateAdapterSimple<BaseInfoAdapterItem, BaseInfoViewHolder>(
        BaseInfoAdapterItem::class.java
    ) {

    override fun createViewHolder(parent: ViewGroup): ViewHolder {
        val binding = ItemBaseInfoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BaseInfoViewHolder(binding)
    }

    override fun bindViewHolder(
        item: BaseInfoAdapterItem,
        viewHolder: BaseInfoViewHolder,
        payloads: List<DelegateAdapterItem.Payloadable>
    ) {
        viewHolder.bind(item)
    }

    class BaseInfoViewHolder(
        private val binding: ItemBaseInfoBinding
    ) : ViewHolder(binding.root) {

        fun bind(item: BaseInfoAdapterItem) {
            binding.infoTextView.text = itemView.context.getString(item.info)
        }
    }
}