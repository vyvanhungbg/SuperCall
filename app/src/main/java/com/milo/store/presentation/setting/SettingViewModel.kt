package com.milo.store.presentation.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.milo_store.base.BaseViewModel

private const val TAG = "SettingViewModel"

class SettingViewModel(

) : BaseViewModel() {

    private val _data = MutableLiveData<String>()
    val data: LiveData<String> get() = _data

}

