package com.milo.store.call.extension

import android.telephony.PhoneNumberUtils
import java.text.Normalizer
import java.util.Locale


/**
- Create by :Vy Hùng
- Create at :27,December,2023
 **/

val normalizeRegex = "\\p{InCombiningDiacriticalMarks}+".toRegex()

fun String.normalizeString() = Normalizer.normalize(this, Normalizer.Form.NFD).replace(normalizeRegex, "")

fun String.normalizePhoneNumber() = PhoneNumberUtils.normalizeNumber(this)

fun String.getNameLetter() = normalizeString().toCharArray().getOrNull(0)?.toString()?.toUpperCase(
    Locale.getDefault()) ?: "A"