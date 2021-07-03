package com.delycomps.entregasya

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.delycomps.entregasya.model.Tracking
import com.delycomps.entregasya.webservice.Repository

class DetailOrderViewModel : ViewModel() {

    private val _trackingList: MutableLiveData<List<Tracking>> = MutableLiveData()
    val trackingList: LiveData<List<Tracking>> = _trackingList

    fun getTracking(idOrder: Int, token: String) {
        Repository().getTracking(idOrder, token) { isSuccess, result, _ ->
            if (isSuccess) {
                _trackingList.value = result!!
            } else {
                _trackingList.value = listOf()
            }
        }
    }
}