package com.milo.store.call.extension

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telecom.Call


/**
- Create by :Vy Hùng
- Create at :27,December,2023
 **/

private val OUTGOING_CALL_STATES = arrayOf(
    Call.STATE_CONNECTING,
    Call.STATE_DIALING,
    Call.STATE_SELECT_PHONE_ACCOUNT
)

fun Call?.getStateCompat(): Int {
    return when {
        this == null -> Call.STATE_DISCONNECTED
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> details.state
        else -> state
    }
}

fun getStartIntent(context: Context, isOutGoing: Boolean): Intent {
    val openAppIntent = Intent()
    if (isOutGoing){
        openAppIntent.component = ComponentName(context, "com.milo.store.presentation.call.outgoing.OutgoingCallActivity")
    } else{
        openAppIntent.component = ComponentName(context, "com.milo.store.presentation.call.incomming.IncommingCallActivity")
    }
    openAppIntent.flags =
        Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
    return openAppIntent
}

fun Call?.getCallDuration(): Int {
    return if (this != null) {
        val connectTimeMillis = details.connectTimeMillis
        if (connectTimeMillis == 0L) {
            return 0
        }
        ((System.currentTimeMillis() - connectTimeMillis) / 1000).toInt()
    } else {
        0
    }
}

fun Call.isOutgoing(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        details.callDirection == Call.Details.DIRECTION_OUTGOING
    } else {
        OUTGOING_CALL_STATES.contains(getStateCompat())
    }
}

fun Call.hasCapability(capability: Int): Boolean = (details.callCapabilities and capability) != 0

fun Call?.isConference(): Boolean = this?.details?.hasProperty(Call.Details.PROPERTY_CONFERENCE) == true