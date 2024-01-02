package com.milo.store.call.model

import android.graphics.Bitmap

data class Contact(
    var prefix: String = "",
    var firstName: String = "",
    var middleName: String = "",
    var surname: String = "",
    var suffix: String = "",
    var photoUri: String = "",
    var phoneNumbers: List<PhoneNumber> = listOf(),
    var contactId: Long = 0L,
    var ringtone: String? = ""
){
    val rawId = contactId
    val name = getNameToDisplay()

     fun getNameToDisplay(): String {
        val firstMiddle = "$firstName $middleName".trim()
        val suffixComma = if (suffix.isEmpty()) "" else ", $suffix"
        val fullName = "$prefix $firstMiddle $surname$suffixComma".trim()
        return fullName
    }

    fun getPhoneNumbersDisplay() = phoneNumbers.map { it.value }.firstOrNull() ?: ""
}
