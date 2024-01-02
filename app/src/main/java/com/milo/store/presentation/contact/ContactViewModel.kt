package com.milo.store.presentation.contact

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.milo_store.base.BaseViewModel
import com.milo.store.call.helper.CallContactInfo
import com.milo.store.call.model.Contact
import kotlinx.coroutines.launch

private const val TAG = "ContactViewModel"

class ContactViewModel(

) : BaseViewModel() {

    private val _listContact = MutableLiveData<List<Contact>>()
    val listContact: LiveData<List<Contact>> get() = _listContact

    fun getAllContact(context: Context) {
        viewModelScope.launch {
            CallContactInfo.getAllPhoneContacts(context, onSuccess = {
                _listContact.postValue(it)
                Log.e(TAG, "getAllContact: ${it.size}", )
            }, onFailure = {
                _listContact.postValue(emptyList())
            })
        }
    }

}

