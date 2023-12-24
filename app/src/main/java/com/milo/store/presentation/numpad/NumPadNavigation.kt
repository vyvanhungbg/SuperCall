package com.milo.store.presentation.numpad

import com.android.milo_store.base.BaseFragment
import com.android.milo_store.base.BaseNavigation


private const val TAG = "NumPadNavigation"

class NumPadNavigation(val fragment: NumPadFragment) : BaseNavigation() {

    override fun fragment(): BaseFragment<*, *> {
        return fragment
    }
}