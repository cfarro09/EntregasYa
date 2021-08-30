package com.delycomps.entregasya.ui.orderdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.delycomps.entregasya.model.DataImage
import com.delycomps.entregasya.model.Order
import com.delycomps.entregasya.model.ResMotive
import com.delycomps.entregasya.model.Tracking
import com.delycomps.entregasya.webservice.Repository
import org.json.JSONObject
import java.io.File

class ManageViewModel : ViewModel() {

    private val _listOrders: MutableLiveData<List<Order>> = MutableLiveData()
    val listOrders: LiveData<List<Order>> = _listOrders

    private val _listMotive: MutableLiveData<List<String>> = MutableLiveData()
    val listMotive: LiveData<List<String>> = _listMotive

    private val _textError: MutableLiveData<String> = MutableLiveData()
    val textError: LiveData<String> = _textError

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> = _loading

    private val _success: MutableLiveData<Boolean> = MutableLiveData()
    val success: LiveData<Boolean> = _success

    private val _image: MutableLiveData<String> = MutableLiveData()
    val image: LiveData<String> = _image



    private val _listImages: MutableLiveData<List<DataImage>> = MutableLiveData()
    val listImages: LiveData<List<DataImage>> = _listImages

    fun getListImage(idOrder: Int, token: String) {
        _loading.value = true
        Repository().getListImages(idOrder, token) { isSuccess, result, message ->
            _loading.value = false
            if (isSuccess) {
                _listImages.value = result!!
            } else {
                _textError.value = message
            }
        }
    }

    fun getListMotive(token: String) {
        Repository().getMotives("", token) { isSuccess, result, message ->
            if (isSuccess) {
                _listMotive.value = result!!.map{ "" + it.sub_status }
            } else {
                _textError.value = message
            }
        }
    }

    fun uploadImage(file: File, type: String, idOrder: Int, token: String) {
        _loading.value = true
        Repository().uploadImageDRiver(file, type, idOrder, token) { isSuccess, result, message ->
            _loading.value = false
            if (isSuccess) {
                _image.value = result!!
            } else {
//                _success.value = false
                _textError.value = message
            }
        }
    }

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