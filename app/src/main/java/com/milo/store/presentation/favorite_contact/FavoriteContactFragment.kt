package com.milo.store.presentation.favorite_contact

import com.android.milo_store.base.BaseFragment
import com.milo.store.R
import com.milo.store.databinding.FragmentFavoriteContactBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


private const val TAG = "FavoriteContactFragment"

class FavoriteContactFragment :
    BaseFragment<FragmentFavoriteContactBinding, FavoriteContactNavigation>() {

    override fun getLayoutId() = R.layout.fragment_favorite_contact

    override val viewModel by viewModel<FavoriteContactViewModel>()
    override val navigation: FavoriteContactNavigation
        get() = FavoriteContactNavigation(this)

    override fun initData() {

    }

    override fun observeData() {

    }

    override fun setView() {

    }

    override fun setOnClick() {

    }


}