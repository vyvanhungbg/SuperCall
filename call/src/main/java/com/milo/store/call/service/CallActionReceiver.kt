package com.milo.store.call.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.milo.store.call.extension.getStartIntent
import com.milo.store.call.helper.CallManager


private const val PACKAGE = "com.milo.store.call.service."
const val ACCEPT_CALL = PACKAGE + "ACCEPT_CALL"
const val DECLINE_CALL = PACKAGE + "DECLINE_CALL"
class CallActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACCEPT_CALL -> {
                context.startActivity(getStartIntent(context, false))
                CallManager.accept()
            }

            DECLINE_CALL -> CallManager.reject()
        }
    }
}
