package nik.borisov.delegateadapterwithexpandableitems.delegateadapter.impl

import androidx.annotation.StringRes
import nik.borisov.delegateadapterwithexpandableitems.delegateadapter.core.DelegateAdapterItem

class ProfileInfoAdapterItem(
    val id: Int,
    @StringRes val name: Int,
    @StringRes val description: Int,
    @StringRes val link: Int
) : DelegateAdapterItem {

    override fun id(): Any {
        return id
    }

    override fun content(): Any {
        return description
    }
}