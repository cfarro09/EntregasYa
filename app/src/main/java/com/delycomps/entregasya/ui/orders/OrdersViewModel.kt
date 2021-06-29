package com.delycomps.entregasya.ui.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.delycomps.entregasya.AdapterOrder
import com.delycomps.entregasya.R
import com.delycomps.entregasya.cache.SharedPrefsCache
import com.delycomps.entregasya.model.Order
import com.delycomps.entregasya.webservice.Repository
import com.google.android.material.snackbar.Snackbar

class OrdersViewModel : ViewModel() {

    private val _listOrders: MutableLiveData<List<Order>> = MutableLiveData()
    val listOrders: LiveData<List<Order>> = _listOrders

    private val _textError: MutableLiveData<String> = MutableLiveData()
    val textError: LiveData<String> = _textError

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> = _loading

    fun getListOrder(token: String) {
        _loading.value = true
        Repository().getOrder(token) { isSuccess, result, message ->
            _loading.value = false
            if (isSuccess) {
                _listOrders.value = result!!
            } else {
                val oo = Order(1, "23232", "Cementro 40kg", "Carlos Farro",
                    "DNI","73147683", "943856850", "Jr enrique barron 1323",
                    "Por mi casa", "David Diaz", "DNI", "73147682", "943856850",
                    "03", "David Diaz", "DNI", "73147683", "943856850",
                    "03", "David Diaz", "DNI", "73147683", "DNI", "3232",
                    "943856850", "03", "NINGUNO", "01/01/1994")
                _listOrders.value = listOf(oo, oo, oo)
                _textError.value = message
            }
        }
    }

}