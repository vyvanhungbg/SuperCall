package com.milo.store.data.data_source.user

import com.milo.store.data.model.BaseResponseGitHub
import com.milo.store.data.model.SearchCacheLocal
import com.milo.store.data.model.User
import io.objectbox.Box
/**
- Create by :Vy Hùng
- Create at :05,November,2023
 **/

class UserLocalDataSource(private val boxSearchCacheLocal: Box<SearchCacheLocal>) :
    UserDataSource.Local {
    override suspend fun searchUserByName(name: String): BaseResponseGitHub<User>? {
        return boxSearchCacheLocal.query().filter { it -> it.keySearch == name }.build()
            .find().toMutableList().firstOrNull()?.data
    }

    override suspend fun saveSearchResult(searchResult: SearchCacheLocal): Long {
        val exits = boxSearchCacheLocal.query().filter { it -> it.keySearch == searchResult.keySearch }.build()
            .find().firstOrNull()
        if (exits != null) {
            boxSearchCacheLocal.remove(exits.id)
        }
       return boxSearchCacheLocal.put(searchResult)
    }


}