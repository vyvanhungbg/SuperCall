package com.milo.store.presentation.favorite_contact

import com.android.milo_store.base.BaseFragment
import com.android.milo_store.base.BaseNavigation

private const val TAG = "FavoriteContactNavigation"

class FavoriteContactNavigation(val fragment: FavoriteContactFragment) : BaseNavigation() {

    override fun fragment(): BaseFragment<*, *> {
        return fragment
    }
}