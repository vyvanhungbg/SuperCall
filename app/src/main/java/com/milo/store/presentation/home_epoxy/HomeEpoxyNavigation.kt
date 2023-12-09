package com.milo.store.presentation.home_epoxy

import com.milo.store.R
import com.milo.store.data.model.User
import com.android.milo_store.base.BaseFragment
import com.android.milo_store.base.BaseNavigation

/**
- Create by :Vy Hùng
- Create at :05,November,2023
 **/
private const val TAG = "HomeEpoxyNavigation"

class HomeEpoxyNavigation(val fragment: HomeEpoxyFragment) : BaseNavigation() {

    override fun fragment(): BaseFragment<*, *> {
        return fragment
    }

    fun navigateToDetail(user: User) {
        val direction = HomeEpoxyFragmentDirections.actionHomeEpoxyFragmentToDetailFragment(user)
        navigateTo(R.id.homeEpoxyFragment, direction)
    }
}