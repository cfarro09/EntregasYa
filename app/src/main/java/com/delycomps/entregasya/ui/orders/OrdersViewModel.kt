package com.delycomps.entregasya.ui.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.delycomps.entregasya.model.Order
import com.delycomps.entregasya.webservice.Repository

class OrdersViewModel : ViewModel() {

    private val _listOrders: MutableLiveData<List<Order>> = MutableLiveData()
    val listOrders: LiveData<List<Order>> = _listOrders

    private val _textError: MutableLiveData<String> = MutableLiveData()
    val textError: LiveData<String> = _textError

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> = _loading

    fun getListOrder(token: String, type: String, status: String = "") {
        _loading.value = true
        Repository().getOrder(type, token, false, status) { isSuccess, result, message ->
            _loading.value = false
            if (isSuccess) {
                if (result != null) {
                    _listOrders.value = result
                }
            } else {
                _listOrders.value = listOf()
                _textError.value = message
            }
        }
    }

}