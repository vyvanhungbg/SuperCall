package com.milo.store.presentation.detail

import com.android.milo_store.base.BaseFragment
import com.android.milo_store.base.BaseNavigation
/**
- Create by :Vy Hùng
- Create at :05,November,2023
 **/
private const val TAG = "DetailNavigation"

class DetailNavigation(val fragment: DetailFragment) : BaseNavigation() {

    override fun fragment(): BaseFragment<*, *> {
        return fragment
    }
}