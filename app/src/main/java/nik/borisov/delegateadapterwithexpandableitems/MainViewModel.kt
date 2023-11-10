package nik.borisov.delegateadapterwithexpandableitems

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nik.borisov.delegateadapterwithexpandableitems.delegateadapter.core.DelegateAdapterItem
import nik.borisov.delegateadapterwithexpandableitems.delegateadapter.impl.BaseInfoAdapterItem
import nik.borisov.delegateadapterwithexpandableitems.delegateadapter.impl.DescriptionAdapterItem
import nik.borisov.delegateadapterwithexpandableitems.delegateadapter.impl.ProfileInfoAdapterItem

class MainViewModel : ViewModel() {

    private val _content = MutableLiveData<List<DelegateAdapterItem>>()
    val content: LiveData<List<DelegateAdapterItem>> = _content

    init {
        setupFakeContent()
    }

    private fun setupFakeContent() {
        val fakeContent = mutableListOf<DelegateAdapterItem>().apply {
            add(
                BaseInfoAdapterItem(
                    id = 1,
                    info = R.string.lorem_ipsum_base_info
                )
            )
            add(
                DescriptionAdapterItem(
                    id = 1,
                    title = R.string.lorem_ipsum_title,
                    description = R.string.lorem_ipsum
                )
            )
            repeat(3) {
                add(
                    ProfileInfoAdapterItem(
                        id = it,
                        name = R.string.lorem_ipsum_profile_name,
                        description = R.string.lorem_ipsum,
                        link = R.string.lorem_ipsum_link
                    )
                )
            }
            add(
                DescriptionAdapterItem(
                    id = 2,
                    title = R.string.lorem_ipsum_title,
                    description = R.string.lorem_ipsum
                )
            )
            repeat(4) {
                add(
                    ProfileInfoAdapterItem(
                        id = it + 21,
                        name = R.string.lorem_ipsum_profile_name,
                        description = R.string.lorem_ipsum,
                        link = R.string.lorem_ipsum_link
                    )
                )
            }
        }

        _content.value = fakeContent.toList()
    }
}