package com.islam.otgtask.ui.country_code

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.islam.otgtask.R
import com.islam.otgtask.project_base.base.activities.BaseActivityFragment
import com.islam.otgtask.project_base.base.fragments.BaseFragment
import com.islam.otgtask.project_base.base.fragments.BaseSuperFragment
import com.islam.otgtask.project_base.utils.DefaultTextWatcher
import com.islam.otgtask.ui.contacts.ContactsViewModel
import kotlinx.android.synthetic.main.fragment_country_code.*

class CountryCodeActivity : BaseActivityFragment() {
    override fun getStartFragment(): BaseFragment<*> {
        return CountryCodeFragment()
    }
}

class CountryCodeFragment : BaseSuperFragment<CountryCodeViewModel>() {

    override var fragmentTag = "CountryCodeFragment"
    lateinit var mAdapter: CountryCodeAdapter

    override fun onLaunch() {
        initContentView(R.layout.fragment_country_code)
        initToolbar(R.string.country_code)
        initViewModel(this, CountryCodeViewModel::class.java)
    }

    override fun onViewCreated(view: View, viewModel: CountryCodeViewModel, instance: Bundle?) {

        mAdapter = CountryCodeAdapter(viewModel)

        createRecyclerView(mAdapter)

        etSearch.addTextChangedListener(DefaultTextWatcher {
            mViewModel.queryFilter.value = it
        })
    }

    override fun setUpObservers() {
        mViewModel.filterdCountryCodeList?.observe(viewLifecycleOwner, Observer {
            mAdapter.setData(it.toMutableList())
        })
        mViewModel.pickedCountryCode.observes(viewLifecycleOwner, Observer {
            val data = Intent()
            data.putExtra("code", it.callingCodes.first())
            activity?.setResult(Activity.RESULT_OK, data)
            finish()
        })
    }

}