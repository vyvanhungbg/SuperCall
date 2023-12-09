package com.milo.store.data.repositories.user

import com.milo.store.data.data_source.user.UserDataSource
import com.milo.store.data.model.BaseResponseGitHub
import com.milo.store.data.model.SearchCacheLocal
import com.milo.store.data.model.User
import com.milo.store.di.mapError
import com.android.milo_store.base.BaseRepository
import com.android.milo_store.base.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserRepositoryImpl(
    private val remote: UserDataSource.Remote,
    private val local: UserDataSource.Local
) : UserRepository,
    BaseRepository() {

    override suspend fun searchUserByName(name: String): Flow<DataState<BaseResponseGitHub<User>?>> =
        flow {
            emit(DataState.Loading)
            emit(getResult(transformError = ::mapError) { remote.searchUserByName(name) })
        }.flowOn(Dispatchers.IO).catch {
            emit(DataState.Error(Exception(it.message)))
        }

    override suspend fun searchUserByNameLocal(name: String): Flow<DataState<BaseResponseGitHub<User>>> =
        flow {
            emit(DataState.Loading)
            val result = getResult(transformError = ::mapError) { local.searchUserByName(name) }
            if (result is DataState.Success && result.data != null) {
                emit(DataState.Success(result.data!!))
            } else {
                emit(DataState.Error(Exception("Can not find value with search key ${name}")))
            }
        }.flowOn(Dispatchers.IO).catch {
            emit(DataState.Error(Exception(it.message)))
        }

    override suspend fun saveSearchResult(searchResult: SearchCacheLocal): Flow<DataState<Long>> =
        flow {
            emit(DataState.Loading)
            val result =
                getResult(transformError = ::mapError) { local.saveSearchResult(searchResult) }
            emit(result)
        }.flowOn(Dispatchers.IO).catch {
            emit(DataState.Error(Exception(it.message)))
        }

}