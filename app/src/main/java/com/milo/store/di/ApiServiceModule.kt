package com.milo.store.di

import com.milo.store.data.model.BaseResponseGitHub
import com.milo.store.data.model.User
import com.milo.store.utils.constant.APIConstant
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query


/**
- Create by :Vy Hùng
- Create at :05,November,2023
 **/

val apiServiceModule = module {
    single { get<Retrofit>().create(ApiService::class.java) }
}


interface ApiService{
    @GET(APIConstant.EndPoint.SEARCH_USER)
    suspend fun searchUserByKeyWord(@Query("q") keyWord: String): BaseResponseGitHub<User>?
}