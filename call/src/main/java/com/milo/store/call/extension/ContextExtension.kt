package com.milo.store.call.extension

import android.app.NotificationManager
import android.app.role.RoleManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.BlockedNumberContract
import android.provider.ContactsContract
import android.telecom.TelecomManager
import android.view.WindowManager
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.loader.content.CursorLoader
import com.milo.store.call.R
import com.milo.store.call.helper.MyContactsContentProvider
import com.milo.store.call.model.BlockedNumber


/**
- Create by :Vy Hùng
- Create at :27,December,2023
 **/

fun Context.isNumberBlocked(
    number: String,
    blockedNumbers: ArrayList<BlockedNumber> = getBlockedNumbers()
): Boolean {
    val numberToCompare = number.trimToComparableNumber()
    return blockedNumbers.any { numberToCompare == it.numberToCompare || numberToCompare == it.number } || isNumberBlockedByPattern(
        number,
        blockedNumbers
    )
}

val Context.telecomManager: TelecomManager get() = getSystemService(Context.TELECOM_SERVICE) as TelecomManager
val Context.windowManager: WindowManager get() = getSystemService(Context.WINDOW_SERVICE) as WindowManager
val Context.notificationManager: NotificationManager get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N)
fun isNougatPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N_MR1)
fun isNougatMR1Plus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O)
fun isOreoPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O_MR1)
fun isOreoMr1Plus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.P)
fun isPiePlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
fun isQPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
fun isRPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun isSPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
fun isTiramisuPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

fun Context.getBlockedNumbers(): ArrayList<BlockedNumber> {
    val blockedNumbers = ArrayList<BlockedNumber>()
    if (!isNougatPlus() || !isDefaultDialer()) {
        return blockedNumbers
    }

    val uri = BlockedNumberContract.BlockedNumbers.CONTENT_URI
    val projection = arrayOf(
        BlockedNumberContract.BlockedNumbers.COLUMN_ID,
        BlockedNumberContract.BlockedNumbers.COLUMN_ORIGINAL_NUMBER,
        BlockedNumberContract.BlockedNumbers.COLUMN_E164_NUMBER
    )

    queryCursor(uri, projection) { cursor ->
        val id = cursor.getLongValue(BlockedNumberContract.BlockedNumbers.COLUMN_ID)
        val number =
            cursor.getStringValue(BlockedNumberContract.BlockedNumbers.COLUMN_ORIGINAL_NUMBER) ?: ""
        val normalizedNumber =
            cursor.getStringValue(BlockedNumberContract.BlockedNumbers.COLUMN_E164_NUMBER) ?: number
        val comparableNumber = normalizedNumber.trimToComparableNumber()
        val blockedNumber = BlockedNumber(id, number, normalizedNumber, comparableNumber)
        blockedNumbers.add(blockedNumber)
    }

    return blockedNumbers
}

fun String.trimToComparableNumber(): String {
    // don't trim if it's not a phone number
    if (!this.isPhoneNumber()) {
        return this
    }
    val normalizedNumber = this.normalizeString()
    val startIndex = Math.max(0, normalizedNumber.length - 9)
    return normalizedNumber.substring(startIndex)
}

fun String.isPhoneNumber(): Boolean {
    return this.matches("^[0-9+\\-\\)\\( *#]+\$".toRegex())
}

fun Context.isDefaultDialer(): Boolean {
    return if (isQPlus()) {
        val roleManager = getSystemService(RoleManager::class.java)
        (roleManager?.isRoleAvailable(RoleManager.ROLE_DIALER) ?: false) && roleManager.isRoleHeld(
            RoleManager.ROLE_DIALER
        )
    } else {
        telecomManager.defaultDialerPackage == packageName
    }
}

fun Context.queryCursor(
    uri: Uri,
    projection: Array<String>,
    selection: String? = null,
    selectionArgs: Array<String>? = null,
    sortOrder: String? = null,
    showErrors: Boolean = false,
    callback: (cursor: Cursor) -> Unit
) {
    try {
        val cursor = contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)
        cursor?.use {
            if (cursor.moveToFirst()) {
                do {
                    callback(cursor)
                } while (cursor.moveToNext())
            }
        }
    } catch (e: Exception) {

    }
}


fun Context.isNumberBlockedByPattern(
    number: String,
    blockedNumbers: ArrayList<BlockedNumber> = getBlockedNumbers()
): Boolean {
    for (blockedNumber in blockedNumbers) {
        val num = blockedNumber.number
        if (num.isBlockedNumberPattern()) {
            val pattern = num.replace("+", "\\+").replace("*", ".*")
            if (number.matches(pattern.toRegex())) {
                return true
            }
        }
    }
    return false
}

fun String.isBlockedNumberPattern() = contains("*")

fun Context.getMyContactsCursor(favoritesOnly: Boolean, withPhoneNumbersOnly: Boolean) = try {
    val getFavoritesOnly = if (favoritesOnly) "1" else "0"
    val getWithPhoneNumbersOnly = if (withPhoneNumbersOnly) "1" else "0"
    val args = arrayOf(getFavoritesOnly, getWithPhoneNumbersOnly)
    CursorLoader(
        this,
        MyContactsContentProvider.CONTACTS_CONTENT_URI,
        null,
        null,
        args,
        null
    ).loadInBackground()
} catch (e: Exception) {
    null
}


fun Context.getPhoneNumberTypeText(type: Int, label: String): String {
    return if (type == ContactsContract.CommonDataKinds.BaseTypes.TYPE_CUSTOM) {
        label
    } else {
        getString(
            when (type) {
                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> R.string.mobile
                ContactsContract.CommonDataKinds.Phone.TYPE_HOME -> R.string.home
                ContactsContract.CommonDataKinds.Phone.TYPE_WORK -> R.string.work
                ContactsContract.CommonDataKinds.Phone.TYPE_MAIN -> R.string.main_number
                ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK -> R.string.work_fax
                ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME -> R.string.home_fax
                ContactsContract.CommonDataKinds.Phone.TYPE_PAGER -> R.string.pager
                else -> R.string.other
            }
        )
    }
}

val Context.powerManager: PowerManager get() = getSystemService(Context.POWER_SERVICE) as PowerManager