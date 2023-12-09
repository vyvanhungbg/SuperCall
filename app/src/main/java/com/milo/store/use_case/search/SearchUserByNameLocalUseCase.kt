package com.milo.store.use_case.search

import com.milo.store.data.model.BaseResponseGitHub
import com.milo.store.data.model.User
import com.milo.store.data.repositories.user.UserRepository
import com.android.milo_store.base.BaseUseCase
import com.android.milo_store.base.DataState
import kotlinx.coroutines.flow.Flow

class SearchUserByNameLocalUseCase(private val repo: UserRepository): BaseUseCase<String, Flow<DataState<BaseResponseGitHub<User>>>>() {

    override suspend fun execute(request: String): Flow<DataState<BaseResponseGitHub<User>>> {
        return  repo.searchUserByNameLocal(request)
    }

}