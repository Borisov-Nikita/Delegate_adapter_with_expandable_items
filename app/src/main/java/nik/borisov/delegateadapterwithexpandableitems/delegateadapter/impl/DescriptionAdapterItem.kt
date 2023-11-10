package nik.borisov.delegateadapterwithexpandableitems.delegateadapter.impl

import androidx.annotation.StringRes
import nik.borisov.delegateadapterwithexpandableitems.delegateadapter.core.DelegateAdapterItem

class DescriptionAdapterItem(
    val id: Int,
    @StringRes val title: Int,
    @StringRes val description: Int
) : DelegateAdapterItem {

    override fun id(): Any {
        return id
    }

    override fun content(): Any {
        return description
    }
}