package com.milo.store.presentation.numpad

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.milo_store.base.BaseViewModel
import com.milo.store.call.helper.CallContactInfo
import com.milo.store.call.model.Contact
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

private const val TAG = "NumPadViewModel"

class NumPadViewModel(

) : BaseViewModel() {

    private val _data = MutableLiveData<String>()
    val data: LiveData<String> get() = _data

    private val _numberPhone = MutableLiveData<String>("")
    val numberPhone: LiveData<String> get() = _numberPhone

    private var jobMutilDeleteDelay: Job? = null


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

    fun clickButtonNormal(text: String) {
        _numberPhone.value = _numberPhone.value + text
    }

    fun clearTextNumberPhone() {
        _numberPhone.value = ""
    }

    private fun clickDeleteNumberPhone() {
        if (_numberPhone.value?.isNotEmpty() == true) {
            val pos = _numberPhone.value?.length ?: 0
            _numberPhone.value = _numberPhone.value?.substring(0, pos - 1)
        } else {
            removeMultipleDelete()
        }
    }

    private var timeDelayBetweenDelete = TIME_DELAY_MULTIPLE_DELETE

    private fun progressiveReductionSpeedDelete() {
        if (timeDelayBetweenDelete > 300) {
            timeDelayBetweenDelete -= 200
        }
    }

    fun multipleClickDelete() {
        removeMultipleDelete()
        timeDelayBetweenDelete = TIME_DELAY_MULTIPLE_DELETE
        jobMutilDeleteDelay = viewModelScope.launch {
            while (isActive) {
                progressiveReductionSpeedDelete()
                clickDeleteNumberPhone()
                delay(timeDelayBetweenDelete)
            }
        }
    }

    fun removeMultipleDelete() {
        jobMutilDeleteDelay?.cancel()
        jobMutilDeleteDelay = null
    }

    companion object {
        const val TIME_DELAY_MULTIPLE_DELETE = 800L
    }
}

