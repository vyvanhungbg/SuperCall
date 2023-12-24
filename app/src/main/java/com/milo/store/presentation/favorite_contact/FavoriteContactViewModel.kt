package com.milo.store.presentation.favorite_contact

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.milo_store.base.BaseViewModel

private const val TAG = "FavoriteContactViewModel"

class FavoriteContactViewModel(

) : BaseViewModel() {

    private val _data = MutableLiveData<String>()
    val data: LiveData<String> get() = _data

}

