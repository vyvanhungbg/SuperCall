package com.milo.store.di

import com.milo.store.data.data_source.user.UserDataSource
import com.milo.store.data.data_source.user.UserLocalDataSource
import com.milo.store.data.data_source.user.UserRemoteDataSource
import org.koin.dsl.module

val dataSourceModule = module {
    single<UserDataSource.Remote> { UserRemoteDataSource(get()) }
    single<UserDataSource.Local> { UserLocalDataSource(get()) }
}