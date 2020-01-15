package com.islam.otgtask.project_base.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

object AppManager {

    fun openPlayStoreForApp(context: Context) {
        val appPackageName = context.packageName
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$appPackageName")))
        } catch (e: android.content.ActivityNotFoundException) {
            context.startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
        }

    }

    fun checkForAppUpdates(context: Activity, onUpdateResult: (b: Boolean) -> Unit) {

        // Creates instance of the manager.
        val appUpdateManager = AppUpdateManagerFactory.create(context)

        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            onUpdateResult(appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE)
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, context, 102)}
        appUpdateInfoTask.addOnFailureListener { onUpdateResult(false)}
    }


}
