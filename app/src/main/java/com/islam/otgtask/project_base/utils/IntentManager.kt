package com.islam.otgtask.project_base.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import com.islam.otgtask.R
import com.islam.otgtask.project_base.POJO.Message
import java.util.*


object IntentManager {

    private fun isPackageInstalledAndEnabled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getApplicationInfo(packageName, 0).enabled
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }

    }

    private fun launchExternalApp(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        context.startActivity(intent)
    }

    fun openTabBrowser(context: Context, url: String) {
    }


    fun openAppSettings(context: Context) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

    fun openGoogleMap(context: Context, lat: Double, lon: Double) {
        val uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", lat, lon)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        context.startActivity(intent)
    }

    fun openEmail(context: Context, mailTo: String) {
        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", mailTo, null))
        context.startActivity(Intent.createChooser(emailIntent, "Send email..."))
    }


    fun openFacebookPage(context: Context) {
        val isInstalled = isPackageInstalledAndEnabled("com.facebook.katana",
                context.packageManager)

        if (isInstalled) {
            launchExternalApp(context, "fb://page/YOUR_PAGE_ID")
            //ex:fb://page/314086229325675
        } else {
            openTabBrowser(context, "https://www.facebook.com/YOUR_PAGE_URL")
            //ex :https://www.facebook.com/thaipeoplepowerpartypage
        }
    }

    fun openTwitterPage(context: Context) {
        val isInstalled = isPackageInstalledAndEnabled("com.twitter.android",
                context.packageManager)

        if (isInstalled) {
            launchExternalApp(context, "twitter://user?user_id=YOUR_USER_ID")
            //ex :twitter://user?user_id=1049571254137643009
        } else {
            openTabBrowser(context, "https://twitter.com/YOUR_PAGE_URL")
            //ex :https://twitter.com/0xREjn0OHxcFkQg
        }
    }

    fun openInstagramPage(context: Context) {
        val isInstalled = isPackageInstalledAndEnabled("com.instagram.android",
                context.packageManager)

        if (isInstalled) {
            launchExternalApp(context, "http://instagram.com/_u/YOUR_PAGE_URL")
            //ex :http://instagram.com/_u/thaipeoplepowerparty
        } else {
            openTabBrowser(context, "https://www.instagram.com/YOUR_PAGE_URL/")
            //ex :https://www.instagram.com/thaipeoplepowerparty/
        }
    }

    fun openYoutubeChannel(context: Context) {
        val isInstalled = isPackageInstalledAndEnabled("com.google.android.youtube",
                context.packageManager)

        if (isInstalled) {
            launchExternalApp(context, "https://www.youtube.com/channel/YOUR_PAGE_URL")
            //ex :https://www.youtube.com/channel/UC-Mz1RS-96VrxZv_N7mYzEw
        } else {
            openTabBrowser(context, "https://www.youtube.com/channel/YOUR_PAGE_URL")
            //ex :https://www.youtube.com/channel/UC-Mz1RS-96VrxZv_N7mYzEw
        }
    }

    fun dialNumber(context: Context, phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phoneNumber")
        context.startActivity(intent)
    }
}

