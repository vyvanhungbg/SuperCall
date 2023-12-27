package com.milo.store.call.model

data class PhoneNumber(var value: String, var type: Int, var label: String, var normalizedNumber: String, var isPrimary: Boolean = false)