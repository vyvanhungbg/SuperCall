package com.milo.store.call.helper

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.net.Uri
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.provider.MediaStore
import android.telecom.Call
import android.util.Log
import android.util.Size
import androidx.core.content.ContextCompat
import com.milo.store.call.R
import com.milo.store.call.extension.getIntValueOrNull
import com.milo.store.call.extension.getLongValue
import com.milo.store.call.extension.getStringValueOrNull
import com.milo.store.call.extension.isQPlus
import com.milo.store.call.model.Contact
import com.milo.store.call.model.PhoneNumber
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
- Create by :Vy Hùng
- Create at :27,December,2023
 **/
private const val TAG = "CallContactInfo"

object CallContactInfo {

    @SuppressLint("Range")
    fun getCallContact2(context: Context, call: Call?, callback: (Contact) -> Unit) {
        val handle = call?.details?.handle

        var nameContact = ""
        var firstPhotoUri = ""
        val numberBackup = call?.details?.handle?.schemeSpecificPart ?: ""
        val contactDefault = Contact(
            firstName = "",
            middleName = "Unknown",
            phoneNumbers = ArrayList<PhoneNumber>(
                listOf(
                    PhoneNumber(
                        numberBackup,
                        Phone.TYPE_MOBILE,
                        numberBackup,
                    )
                )
            )
        )
        try {

            if (handle == null) {
                callback(contactDefault)
            } else {
                val uriTmp = Uri.decode(handle.toString())
                val number = uriTmp.substringAfter("tel:")

                val handler = CoroutineExceptionHandler { _, exception ->
                    Log.e(TAG, "CoroutineExceptionHandler got $exception")
                    callback(
                        contactDefault
                    )
                }
                CoroutineScope(Dispatchers.IO + handler).launch {
                    getContactWithMutilPhoneNumber(context, number) { _name, _phoneUriPhoto ->
                        nameContact = _name
                        firstPhotoUri = _phoneUriPhoto
                    }

                    val uri = Uri.withAppendedPath(
                        ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                        Uri.encode(number)
                    )
                    val projection = arrayOf(
                        ContactsContract.PhoneLookup.DISPLAY_NAME,
                        ContactsContract.PhoneLookup.PHOTO_URI,
                    )

                    val cursor = context.contentResolver.query(
                        uri,
                        projection,
                        null,
                        null,
                        null
                    )

                    cursor?.use {
                        if (cursor.moveToFirst()) {
                            val displayName =
                                cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME))
                                    ?: ""

                            val photoAvatarUri =
                                cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.PHOTO_URI))
                                    ?: ""
                            if (nameContact.isEmpty()) {
                                nameContact = displayName
                            }
                            if (firstPhotoUri.isEmpty()) {
                                firstPhotoUri = photoAvatarUri
                            }
                            withContext(Dispatchers.Main) {
                                callback(
                                    Contact(
                                        firstName = "",
                                        middleName = nameContact,
                                        photoUri = firstPhotoUri,
                                        phoneNumbers = ArrayList<PhoneNumber>(
                                            listOf(
                                                PhoneNumber(
                                                    number,
                                                    Phone.TYPE_MOBILE,
                                                    number,
                                                )
                                            )
                                        )
                                    )
                                )
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                callback(
                                    contactDefault
                                )
                            }
                        }
                    } ?: run {
                        withContext(Dispatchers.Main) {
                            callback(
                                contactDefault
                            )
                        }
                    }
                    cursor?.close()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "getCallContact2 error: ${e.message}")
            callback(
                contactDefault
            )
        }
    }

    @SuppressLint("Range")
    fun getContactWithMutilPhoneNumber(
        context: Context,
        phoneNumber: String,
        callBack: (String, String) -> Unit
    ) {
        var allNameDisplay = ""
        var firstUriPhoto = ""
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
        )

        val selection = "${ContactsContract.CommonDataKinds.Phone.NUMBER} = ?"
        val selectionArgs = arrayOf(phoneNumber)

        val sortOrder = "${ContactsContract.Contacts.DISPLAY_NAME} ASC"

        val cursor = context.contentResolver.query(
            uri,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )
        try {

            if (cursor != null && cursor.moveToFirst()) {
                val contactNames = mutableListOf<String>()
                val photoUri = mutableListOf<String>()
                do {
                    val contactName =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                            ?: ""
                    contactNames.add(contactName)

                    val uri =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
                            ?: ""
                    photoUri.add(uri)
                } while (cursor.moveToNext())


                for (name in contactNames) {
                    Log.e(TAG, name)
                }
                allNameDisplay = contactNames.joinToString(" | ")

                firstUriPhoto = if (photoUri.any { it.isNotEmpty() }) {
                    photoUri.first { it.isNotEmpty() }
                } else {
                    ""
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "getContact3: ${e.message}")
        } finally {
            cursor?.close()
        }

        return callBack(allNameDisplay, firstUriPhoto)
    }


    fun getCallContactAvatar(context: Context, callContact: Contact): Bitmap? {
        var bitmap: Bitmap? = null
        if (callContact.photoUri.isNotEmpty()) {
            val photoUri = Uri.parse(callContact.photoUri)
            try {
                val contentResolver = context.contentResolver
                bitmap = if (isQPlus()) {
                    val tmbSize = context.resources.getDimension(R.dimen.avatar_size).toInt()
                    contentResolver.loadThumbnail(photoUri, Size(tmbSize, tmbSize), null)
                } else {
                    MediaStore.Images.Media.getBitmap(contentResolver, photoUri)
                }
                bitmap = getCircularBitmap(bitmap!!)
            } catch (ignored: Exception) {
                return null
            }
        }
        return bitmap
    }

    fun getCircularBitmap(bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.width, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val radius = bitmap.width / 2.toFloat()

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawCircle(radius, radius, radius, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }


    fun getAllPhoneContacts(
        context: Context,
        onSuccess: (List<Contact>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val hasPermissionContact = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermissionContact) {
            onFailure.invoke(Exception("Not permission"))
            return
        }

        val contentResolver = context.contentResolver

        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY,
            ContactsContract.CommonDataKinds.StructuredName.SUFFIX,
            ContactsContract.CommonDataKinds.StructuredName.PREFIX,
            ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME,
            ContactsContract.CommonDataKinds.Nickname.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER,
            ContactsContract.CommonDataKinds.Phone.CONTACT_STATUS_LABEL,
            ContactsContract.CommonDataKinds.Phone.CUSTOM_RINGTONE,
            ContactsContract.CommonDataKinds.Phone.TYPE,
        )
        val sortOrder = ContactsContract.Data.DISPLAY_NAME_PRIMARY + " COLLATE NOCASE ASC";

        val cursor = contentResolver.query(
            Phone.CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )
        val mapContact = mutableMapOf<Long, Contact>()
        val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            onFailure.invoke(Exception(throwable.message))
        }
        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
            if (cursor != null) {
                try {
                    while (cursor.moveToNext()) {
                        val contactId =
                            cursor.getLongValue(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
                        val displayName =
                            cursor.getStringValueOrNull(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY)
                        val phoneNumber =
                            cursor.getStringValueOrNull(ContactsContract.CommonDataKinds.Phone.NUMBER)

                        val phoneNumberNormalizer =
                            cursor.getStringValueOrNull(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER)
                        val avatar =
                            cursor.getStringValueOrNull(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)
                        val status =
                            cursor.getStringValueOrNull(ContactsContract.CommonDataKinds.Phone.CONTACT_STATUS_LABEL)
                        val customRingtone =
                            cursor.getStringValueOrNull(ContactsContract.CommonDataKinds.Phone.CUSTOM_RINGTONE)
                        val type =
                            cursor.getIntValueOrNull(ContactsContract.CommonDataKinds.Phone.TYPE)
                        val suffix =
                            cursor.getStringValueOrNull(ContactsContract.CommonDataKinds.StructuredName.SUFFIX)
                        val prefix =
                            cursor.getStringValueOrNull(ContactsContract.CommonDataKinds.StructuredName.PREFIX)
                        val middleName =
                            cursor.getStringValueOrNull(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME)
                        val nickName =
                            cursor.getStringValueOrNull(ContactsContract.CommonDataKinds.Nickname.DISPLAY_NAME)


                        val contact = Contact(
                            contactId = contactId,
                            prefix = prefix ?: "",
                            suffix = suffix ?: "",
                            firstName = displayName ?: "",
                            middleName = middleName ?: "",
                            photoUri = avatar ?: "",
                            ringtone = customRingtone
                        )

                        val phoneNumberObject = PhoneNumber(
                            value = phoneNumber ?: phoneNumberNormalizer ?: "",
                            type = type ?: Phone.TYPE_MOBILE,
                            normalizedNumber = phoneNumberNormalizer ?: phoneNumber ?: "",
                        )
                        if (mapContact.containsKey(contactId)) {
                            val oldNumber =
                                mapContact[contactId]?.phoneNumbers?.toMutableList()
                                    ?: mutableListOf()
                            oldNumber.add(phoneNumberObject)
                            mapContact[contactId] = contact.copy(phoneNumbers = oldNumber)
                        } else {
                            mapContact[contactId] =
                                contact.copy(phoneNumbers = listOf(phoneNumberObject))
                        }

                    }
                    withContext(Dispatchers.Main) {
                        onSuccess.invoke(mapContact.values.toList())
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        onFailure.invoke(e)
                    }
                } finally {
                    cursor.close()
                }
            }
        }

    }
}