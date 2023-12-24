package com.milo.store.presentation.history_call

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.milo_store.base.BaseViewModel

private const val TAG = "HistoryCallViewModel"

class HistoryCallViewModel(

) : BaseViewModel() {

    private val _data = MutableLiveData<String>()
    val data: LiveData<String> get() = _data

}

