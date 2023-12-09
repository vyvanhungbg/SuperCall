package com.milo.store.di

import com.milo.store.data.repositories.user.UserRepository
import com.milo.store.data.repositories.user.UserRepositoryImpl
import org.koin.dsl.module

/**
- Create by :Vy Hùng
- Create at :05,November,2023
 **/

val repositoryModule = module {
    single<UserRepository> { UserRepositoryImpl(get(),get()) }
}