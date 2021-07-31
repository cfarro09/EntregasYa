package com.delycomps.entregasya

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.delycomps.entregasya.adapters.AdapterGallery
import com.delycomps.entregasya.cache.SharedPrefsCache
import com.delycomps.entregasya.helpers.Helpers
import com.delycomps.entregasya.helpers.Helpers.showToast
import com.delycomps.entregasya.model.ResLocation
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_new_order.*
import kotlinx.android.synthetic.main.dialog_ubigeo.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class NewOrderActivity : AppCompatActivity() {

    private var optionLocation = ""
    var initLocation = "department"
    var setLocation = ""
    private var departmentList: List<ResLocation> = listOf()
    private var provinceList: List<ResLocation> = listOf()
    var districtList: List<ResLocation> = listOf()
    private lateinit var dialogLoading: AlertDialog
    private val WRITE_EXTERNAL_STORAGE_PERMISSION = 100
    private val CODE_RESULT_CAMERA = 10001
    private var currentPhotoPath: String = ""
    private var pickup_ubigeo = ""
    private var delivery_ubigeo = ""
    private lateinit var rvGallery: RecyclerView
    private val imagesProduct = JSONArray()

//    private val listImage: MutableList<String> = ArrayList()

    private lateinit var newOrderViewModel: NewOrderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_order)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        newOrderViewModel = ViewModelProviders.of(this).get(NewOrderViewModel::class.java)

        newOrderViewModel.getListLocation("", "department", SharedPrefsCache(this).getToken())

        val builderLoading: AlertDialog.Builder = AlertDialog.Builder(this)
        builderLoading.setCancelable(false) // if you want user to wait for some process to finish,
        builderLoading.setView(R.layout.layout_loading_dialog)
        dialogLoading = builderLoading.create()

        val layoutManager = GridLayoutManager(this, 2)
        rvGallery = recyclerView_gallery
        rvGallery.setHasFixedSize(true)
        rvGallery.layoutManager = layoutManager

        val  builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogUbigeo = inflater.inflate(R.layout.dialog_ubigeo, null)
        builder.setView(dialogUbigeo)
        val dialog = builder.create()

        val spinnerDepartment = dialogUbigeo.spinner_department
        val spinnerProvince = dialogUbigeo.spinner_province
        val spinnerDistrict = dialogUbigeo.spinner_district

        spinnerDepartment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) { }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val valueSelected = spinnerDepartment.selectedItem.toString()
                if (valueSelected != "SELECCIONE") {
                    initLocation = "province"
                    newOrderViewModel.getListLocation(valueSelected, "department", SharedPrefsCache(applicationContext).getToken())
                }
            }
        }

        spinnerProvince.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) { }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val valueSelected = spinnerProvince.selectedItem.toString()
                if (valueSelected != "SELECCIONE") {
                    initLocation = "district"
                    newOrderViewModel.getListLocation(valueSelected, "province", SharedPrefsCache(applicationContext).getToken())

                }
            }
        }

        val ff = AdapterGallery(listOf())

        rvGallery.adapter = ff

        newOrderViewModel.image.observe(this, Observer {
            dialogLoading.hide()
            imagesProduct.put(it)
            ff.addItem(it)
        })

        newOrderViewModel.saveSuccess.observe(this, Observer {
            dialogLoading.hide()
            if (it) {
                val data = Intent()
                data.putExtra("neworder", true)
                setResult(RESULT_OK, data)
                finish()
            }
            else
                Snackbar.make(order_pickup_address, "Hubo un error" as CharSequence, Snackbar.LENGTH_LONG).setBackgroundTint(resources.getColor(R.color.colorPrimary)).show()
        })

        newOrderViewModel.locationList.observe(this, Observer{ locations ->
            val locationstmp: MutableList<String> = locations.map { it.value!! }.toMutableList()
            locationstmp.add(0, "SELECCIONE")

            when (initLocation) {
                "department" -> {
                    departmentList = locations
                    spinnerDepartment.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, locationstmp)
                    spinnerProvince.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOf())
                    spinnerDistrict.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOf())
                }
                "province" -> {
                    provinceList = locations
                    spinnerProvince.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, locationstmp)
                    spinnerDistrict.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOf())

                    if (setLocation.isNotEmpty()) {
                        spinnerProvince.setSelection(provinceList.indexOfFirst { it.value == setLocation.split(" - ")[1] } + 1)
                    }
                }
                else -> {
                    districtList = locations
                    spinnerDistrict.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, locationstmp)

                    if (setLocation.isNotEmpty()) {
                        spinnerDistrict.setSelection(districtList.indexOfFirst { it.value == setLocation.split(" - ")[2] } + 1)
                        setLocation = ""
                    }
                }
            }
        })

        dialogUbigeo.save_ubigeo.setOnClickListener {
            val department = spinnerDepartment.selectedItem?.toString()
            val province = spinnerProvince?.selectedItem?.toString()
            val district = spinnerDistrict?.selectedItem?.toString()

            if (!department.isNullOrEmpty() && !province.isNullOrEmpty() && !district.isNullOrEmpty() && district != "SELECCIONE") {
                dialog.hide()

                val rr: ResLocation? = districtList.find { it.value == district }

                if (optionLocation == "pickup") {
                    pickup_ubigeo = rr!!.ubigeo!!
                    order_pickup_ubigeo.setText("$department - $province - $district")
                }
                else {
                    delivery_ubigeo = rr!!.ubigeo!!
                    order_delivery_ubigeo.setText("$department - $province - $district")
                }
            }
        }

        order_pickup_ubigeo.setOnClickListener {
            optionLocation = "pickup"
            val location = order_pickup_ubigeo.text.toString()
            setLocation = location
            if (location.isNotEmpty()) {
                val locTmp = location.split(" - ")
                spinnerDepartment.setSelection(departmentList.indexOfFirst { it.value == locTmp[0] } + 1)
            } else {
                spinnerDepartment.setSelection(0)
                spinnerProvince.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOf())
                spinnerDistrict.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOf())
            }
            dialog.show()
        }

        order_delivery_ubigeo.setOnClickListener {
            optionLocation = "delivery"
            val location = order_delivery_ubigeo.text.toString()
            setLocation = location
            if (location.isNotEmpty()) {
                val locTmp = location.split(" - ")
                spinnerDepartment.setSelection(departmentList.indexOfFirst { it.value == locTmp[0] } + 1)
            } else {
                spinnerDepartment.setSelection(0)
                spinnerProvince.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOf())
                spinnerDistrict.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOf())
            }
            dialog.show()
        }

        verifyPermissionWriteStorage()

        button_upload_image.setOnClickListener {
            dispatchTakePictureIntent()
        }

        button_save_order.setOnClickListener {
            val orderPickupAddress = order_pickup_address.text.toString()
            val orderPickupReference = order_pickup_reference.text.toString()
            val orderPickupContactName = order_pickup_contact_name.text.toString()
            val orderPickupContactPhone = order_pickup_contact_phone.text.toString()
            val orderDeliveryAddress = order_delivery_address.text.toString()
            val orderDeliveryReference = order_delivery_reference.text.toString()
            val orderDeliveryContactName = order_delivery_contact_name.text.toString()
            val orderDeliveryContactPhone = order_delivery_contact_phone.text.toString()

            val orderDeliveryContactDocument = order_delivery_contact_document.text.toString()
            val orderPickupContactDocument = order_pickup_contact_document.text.toString()

            val productName = product_name.text.toString()
            val productQuantity = product_quantity.text.toString()

            if (orderPickupAddress != "" && orderPickupReference != "" && orderPickupContactName != "" && orderPickupContactPhone != "" && orderDeliveryAddress != "" &&
                orderDeliveryReference != "" && orderDeliveryContactName != "" && orderDeliveryContactPhone != "" && pickup_ubigeo != "" && delivery_ubigeo != "" && productName != "" &&
                productQuantity != "" && orderDeliveryContactDocument != "" && orderPickupContactDocument != "" ) {

                val headerObject = JSONObject()
                headerObject.put("order_pickup_address", orderPickupAddress)
                headerObject.put("order_pickup_reference", orderPickupReference)
                headerObject.put("order_pickup_ubigeo", pickup_ubigeo)
                headerObject.put("order_pickup_contact_name", orderPickupContactName)
                headerObject.put("order_pickup_contact_phone", orderPickupContactPhone)
                headerObject.put("order_delivery_address", orderDeliveryAddress)
                headerObject.put("order_delivery_reference", orderDeliveryReference)
                headerObject.put("order_delivery_ubigeo", delivery_ubigeo)
                headerObject.put("order_delivery_contact_name", orderDeliveryContactName)
                headerObject.put("order_delivery_contact_phone", orderDeliveryContactPhone)

                headerObject.put("order_delivery_contact_document", orderDeliveryContactDocument)
                headerObject.put("order_pickup_contact_document", orderPickupContactDocument)

                val headerData = JSONObject()
                headerData.put("data", headerObject)

                val productData = JSONObject()
                productData.put("product_name", productName)
                productData.put("product_quantity", productQuantity)
                productData.put("product_images", imagesProduct)

                val productsArray = JSONArray()
                productsArray.put(productData)

                val detailData = JSONObject()
                detailData.put("data", productsArray)

                val oo = JSONObject()
                oo.put("method", "SP_INS_ORDER")
                oo.put("header", headerData)
                oo.put("details", detailData)

                dialogLoading.show()
                newOrderViewModel.executeMain(oo, SharedPrefsCache(applicationContext).getToken())
            } else {
                Snackbar.make(order_pickup_address, "Debe completar todos los campos" as CharSequence, Snackbar.LENGTH_LONG).setBackgroundTint(resources.getColor(R.color.colorPrimary)).show()
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }
    private fun saveBitmapToFile(file: File): File? {
        return try { // BitmapFactory options to downsize the image
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            o.inSampleSize = 6
            // factor of downsizing the image
            var inputStream = FileInputStream(file)
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o)
            inputStream.close()
            // The new size we want to scale to
            val REQUIRED_SIZE = 75
            // Find the correct scale value. It should be the power of 2.
            var scale = 1
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                o.outHeight / scale / 2 >= REQUIRED_SIZE
            ) {
                scale *= 2
            }
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            inputStream = FileInputStream(file)
            val selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2)
            inputStream.close()
            // here i override the original image file
            file.createNewFile()
            val outputStream = FileOutputStream(file)
            selectedBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            file
        } catch (e: Exception) {
            null
        }
    }
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager).also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.delycomps.entregasyasharing.provider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, CODE_RESULT_CAMERA)
                }
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        imageReturnedIntent: Intent?
    ){
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent)
        when (requestCode) {
            CODE_RESULT_CAMERA -> if (resultCode == RESULT_OK) {
                dialogLoading.show()
                val f = saveBitmapToFile(File(currentPhotoPath))
                newOrderViewModel.uploadImage(f!!, SharedPrefsCache(this).getToken())
            }
        }
    }

    private fun verifyPermissionWriteStorage() : Boolean { //si la API 23 a mas
        return Helpers.verifyPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE_PERMISSION)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            WRITE_EXTERNAL_STORAGE_PERMISSION ->
                if (grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    showToast(this, "Haz rechazado la petición, por favor considere en aceptarla desde ajustes.")
        }
    }


}
