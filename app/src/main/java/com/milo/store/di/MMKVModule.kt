package com.milo.store.di

import android.content.Context
import com.tencent.mmkv.MMKV
import org.koin.dsl.module


/**
- Create by :Vy Hùng
- Create at :05,November,2023
 **/

private const val TAG = "MMKVModule"
val mmkvModule = module {
    single { provideMMKV(get()) }
}

fun provideMMKV(context: Context): MMKV {
    MMKV.initialize(context)
    return MMKV.defaultMMKV()
}