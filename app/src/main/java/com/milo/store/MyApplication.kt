package com.milo.store

import android.app.Application
import com.milo.store.di.apiServiceModule
import com.milo.store.di.dataSourceModule
import com.milo.store.di.mmkvModule
import com.milo.store.di.networkModule
import com.milo.store.di.objectBoxModule
import com.milo.store.di.repositoryModule
import com.milo.store.di.useCaseModule
import com.milo.store.di.viewModelModule
import com.android.milo_store.base.Logg
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin


/**
- Create by :Vy Hùng
- Create at :31,October,2023
 **/

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Logg.initLogger(BuildConfig.DEBUG)
        startKoin {
            androidContext(this@MyApplication)
            modules(
                objectBoxModule,
                dataSourceModule,
                repositoryModule,
                useCaseModule,
                viewModelModule,
                apiServiceModule,
                mmkvModule,
                networkModule,
                viewModelModule
            )
        }
    }
}