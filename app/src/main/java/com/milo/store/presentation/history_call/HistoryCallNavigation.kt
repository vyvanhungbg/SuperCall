package com.milo.store.presentation.history_call

import com.android.milo_store.base.BaseFragment
import com.android.milo_store.base.BaseNavigation


private const val TAG = "HistoryCallNavigation"

class HistoryCallNavigation(val fragment: HistoryCallFragment) : BaseNavigation() {

    override fun fragment(): BaseFragment<*, *> {
        return fragment
    }
}