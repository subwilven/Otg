package com.islam.otgtask.ui.country_code

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.islam.otgtask.data.Repository
import com.islam.otgtask.pojo.CountryCode
import com.islam.otgtask.project_base.base.other.BaseViewModel
import com.islam.otgtask.project_base.base.other.SingleLiveEvent
import kotlinx.coroutines.launch

class CountryCodeViewModel : BaseViewModel() {
    val repository = Repository()
    var countryCodeList: List<CountryCode>? = listOf()
    val queryFilter = MutableLiveData<String>()
    val pickedCountryCode = SingleLiveEvent<CountryCode>()
    var filterdCountryCodeList: LiveData<List<CountryCode>>? = null

    override fun loadInitialData() {
        loadCountryCode(null)
    }

    private fun loadCountryCode(viewId: Int?) {
        filterdCountryCodeList = Transformations.map(queryFilter) { query ->
            if (query.isBlank()) {
                return@map countryCodeList
            }
            countryCodeList?.filter {
                it.nativeName.contains(query, ignoreCase = true)
                        || isContainCode(query,it.callingCodes)
                        || it.capital.contains(query, ignoreCase = true)
            }

        }
        viewModelScope.launch {
            countryCodeList = networkCall(viewId) { repository.fetchCountryCode() }.value
            queryFilter.value=""
        }
    }

    private fun isContainCode(codeQuery :String,codes:List<String>):Boolean{
        for(code in codes ){
            if(code.contains(codeQuery))
                return true
        }
        return false
    }
}