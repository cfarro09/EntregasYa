package com.delycomps.entregasya

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.delycomps.entregasya.model.ResLocation
import com.delycomps.entregasya.webservice.Repository
import org.json.JSONObject
import java.io.File

class NewOrderViewModel : ViewModel() {

    private val _locationList: MutableLiveData<List<ResLocation>> = MutableLiveData()
    val locationList: LiveData<List<ResLocation>> = _locationList

    private val _image: MutableLiveData<String> = MutableLiveData()
    val image: LiveData<String> = _image

    private val _saveSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val saveSuccess: LiveData<Boolean> = _saveSuccess

    fun getListLocation(search: String, filter: String, token: String) {
        Repository().getUbigeo(search, filter, token) { isSuccess, result, _ ->
            if (isSuccess) {
                _locationList.value = result!!
            }
        }
    }
    fun uploadImage(file: File, token: String) {
        Repository().uploadImage(file, token) { isSuccess, result, _ ->
            if (isSuccess) {
                _image.value = result!!
            }
        }
    }
    fun executeMain(jo: JSONObject, token: String) {
        Repository().executeMain(jo, true, token ) { isSuccess, _, _ ->
            _saveSuccess.value = isSuccess
        }
    }
}