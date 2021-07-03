package com.delycomps.entregasya.ui.orderdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.delycomps.entregasya.model.Order
import com.delycomps.entregasya.model.Tracking
import com.delycomps.entregasya.webservice.Repository
import org.json.JSONObject

class ManageViewModel : ViewModel() {

    private val _listOrders: MutableLiveData<List<Order>> = MutableLiveData()
    val listOrders: LiveData<List<Order>> = _listOrders

    private val _textError: MutableLiveData<String> = MutableLiveData()
    val textError: LiveData<String> = _textError

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> = _loading

    private val _success: MutableLiveData<Boolean> = MutableLiveData()
    val success: LiveData<Boolean> = _success

    fun assignStatus(token: String, status: String, idOrder: Int) {
        _loading.value = true
        val data = JSONObject()
        data.put("id_order", idOrder)
        data.put("status", status)

        val oo = JSONObject()
        oo.put("method", "SP_CHANGE_ORDER_STATUS")
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

}