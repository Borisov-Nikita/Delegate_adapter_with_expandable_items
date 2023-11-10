package nik.borisov.delegateadapterwithexpandableitems.delegateadapter.impl

import androidx.annotation.StringRes
import nik.borisov.delegateadapterwithexpandableitems.delegateadapter.core.DelegateAdapterItem

class BaseInfoAdapterItem(
    val id: Int,
   @StringRes val info: Int
) : DelegateAdapterItem {

    override fun id(): Any {
        return id
    }

    override fun content(): Any {
        return info
    }
}