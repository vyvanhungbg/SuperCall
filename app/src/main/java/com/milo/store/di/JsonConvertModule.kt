package com.milo.store.di

import com.google.gson.Gson
import org.koin.dsl.module


/**
- Create by :Vy Hùng
- Create at :05,November,2023
 **/

val jsonConvertModule = module {
    single { provideJsonConverter() }
}

fun provideJsonConverter() = Gson()