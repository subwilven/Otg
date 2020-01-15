package com.islam.otgtask

import android.annotation.SuppressLint
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.islam.otgtask.project_base.POJO.Message
import com.islam.otgtask.project_base.common.boradcast.AlerterReceiver
import com.islam.otgtask.project_base.common.boradcast.ConnectivityReceiver
import com.islam.otgtask.project_base.utils.LocalManager
import com.islam.otgtask.project_base.utils.NotificationManager

class MyApplication : Application() {


    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }

        @SuppressLint("StaticFieldLeak")
        @get:Synchronized
        var instance: MyApplication? = null
            private set

    }



    internal var connectivityReceivern: BroadcastReceiver? = null
    internal var alerterReceiver: BroadcastReceiver? = null

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocalManager.updateResources(base, LocalManager.getLanguage(base)))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocalManager.setLocale(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        initConnectivityBroadcast()
        initAlerterBroadcast()
        NotificationManager.initNotificationChannels(this)

    }

    private fun initAlerterBroadcast() {
        alerterReceiver = AlerterReceiver()
        val filter = IntentFilter("action_alert")
        LocalBroadcastManager.getInstance(this).registerReceiver(alerterReceiver!!, filter)
    }

    private fun initConnectivityBroadcast() {
        connectivityReceivern = ConnectivityReceiver()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(connectivityReceivern, filter)
    }

    fun showAlertBar(message: Message){
        val intent = Intent("action_alert")
        intent.putExtra("message", message.getValue(this))
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }
    fun setConnectivityListener(listener: ConnectivityReceiver.ConnectivityReceiverListener?) {
        ConnectivityReceiver.listener = listener
    }
    fun setAlerterListener(listener: AlerterReceiver.AlerterReceiverListener?) {
        AlerterReceiver.listener = listener
    }
    override fun onTerminate() {
        unregisterReceiver(connectivityReceivern)
        LocalBroadcastManager.getInstance(this).unregisterReceiver(alerterReceiver!!)
        super.onTerminate()
    }
}
