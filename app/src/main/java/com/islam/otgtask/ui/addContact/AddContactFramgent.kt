package com.islam.otgtask.ui.addContact

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.islam.otgtask.R
import com.islam.otgtask.pojo.Contact
import com.islam.otgtask.project_base.base.fragments.BaseFragment
import com.islam.otgtask.project_base.utils.DefaultTextWatcher
import com.islam.otgtask.project_base.utils.ImageHandler.loadImage
import com.islam.otgtask.ui.country_code.CountryCodeActivity
import kotlinx.android.synthetic.main.fragment_add_contact.*
import java.io.File
import kotlin.random.Random

class AddContactFramgent : BaseFragment<AddContactViewModel>() {

    override var fragmentTag = "AddContactFramgent"


    companion object {
        fun newInstance(contact: Contact): AddContactFramgent {
            val bundle = Bundle()
            bundle.putParcelable("contact", contact)
            val fragment = AddContactFramgent()
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun onLaunch() {
        initContentView(R.layout.fragment_add_contact)
        initToolbar(R.string.new_contact)
        initViewModel(this, AddContactViewModel::class.java)
    }

    override fun onViewCreated(view: View, viewModel: AddContactViewModel, instance: Bundle?) {
        markScreenAsCompleted()
        initUi()
    }

    private fun initUi() {
        btnSave.isEnabled = false
        val contact = arguments?.getParcelable<Contact>("contact")
        contact?.run {
            etFirstName.setText(firstName)
            etName.setText(lastName)
            etPhoneNumber.setText(phoneNumber)
            etAddress.setText(address)
            etEmail.setText(email)
            etLatitude.setText(latitude.toString())
            etLongitude.setText(longitude.toString())
            etCodePhone.setText(phoneCode)
            ivPhoto.loadImage(File(photoPath))
            btnSave.isEnabled = true
        }

        ivPhoto.setOnClickListener {
            pickImage {
                ivPhoto.loadImage(it)
                mViewModel.photoPath = it!!.absolutePath
            }
        }
        etCodePhone.setOnClickListener { startActivityForResult(Intent(context, CountryCodeActivity::class.java), 102) }

        val textWatcher = DefaultTextWatcher { btnSave.isEnabled = allDataIsExist() }

        btnSave.setOnClickListener {
            var newContact: Contact? = null
            if (contact == null) {
                newContact = Contact()
                newContact.id = Random(10000).nextInt()
            } else
                newContact = contact

            newContact.photoPath = mViewModel.photoPath
            newContact.firstName = etFirstName.text.toString()
            newContact.lastName = etName.text.toString()
            newContact.phoneNumber = etPhoneNumber.text.toString()
            newContact.email = etEmail.text.toString()
            newContact.address = etAddress.text.toString()
            newContact.phoneCode = etCodePhone.text.toString()
            newContact.latitude = etLatitude.text.toString().toDouble()
            newContact.longitude = etLongitude.text.toString().toDouble()

            mViewModel.addNewContact(btnSave.id, newContact)
        }

        etFirstName.addTextChangedListener(textWatcher)
        etName.addTextChangedListener(textWatcher)
        etPhoneNumber.addTextChangedListener(textWatcher)
        etEmail.addTextChangedListener(textWatcher)
        etAddress.addTextChangedListener(textWatcher)
        etLatitude.addTextChangedListener(textWatcher)
        etCodePhone.addTextChangedListener(textWatcher)
        etLongitude.addTextChangedListener(textWatcher)

    }

    private fun allDataIsExist(): Boolean {
        return etFirstName.text.toString().isNotEmpty() &&
                etName.text.toString().isNotEmpty() &&
                etPhoneNumber.text.toString().isNotEmpty() &&
                etEmail.text.toString().isNotEmpty() &&
                etAddress.text.toString().isNotEmpty() &&
                etLatitude.text.toString().isNotEmpty() &&
                etCodePhone.text.toString().isNotEmpty() &&
                etLongitude.text.toString().isNotEmpty()
    }

    override fun setUpObservers() {
        mViewModel.navigateToContactsList.observes(viewLifecycleOwner, Observer {
            activity?.onBackPressed()
            //  kotlin.runCatching {  (activity as BaseNavigationActivity).navigateToItem(0)}
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            etCodePhone.setText(data?.getStringExtra("code"))
        } else
            super.onActivityResult(requestCode, resultCode, data)
    }
}