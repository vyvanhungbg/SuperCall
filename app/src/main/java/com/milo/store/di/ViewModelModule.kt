package com.milo.store.di

import com.milo.store.presentation.contact.ContactViewModel
import com.milo.store.presentation.detail.DetailViewModel
import com.milo.store.presentation.favorite_contact.FavoriteContactViewModel
import com.milo.store.presentation.history_call.HistoryCallViewModel
import com.milo.store.presentation.home.HomeViewModel
import com.milo.store.presentation.home_epoxy.HomeEpoxyViewModel
import com.milo.store.presentation.numpad.NumPadViewModel
import com.milo.store.presentation.setting.SettingViewModel
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
    viewModel { NumPadViewModel() }
    viewModel { ContactViewModel() }
    viewModel { FavoriteContactViewModel() }
    viewModel { SettingViewModel() }
    viewModel { HistoryCallViewModel() }
}