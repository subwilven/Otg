package com.islam.otgtask.ui.addContact

import androidx.lifecycle.viewModelScope
import com.islam.otgtask.data.Repository
import com.islam.otgtask.pojo.Contact
import com.islam.otgtask.project_base.POJO.Message
import com.islam.otgtask.project_base.base.other.BaseViewModel
import com.islam.otgtask.project_base.base.other.SingleLiveEvent
import com.islam.otgtask.project_base.utils.ValidationManager
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class AddContactViewModel : BaseViewModel() {

    val repository = Repository()
    var photoPath: String = ""
    val navigateToContactsList = SingleLiveEvent<Any>()

    fun addNewContact(viewId: Int, contact: Contact) {
        viewModelScope.launch {
            var message = ValidationManager.isValidEmail(contact.email)
            if (message == -1) message = ValidationManager.isValidName(contact.firstName)
            if (message == -1) message = ValidationManager.isValidName(contact.lastName)
            if (message == -1) message = ValidationManager.isValidPhone(contact.phoneCode + contact.phoneNumber)
            if (message == -1) message = ValidationManager.isValidLatitude(contact.latitude)
            if (message == -1) message = ValidationManager.isValidLongitude(contact.longitude)
            if (message != -1) {
                showToastMessage(Message(message))
                cancel()
            }
            networkCall(viewId) { repository.addNewContact(contact) }

            navigateToContactsList.value = null
        }
    }
}