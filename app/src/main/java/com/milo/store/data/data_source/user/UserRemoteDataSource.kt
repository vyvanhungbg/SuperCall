package com.milo.store.data.data_source.user

import com.milo.store.data.model.BaseResponseGitHub
import com.milo.store.data.model.User
import com.milo.store.di.ApiService
/**
- Create by :Vy Hùng
- Create at :05,November,2023
 **/

class UserRemoteDataSource(private val apiService: ApiService): UserDataSource.Remote {

    override suspend fun searchUserByName(name: String): BaseResponseGitHub<User>? {
        return apiService.searchUserByKeyWord(keyWord = name)
    }
}