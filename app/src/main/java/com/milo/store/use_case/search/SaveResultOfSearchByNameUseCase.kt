package com.milo.store.use_case.search

import com.milo.store.data.model.SearchCacheLocal
import com.milo.store.data.repositories.user.UserRepository
import com.android.milo_store.base.BaseUseCase
import com.android.milo_store.base.DataState
import kotlinx.coroutines.flow.Flow

class SaveResultOfSearchByNameUseCase(private val repo: UserRepository) :
    BaseUseCase<SearchCacheLocal, Flow<DataState<Long>>>() {

    override suspend fun execute(request: SearchCacheLocal): Flow<DataState<Long>> {
        return repo.saveSearchResult(request)
    }


}