package com.delycomps.entregasya

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.delycomps.entregasya.model.Order
import com.delycomps.entregasya.model.Tracking
import com.delycomps.entregasya.webservice.Repository
import org.json.JSONObject

class OrderPendingViewModel : ViewModel() {

    private val _listOrders: MutableLiveData<List<Order>> = MutableLiveData()
    val listOrders: LiveData<List<Order>> = _listOrders

    private val _textError: MutableLiveData<String> = MutableLiveData()
    val textError: LiveData<String> = _textError

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> = _loading

    private val _success: MutableLiveData<Boolean> = MutableLiveData()
    val success: LiveData<Boolean> = _success

    fun assignOrder(token: String, idOrder: Int) {
        _loading.value = true
        val data = JSONObject()
        data.put("id_order", idOrder)

        val oo = JSONObject()
        oo.put("method", "SP_ASIG_ORDER")
        oo.put("data", data)

        Repository().executeMain(oo, false, token) { isSuccess, _, message ->
            _loading.value = false

            if (isSuccess) {
                _success.value = isSuccess
            } else {
                _textError.value = message
            }

        }
    }

    fun getListOrder(token: String, type: String) {
        _loading.value = true
        Repository().getOrder(type, token, true) { isSuccess, result, message ->
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