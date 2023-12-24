package com.milo.store.presentation.setting

import com.android.milo_store.base.BaseFragment
import com.milo.store.R
import com.milo.store.databinding.FragmentSettingBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


private const val TAG = "SettingFragment"

class SettingFragment :
    BaseFragment<FragmentSettingBinding, SettingNavigation>() {

    override fun getLayoutId() = R.layout.fragment_setting

    override val viewModel by viewModel<SettingViewModel>()
    override val navigation: SettingNavigation
        get() = SettingNavigation(this)

    override fun initData() {

    }

    override fun observeData() {

    }

    override fun setView() {

    }

    override fun setOnClick() {

    }


}