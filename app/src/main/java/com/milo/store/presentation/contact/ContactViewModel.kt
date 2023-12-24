package com.milo.store.presentation.contact

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.milo_store.base.BaseViewModel

private const val TAG = "ContactViewModel"

class ContactViewModel(

) : BaseViewModel() {

    private val _data = MutableLiveData<String>()
    val data: LiveData<String> get() = _data

}

