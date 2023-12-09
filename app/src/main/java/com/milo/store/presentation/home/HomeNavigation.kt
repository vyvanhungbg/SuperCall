package com.milo.store.presentation.home

import com.milo.store.R
import com.milo.store.data.model.User
import com.android.milo_store.base.BaseFragment
import com.android.milo_store.base.BaseNavigation
/**
- Create by :Vy Hùng
- Create at :05,November,2023
 **/
private const val TAG = "HomeNavigation"

class HomeNavigation(val fragment: HomeFragment) : BaseNavigation() {

    override fun fragment(): BaseFragment<*, *> {
        return fragment
    }

    fun navigateToDetail(user: User) {
        val direction = HomeFragmentDirections.actionHomeFragmentToDetailFragment(user)
        navigateTo(R.id.homeFragment, direction)
    }
}