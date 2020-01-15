package com.islam.otgtask.project_base.common.boradcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlerterReceiver : BroadcastReceiver() {

    interface AlerterReceiverListener {
        fun onAlertReceived(message: String)
    }

    companion object {
        var listener: AlerterReceiverListener? = null
    }


    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("message") ?: ""
        listener?.onAlertReceived(message)
    }
}