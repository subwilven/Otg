package com.islam.otgtask.project_base.utils

import android.content.Context
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.islam.otgtask.pojo.Contact
import com.google.gson.reflect.TypeToken
import java.lang.Exception


object PrefManager {

    private const val PREF_LANGUAGE = "language"
    private const val PREF_FIRST_LAUNCH = "first_launch"

    fun loadContacts(context: Context): MutableList<Contact> {
        val contacts = PreferenceManager.getDefaultSharedPreferences(context).getString("contacts", "")
        try {
            val type = object : TypeToken<List<Contact>>() {}.type
            return if (contacts!!.isNotBlank()) Gson().fromJson<List<Contact>>(contacts, type).toMutableList()
            else mutableListOf()
        } catch (e: Exception) {
            val type = object : TypeToken<Contact>() {}.type
            return if (contacts!!.isNotBlank()) mutableListOf(Gson().fromJson<Contact>(contacts, type))
            else mutableListOf()
        }
    }

    fun addContacts(context: Context, contact: Contact) {
        val contactList = loadContacts(context)
        val index = contactList.indexOfFirst { it.id == contact.id }
        if (index != -1) contactList.removeAt(index)
        contactList.add(contact)
        val contactListStr = Gson().toJson(contactList)
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("contacts", contactListStr).apply()
    }

    fun deleteContact(context: Context, contact: Contact) {
        val contactList = loadContacts(context)
        contactList.remove(contact)
        val contactListStr = Gson().toJson(contactList)
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("contacts", contactListStr).apply()
    }

    fun saveAppLanguage(context: Context, lang: String) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(PREF_LANGUAGE, lang).apply()
    }

    fun getAppLanguage(context: Context): String? {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_LANGUAGE, "en")
    }
}
