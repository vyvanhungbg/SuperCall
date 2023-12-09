package com.milo.store.di

import com.milo.store.use_case.search.SaveResultOfSearchByNameUseCase
import com.milo.store.use_case.search.SearchUserByNameLocalUseCase
import com.milo.store.use_case.search.SearchUserByNameOnlineUseCase
import org.koin.dsl.module

/**
- Create by :Vy Hùng
- Create at :05,November,2023
 **/

val useCaseModule = module {
    single { SearchUserByNameOnlineUseCase(get()) }
    single { SearchUserByNameLocalUseCase(get()) }
    single { SaveResultOfSearchByNameUseCase(get()) }
}