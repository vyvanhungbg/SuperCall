package com.milo.store.call.helper

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.telecom.Call
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.milo.store.call.R
import com.milo.store.call.extension.getStartIntent
import com.milo.store.call.extension.isOreoPlus
import com.milo.store.call.extension.isOutgoing
import com.milo.store.call.extension.notificationManager
import com.milo.store.call.extension.powerManager
import com.milo.store.call.helper.CallContactInfo.getCallContact2
import com.milo.store.call.service.ACCEPT_CALL
import com.milo.store.call.service.CallActionReceiver
import com.milo.store.call.service.DECLINE_CALL


/**
- Create by :Vy Hùng
- Create at :27,December,2023
 **/

object CallNotification {


    private val CALL_NOTIFICATION_ID = 84933
    private val ACCEPT_CALL_CODE = 0
    private val DECLINE_CALL_CODE = 1


    fun setupNotification(context: Context, call: Call) {
        val notificationManager = context.notificationManager
        getCallContact2(context.applicationContext, CallManager.getPrimaryCall()) { callContact ->
            val callContactAvatar = CallContactInfo.getCallContactAvatar(context, callContact)
            val callState = CallManager.getState()
            val isHighPriority =
                context.powerManager.isInteractive && callState == Call.STATE_RINGING
            val channelId =
                if (isHighPriority) "simple_dialer_call_high_priority" else "simple_dialer_call"
            if (isOreoPlus()) {
                val importance =
                    if (isHighPriority) NotificationManager.IMPORTANCE_HIGH else NotificationManager.IMPORTANCE_DEFAULT
                val name =
                    if (isHighPriority) "call_notification_channel_high_priority" else "call_notification_channel"

                NotificationChannel(channelId, name, importance).apply {
                    setSound(null, null)
                    notificationManager.createNotificationChannel(this)
                }
            }

            val openAppIntent = getStartIntent(context, call.isOutgoing())
            val openAppPendingIntent =
                PendingIntent.getActivity(context, 0, openAppIntent, PendingIntent.FLAG_MUTABLE)

            val acceptCallIntent = Intent(context, CallActionReceiver::class.java)
            acceptCallIntent.action = ACCEPT_CALL
            val acceptPendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    ACCEPT_CALL_CODE,
                    acceptCallIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_MUTABLE
                )

            val declineCallIntent = Intent(context, CallActionReceiver::class.java)
            declineCallIntent.action = DECLINE_CALL
            val declinePendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    DECLINE_CALL_CODE,
                    declineCallIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_MUTABLE
                )

            var callerName =
                if (callContact.name.isNotEmpty()) callContact.name else context.getString(R.string.unknown_caller)
            if (callContact.phoneNumbers.isNotEmpty()) {
                callerName += " - ${callContact.phoneNumbers.first().normalizedNumber}"
            }

            val contentTextId = when (callState) {
                Call.STATE_RINGING -> R.string.is_calling
                Call.STATE_DIALING -> R.string.dialing
                Call.STATE_DISCONNECTED -> R.string.call_ended
                Call.STATE_DISCONNECTING -> R.string.call_ending
                else -> R.string.ongoing_call
            }

            val collapsedView = RemoteViews(context.packageName, R.layout.call_notification).apply {

                setTextViewText(R.id.notification_caller_name, callerName)
                setTextViewText(R.id.notification_call_status, context.getString(contentTextId))
                setViewVisibility(
                    R.id.notification_accept_call,
                    if (callState == Call.STATE_RINGING) View.VISIBLE else View.GONE
                )

                setOnClickPendingIntent(R.id.notification_decline_call, declinePendingIntent)
                setOnClickPendingIntent(R.id.notification_accept_call, acceptPendingIntent)

                if (callContactAvatar != null) {
                    setImageViewBitmap(
                        R.id.notification_thumbnail,
                        CallContactInfo.getCircularBitmap(callContactAvatar)
                    )
                }
            }

            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_headset_vector)
                .setContentIntent(openAppPendingIntent)
                .setPriority(if (isHighPriority) NotificationCompat.PRIORITY_MAX else NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(Notification.CATEGORY_CALL)
                .setCustomContentView(collapsedView)
                .setOngoing(true)
                .setSound(null)
                .setUsesChronometer(callState == Call.STATE_ACTIVE)
                .setChannelId(channelId)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())

            if (isHighPriority) {
                builder.setFullScreenIntent(openAppPendingIntent, true)
            }

            val notification = builder.build()
            // it's rare but possible for the call state to change by now
            if (CallManager.getState() == callState) {
                notificationManager.notify(CALL_NOTIFICATION_ID, notification)
            }
        }
    }

    fun cancelNotification(context: Context) {
        context.notificationManager.cancel(CALL_NOTIFICATION_ID)
    }
}