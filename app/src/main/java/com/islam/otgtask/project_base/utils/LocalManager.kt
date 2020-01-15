package com.islam.otgtask.project_base.utils

import android.content.Context
import android.content.res.Configuration
import java.util.*

object LocalManager {

    fun setLocale(c: Context) {
        setNewLocale(c, getLanguage(c))
    }

    fun setNewLocale(c: Context, language: String?) {
        persistLanguage(c, language)
        updateResources(c, language)
    }

    fun getLanguage(c: Context): String? {
        return PrefManager.getAppLanguage(c)
    }

    private fun persistLanguage(c: Context, language: String?) {
        language?.let { PrefManager.saveAppLanguage(c, language) }
    }

    fun updateResources(context: Context, language: String?): Context {
        var context = context
        val locale = Locale(language)
        Locale.setDefault(locale)

        val res = context.resources
        val config = Configuration(res.configuration)

        config.setLocale(locale)
        context = context.createConfigurationContext(config)

        return context
    }
}
