package com.islam.otgtask.project_base.common.boradcast


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import com.islam.otgtask.project_base.utils.NetworkManager

class ConnectivityReceiver : BroadcastReceiver() {

    companion object {
        var listener: ConnectivityReceiverListener? = null
    }

    override fun onReceive(context: Context, arg1: Intent) {
        val isConnected = NetworkManager.isNetworkConnected(context)
        if (listener != null) {
            listener!!.onNetworkConnectionChanged(isConnected)
        }
    }

     interface ConnectivityReceiverListener {
          fun onNetworkConnectionChanged(isConnected: Boolean?)
    }


}