package com.milo.store.call.service

import android.app.KeyguardManager
import android.content.Context
import android.telecom.Call
import android.telecom.CallAudioState
import android.telecom.InCallService
import android.util.Log
import com.milo.store.call.extension.getStartIntent
import com.milo.store.call.extension.isOutgoing
import com.milo.store.call.extension.powerManager
import com.milo.store.call.helper.CallManager
import com.milo.store.call.helper.CallNotification
import com.milo.store.call.helper.NoCall


class CallService : InCallService() {

    private val callListener = object : Call.Callback() {
        override fun onStateChanged(call: Call, state: Int) {
            super.onStateChanged(call, state)
            if (state == Call.STATE_DISCONNECTED || state == Call.STATE_DISCONNECTING) {
                CallNotification.cancelNotification(this@CallService)
            } else {
                CallNotification.setupNotification(this@CallService, call)
            }
        }
    }

    override fun onCallAdded(call: Call) {
        super.onCallAdded(call)
        CallManager.onCallAdded(call)
        CallManager.inCallService = this
        call.registerCallback(callListener)

        val isScreenLocked =
            (getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager).isDeviceLocked
        if (!powerManager.isInteractive || call.isOutgoing() || isScreenLocked ) {
            try {
                CallNotification.setupNotification(this@CallService, call)
                startActivity(getStartIntent(this, call.isOutgoing()))
            } catch (e: Exception) {
                // seems like startActivity can throw AndroidRuntimeException and ActivityNotFoundException, not yet sure when and why, lets show a notification
                CallNotification.setupNotification(this@CallService, call)
            }
        } else {
            CallNotification.setupNotification(this@CallService, call)
        }
    }

    override fun onCallRemoved(call: Call) {
        super.onCallRemoved(call)
        call.unregisterCallback(callListener)
        val wasPrimaryCall = call == CallManager.getPrimaryCall()
        CallManager.onCallRemoved(call)
        if (CallManager.getPhoneState() == NoCall) {
            CallManager.inCallService = null
            CallNotification.cancelNotification(this@CallService)
        } else {
            CallNotification.setupNotification(this@CallService, call)
            if (wasPrimaryCall) {
                startActivity(getStartIntent(this, call.isOutgoing()))
            }
        }
    }

    override fun onCallAudioStateChanged(audioState: CallAudioState?) {
        super.onCallAudioStateChanged(audioState)
        if (audioState != null) {
            CallManager.onAudioStateChanged(audioState)
        }
    }

    override fun onDestroy() {
        CallNotification.cancelNotification(this@CallService)
        super.onDestroy()
    }
}
