package com.milo.store.data.data_source.user

import com.milo.store.data.model.BaseResponseGitHub
import com.milo.store.data.model.SearchCacheLocal
import com.milo.store.data.model.User
/**
- Create by :Vy Hùng
- Create at :05,November,2023
 **/

interface UserDataSource {
    interface Remote {
        suspend fun searchUserByName(name: String): BaseResponseGitHub<User>?
    }

    interface Local {
        suspend fun searchUserByName(name: String): BaseResponseGitHub<User>?
        suspend fun saveSearchResult(searchResult: SearchCacheLocal):Long
    }
}