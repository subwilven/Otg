package com.islam.otgtask.ui.contacts

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.islam.otgtask.R
import com.islam.otgtask.project_base.base.fragments.BaseSuperFragment
import com.islam.otgtask.project_base.utils.IntentManager
import com.islam.otgtask.ui.ContactDetails.ContactDetailsActiviy

class ContactsFragment : BaseSuperFragment<ContactsViewModel>() {


    lateinit var mAdapter: ContactsAdapter
    override var fragmentTag = "ContactsFragment"

    override fun onLaunch() {
        initContentView(R.layout.fragment_fragment2)
        initToolbar(R.string.contacts, menuId = R.menu.menu_contacts)
        initViewModel(this, ContactsViewModel::class.java)
    }

    override fun onViewCreated(view: View, viewModel: ContactsViewModel, instance: Bundle?) {
        mAdapter = ContactsAdapter(viewModel)
        createRecyclerView(mAdapter,layoutManager = GridLayoutManager(context,2))
    }

    override fun setUpObservers() {
        mViewModel.contactSortedList.observe(viewLifecycleOwner, Observer {
            mAdapter.setData(it)
        })
        mViewModel.dialNumber.observes(viewLifecycleOwner, Observer {
            IntentManager.dialNumber(context!!, it)
        })
        mViewModel.navigatetoContactDetails.observes(viewLifecycleOwner, Observer {
            val bundle = Bundle()
            bundle.putParcelable("contact", it)
            navigate(ContactDetailsActiviy::class.java, bundle)
        })
        mViewModel.deleteContact.observes(viewLifecycleOwner, Observer {
            mAdapter.removeItem(it)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        showDialogList(R.string.sort_by, listOf("First Name", "Last Name")) { _, index, _ ->
            mViewModel.sortBy.value = index
        }
        return true
    }
}