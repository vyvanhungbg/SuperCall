package com.milo.store.utils.extension

import android.view.View
import androidx.core.view.isVisible


fun View.hide() {
    this.isVisible = false
}

fun View.show() {
    this.isVisible = true
}

fun View.setPreventDoubleClick(debounceTime: Long = 500, action: () -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0
        override fun onClick(v: View?) {
            if (System.currentTimeMillis() - lastClickTime < debounceTime) return
            action.invoke()
            lastClickTime = System.currentTimeMillis()
        }
    })
}


/*fun View.enableDisableView(enabled: Boolean) {
    val view = this
    try {
        view.isEnabled = enabled
        if (view is ViewGroup) {
            val group = view
            for (idx in 0 until group.childCount) {
                view.enableDisableView(enabled)
            }
        }
    } catch (ex: Exception) {
        logError(message = ex.message)
    }
}*/


