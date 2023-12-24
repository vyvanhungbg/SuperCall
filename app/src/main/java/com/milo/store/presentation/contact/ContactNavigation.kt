package com.milo.store.presentation.contact

import com.android.milo_store.base.BaseFragment
import com.android.milo_store.base.BaseNavigation

private const val TAG = "ContactNavigation"

class ContactNavigation(val fragment: ContactFragment) : BaseNavigation() {

    override fun fragment(): BaseFragment<*, *> {
        return fragment
    }
}