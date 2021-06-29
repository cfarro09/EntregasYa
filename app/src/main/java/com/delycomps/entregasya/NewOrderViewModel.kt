package com.delycomps.entregasya

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.delycomps.entregasya.AdapterOrder
import com.delycomps.entregasya.R
import com.delycomps.entregasya.cache.SharedPrefsCache
import com.delycomps.entregasya.model.Order
import com.delycomps.entregasya.model.ResLocation
import com.delycomps.entregasya.webservice.Repository
import com.google.android.material.snackbar.Snackbar

class NewOrderViewModel : ViewModel() {

    private val _locationList: MutableLiveData<List<ResLocation>> = MutableLiveData()
    val locationList: LiveData<List<ResLocation>> = _locationList

    fun getListLocation(search: String, filter: String, token: String) {
        Repository().getUbigeo(search, filter, token) { isSuccess, result, _ ->
            if (isSuccess) {
                _locationList.value = result!!
            }
        }
    }
}