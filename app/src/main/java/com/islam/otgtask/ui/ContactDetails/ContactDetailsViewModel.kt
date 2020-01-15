package com.islam.otgtask.ui.ContactDetails

import androidx.lifecycle.MutableLiveData
import com.islam.otgtask.pojo.Contact
import com.islam.otgtask.project_base.base.other.BaseViewModel

class ContactDetailsViewModel : BaseViewModel() {
    val contact = MutableLiveData<Contact>()
}