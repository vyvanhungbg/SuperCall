package com.milo.store.di

import com.milo.store.presentation.detail.DetailViewModel
import com.milo.store.presentation.home.HomeViewModel
import com.milo.store.presentation.home_epoxy.HomeEpoxyViewModel
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

/**
- Create by :Vy Hùng
- Create at :05,November,2023
 **/

val viewModelModule = module {
    viewModel { HomeViewModel(get(),get(),get(),get()) }
    viewModel { HomeEpoxyViewModel(get(),get(),get(),get()) }
    viewModel { DetailViewModel() }
}