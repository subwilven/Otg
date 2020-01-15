package com.islam.otgtask.ui.contacts

import android.view.View
import android.view.ViewGroup
import com.islam.otgtask.R
import com.islam.otgtask.pojo.Contact
import com.islam.otgtask.project_base.base.adapters.BaseAdapter
import com.islam.otgtask.project_base.base.adapters.BaseViewHolder
import com.islam.otgtask.project_base.utils.IntentManager
import com.islam.otgtask.project_base.utils.ImageHandler.loadImage
import kotlinx.android.synthetic.main.item_contact.*
import java.io.File

class ContactsAdapter(val viewModel: ContactsViewModel) : BaseAdapter<Contact, ContactsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent, R.layout.item_contact)
    }

    inner class ViewHolder(viewGroup: ViewGroup, layoutId: Int) : BaseViewHolder<Contact>(viewGroup, layoutId), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
            ivMakeCall.setOnClickListener(this)
            ivDelete.setOnClickListener(this)
        }

        override fun onBind(item: Contact) {
            tvFirstName.text = item.firstName
            tvLastName.text = item.lastName
            ivContantPhoto.loadImage(File(item.photoPath))
        }

        override fun onClick(p0: View?) {
            when (p0?.id) {
                R.id.ivMakeCall -> {
                    val phone = getItem(adapterPosition)?.phoneCode + getItem(adapterPosition)?.phoneNumber
                    viewModel.dialNumber.value = phone
                }
                R.id.ivDelete -> viewModel.deleteContact(adapterPosition)
                else -> viewModel.navigatetoContactDetails.value = getItem(adapterPosition)
            }
        }

    }

}