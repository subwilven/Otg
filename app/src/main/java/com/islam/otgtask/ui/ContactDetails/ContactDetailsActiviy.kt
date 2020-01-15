package com.islam.otgtask.ui.ContactDetails

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import com.islam.otgtask.R
import com.islam.otgtask.pojo.Contact
import com.islam.otgtask.project_base.base.activities.BaseActivityFragment
import com.islam.otgtask.project_base.base.fragments.BaseFragment
import com.islam.otgtask.project_base.utils.ImageHandler.loadImage
import com.islam.otgtask.project_base.utils.IntentManager
import com.islam.otgtask.ui.addContact.AddContactFramgent
import kotlinx.android.synthetic.main.fragment_contact_details.*
import java.io.File

class ContactDetailsActiviy : BaseActivityFragment() {
    override val layoutId = R.layout.activity_empty
    override fun getStartFragment(): BaseFragment<*> {
        val contact = intent.getParcelableExtra<Contact>("contact")
        return ContactDetailsFragment.newInstance(contact)
    }

}

class ContactDetailsFragment : BaseFragment<ContactDetailsViewModel>() {
    override var fragmentTag = "ContactDetailsFragment"

    companion object {
        fun newInstance(contact: Contact): ContactDetailsFragment {
            val bundle = Bundle()
            bundle.putParcelable("contact", contact)
            val fragment = ContactDetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onLaunch() {
        initContentView(R.layout.fragment_contact_details)
        initToolbar(0, menuId = R.menu.menu_contacts_details)
        initViewModel(this, ContactDetailsViewModel::class.java)
    }

    override fun onViewCreated(view: View, viewModel: ContactDetailsViewModel, instance: Bundle?) {
        val contact = arguments?.getParcelable<Contact>("contact")

        viewModel.contact.value = contact

        ivCall.setOnClickListener {
            IntentManager.dialNumber(context!!, contact!!.phoneCode + contact.phoneNumber)
        }
        ivMap.setOnClickListener {
            IntentManager.openGoogleMap(context!!, contact!!.latitude, contact.longitude)
        }
        ivOpenEmail.setOnClickListener {
            IntentManager.openEmail(context!!, contact!!.email)
        }

    }

    override fun setUpObservers() {
        mViewModel.contact.observe(viewLifecycleOwner, Observer {
            it?.run {
                etName.text = "$firstName $lastName"
                etPhoneNumber.text = phoneCode+phoneNumber
                etAddress.text = address
                etEmail.text = email
                etLatitude.text = latitude.toString()
                etLongitude.text = longitude.toString()
                ivPhoto.loadImage(File(photoPath))
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        navigate(AddContactFramgent.newInstance(mViewModel.contact.value!!),addToBackStack = true)
        return super.onOptionsItemSelected(item)
    }
}