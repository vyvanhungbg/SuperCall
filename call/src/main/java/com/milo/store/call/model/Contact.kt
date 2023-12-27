package com.milo.store.call.model

import android.graphics.Bitmap

data class Contact(
    var id: Int = 0,
    var prefix: String = "",
    var firstName: String = "",
    var middleName: String = "",
    var surname: String = "",
    var suffix: String = "",
    var nickname: String = "",
    var photoUri: String = "",
    var phoneNumbers: ArrayList<PhoneNumber> = arrayListOf(),
    var region: String = "",
    var source: String = "",
    var contactId: Int = 0,
    var thumbnailUri: String = "",
    var photo: Bitmap? = null,
    var mimetype: String = "",
    var ringtone: String? = ""
){
    val rawId = id
    val name = getNameToDisplay()

    private fun getNameToDisplay(): String {
        val firstMiddle = "$firstName $middleName".trim()
        val suffixComma = if (suffix.isEmpty()) "" else ", $suffix"
        val fullName = "$prefix $firstMiddle $surname$suffixComma".trim()
        return fullName
    }
}
