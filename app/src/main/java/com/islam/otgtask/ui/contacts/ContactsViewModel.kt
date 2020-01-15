package com.islam.otgtask.ui.contacts

import androidx.arch.core.util.Function
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.islam.otgtask.data.Repository
import com.islam.otgtask.pojo.Contact
import com.islam.otgtask.project_base.base.other.BaseViewModel
import com.islam.otgtask.project_base.base.other.SingleLiveEvent
import kotlinx.coroutines.launch

class ContactsViewModel : BaseViewModel() {

    var contactSortedList: LiveData<MutableList<Contact>> = MutableLiveData()
    private var contactList: MutableList<Contact>? = null
    var sortBy: MutableLiveData<Int> = MutableLiveData()
    val repository = Repository()
    val dialNumber: SingleLiveEvent<String> = SingleLiveEvent()
    val deleteContact: SingleLiveEvent<Int> = SingleLiveEvent()
    val navigatetoContactDetails: SingleLiveEvent<Contact> = SingleLiveEvent()

    override fun loadInitialData() {
        loadContacts(null)
    }

    private fun loadContacts(viewId: Int?) {

        contactSortedList = Transformations.map(sortBy) { index ->
            if (contactList != null) {
                contactList?.sortedBy {
                    if (index == 0) it.firstName
                    else it.lastName
                }?.toMutableList()
            } else {
                mutableListOf()
            }
        }

        viewModelScope.launch {
            contactList = networkCall(viewId) { repository.loadContacts() }.value!!
            sortBy.value = 0
        }
    }

    fun deleteContact(index:Int){
        val contact = contactSortedList.value?.get(index)
        contactList?.remove(contact)
        deleteContact.value =index
        viewModelScope.launch {
            repository.deleteContacts(contact!!)
        }

    }
}