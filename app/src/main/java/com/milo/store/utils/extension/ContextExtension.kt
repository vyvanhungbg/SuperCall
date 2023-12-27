package com.milo.store.utils.extension

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.app.role.RoleManager
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.telecom.TelecomManager
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.milo.store.BuildConfig
import com.milo.store.call.extension.isQPlus
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

fun Context.showToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showToast(resource: Int) {
    Toast.makeText(this, resource, Toast.LENGTH_SHORT).show()
}

fun Context.color(@ColorRes color: Int): Int {
    return ContextCompat.getColor(this, color)
}

fun Context.drawable(@DrawableRes drawable: Int): Drawable? {
    return ContextCompat.getDrawable(this, drawable)
}

fun Context.checkIsAppCallDefault(): Boolean {
    return if (isQPlus()) {
        val roleManager = this.getSystemService(RoleManager::class.java)
        (roleManager?.isRoleAvailable(RoleManager.ROLE_DIALER) ?: false) && roleManager.isRoleHeld(
            RoleManager.ROLE_DIALER
        )
    } else {
        this.getSystemService(TelecomManager::class.java)?.defaultDialerPackage == packageName
    }
}

fun Context.pickDateTime(action: (hour: Int, minute: Int) -> Unit) {
    Locale.setDefault(Locale("vi"))
    val currentDateTime = Calendar.getInstance()
    val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
    val startMinute = currentDateTime.get(Calendar.MINUTE)
    TimePickerDialog(
        this,
        AlertDialog.THEME_HOLO_LIGHT,
        TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            action(
                hour,
                minute
            )
        },
        startHour,
        startMinute,
        true
    ).apply {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        show()
    }
}

//fun Context.sendEmailReport(
//    addresses: Array<String> = arrayOf("milostorefeedback@gmail.com"), test: TestEntity
//) {
//
//    fun convertQuestion(question: Question): String {
//        return try {
//            val content = question.content?.deCode() ?: ""
//            "\n Mã             : ${question.id}" +
//                    "\n Tóm tắt câu hỏi: ${
//                        content.substring(
//                            0,
//                            if (content.length > 100) 100 else content.length
//                        )
//                    }"
//        } catch (e: Exception) {
//            ""
//        }
//    }
//    try {
//        val intent = Intent(Intent.ACTION_SENDTO)
//        intent.putExtra(Intent.EXTRA_EMAIL, addresses)
//        intent.data = Uri.parse("mailto:")
//        intent.putExtra(
//            Intent.EXTRA_SUBJECT,
//            "Báo cáo của bạn với hệ thống hoặc thông tin bài thi !"
//        )
//
//        intent.putExtra(
//            Intent.EXTRA_TEXT,
//            "\n\n\n" + "Thông tin về dòng máy của bạn: " + "\n\n" + getDeviceInfo() +
//                    "\n\n" + "Bài test: " +
//                    "\n Thời gian       : ${test.createdAt?.toDateTimeForHuman()}" +
//                    "\n Số câu đúng     : ${test.correctAnswer}" +
//                    "\n Số câu sai      : ${test.wrongAnswer}" +
//                    "\n Số câu bỏ trống : ${test.ignoreAnswer} " +
//                    "\n Vượt qua        : ${if (test.passTest) "Có" else "Không"} " +
//                    "\n Lí do trượt     : ${test.reason} " +
//                    "\n Lí do trượt     : ${test.reason} " +
//                    "\n Danh sách thứ tự câu hỏi     : " +
//                    "\n ${test.questions?.map { convertQuestion(it) }} " +
//                    "\n Ý kiến của bạn  : " +
//                    "\n Về bài thi    : " +
//                    "\n Về giao diện  : " +
//                    "\n Về chức năng  : "
//        )
//        startActivity(intent)
//    } catch (e: Exception) {
//        Toast.makeText(this, "Gửi thất bại !. Vui lòng kiểm tra lại gmail", Toast.LENGTH_SHORT)
//            .show()
//    }
//}

private fun getDeviceInfo(): String {
    try {
        val densityText = when (Resources.getSystem().displayMetrics.densityDpi) {
            DisplayMetrics.DENSITY_LOW -> "LDPI"
            DisplayMetrics.DENSITY_MEDIUM -> "MDPI"
            DisplayMetrics.DENSITY_HIGH -> "HDPI"
            DisplayMetrics.DENSITY_XHIGH -> "XHDPI"
            DisplayMetrics.DENSITY_XXHIGH -> "XXHDPI"
            DisplayMetrics.DENSITY_XXXHIGH -> "XXXHDPI"
            else -> "HDPI"
        }


        val stat = StatFs(Environment.getExternalStorageDirectory().path)
        var megAvailable = 0L
        val bytesAvailable: Long
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            bytesAvailable = stat.blockSizeLong * stat.availableBlocksLong
            megAvailable = bytesAvailable / (1024 * 1024)
        }


        return "Manufacturer ${Build.MANUFACTURER}, Model ${Build.MODEL}," + " ${Locale.getDefault()}, " + "osVer ${Build.VERSION.RELEASE}, Screen ${Resources.getSystem().displayMetrics.widthPixels}x${Resources.getSystem().displayMetrics.heightPixels}, " + "$densityText, Free space ${megAvailable}MB, TimeZone ${
            TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT)
        }"
    } catch (e: Exception) {
        return ""
    }
}