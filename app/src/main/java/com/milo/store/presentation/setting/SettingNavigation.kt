package com.milo.store.presentation.setting

import com.android.milo_store.base.BaseFragment
import com.android.milo_store.base.BaseNavigation

private const val TAG = "SettingNavigation"

class SettingNavigation(val fragment: SettingFragment) : BaseNavigation() {

    override fun fragment(): BaseFragment<*, *> {
        return fragment
    }
}