package com.islam.otgtask.data

import com.islam.otgtask.MyApplication
import com.islam.otgtask.pojo.Contact
import com.islam.otgtask.pojo.CountryCode

import com.islam.otgtask.project_base.base.other.network.NetworkModel
import com.islam.otgtask.project_base.utils.PrefManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext


class Repository {

    suspend fun loadContacts(): MutableList<Contact> {
        return withContext(Dispatchers.IO) {
            delay(2000)
            PrefManager.loadContacts(MyApplication.instance!!.applicationContext)
        }
    }
    suspend fun deleteContacts(contact: Contact) {
        return withContext(Dispatchers.IO) {
            PrefManager.deleteContact(MyApplication.instance!!.applicationContext,contact)
        }
    }

    suspend fun fetchCountryCode(): List<CountryCode> {
        return withContext(Dispatchers.IO) {
            NetworkModel.clientApi!!.getCountryCodes()
        }
    }

    suspend fun addNewContact(contact: Contact) {
        withContext(Dispatchers.IO) {
            delay(2000)
            PrefManager.addContacts(MyApplication.instance!!.applicationContext,contact)
        }
    }

}

