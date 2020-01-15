package com.islam.otgtask.ui.country_code

import android.view.View
import android.view.ViewGroup
import com.islam.otgtask.R
import com.islam.otgtask.pojo.CountryCode
import com.islam.otgtask.project_base.base.adapters.BaseAdapter
import com.islam.otgtask.project_base.base.adapters.BaseViewHolder
import kotlinx.android.synthetic.main.item_country_code.*

class CountryCodeAdapter(val mViewModel: CountryCodeViewModel) : BaseAdapter<CountryCode, CountryCodeAdapter.ViewModel>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewModel {
        return ViewModel(parent, R.layout.item_country_code)
    }

    inner class ViewModel(viewGroup: ViewGroup, layoutId: Int) : BaseViewHolder<CountryCode>(viewGroup, layoutId), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onBind(item: CountryCode) {
            tvCountryCode.text = item.callingCodes.first()
            tvCountryName.text = item.nativeName
        }

        override fun onClick(p0: View?) {
            mViewModel.pickedCountryCode.value = getItem(adapterPosition)
        }

    }
}