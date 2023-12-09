package com.milo.store.data.repositories.user

import com.milo.store.data.model.BaseResponseGitHub
import com.milo.store.data.model.SearchCacheLocal
import com.milo.store.data.model.User
import com.android.milo_store.base.DataState
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun searchUserByName(name: String): Flow<DataState<BaseResponseGitHub<User>?>>
    suspend fun searchUserByNameLocal(name: String): Flow<DataState<BaseResponseGitHub<User>>>
    suspend fun saveSearchResult(searchResult: SearchCacheLocal): Flow<DataState<Long>>

}