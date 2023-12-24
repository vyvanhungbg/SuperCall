package com.milo.store.presentation.history_call

import com.android.milo_store.base.BaseFragment
import com.milo.store.R
import com.milo.store.databinding.FragmentHistoryCallBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


private const val TAG = "HistoryCallFragment"

class HistoryCallFragment :
    BaseFragment<FragmentHistoryCallBinding, HistoryCallNavigation>() {

    override fun getLayoutId() = R.layout.fragment_history_call

    override val viewModel by viewModel<HistoryCallViewModel>()
    override val navigation: HistoryCallNavigation
        get() = HistoryCallNavigation(this)

    override fun initData() {

    }

    override fun observeData() {

    }

    override fun setView() {

    }

    override fun setOnClick() {

    }


}