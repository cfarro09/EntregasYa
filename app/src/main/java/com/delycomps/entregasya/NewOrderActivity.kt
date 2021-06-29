package com.delycomps.entregasya

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
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
import com.delycomps.entregasya.cache.SharedPrefsCache
import com.delycomps.entregasya.helpers.Helpers
import com.delycomps.entregasya.helpers.Helpers.showToast
import com.delycomps.entregasya.model.ResLocation
import kotlinx.android.synthetic.main.activity_new_order.*
import kotlinx.android.synthetic.main.dialog_ubigeo.view.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class NewOrderActivity : AppCompatActivity() {

    var optionLocation = ""
    var initLocation = "department"
    var setLocation = ""
    lateinit var departmentList: List<ResLocation>
    lateinit var provinceList: List<ResLocation>
    lateinit var districtList: List<ResLocation>

    private val WRITE_EXTERNAL_STORAGE_PERMISSION = 100
    private val CODE_RESULT_CAMERA = 10001
    private var currentPhotoPath: String = ""

    private lateinit var newOrderViewModel: NewOrderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_order)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        newOrderViewModel = ViewModelProviders.of(this).get(NewOrderViewModel::class.java)

        newOrderViewModel.getListLocation("", "department", SharedPrefsCache(this).getToken())

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
            val department = spinnerDepartment.selectedItem.toString()
            val province = spinnerProvince.selectedItem.toString()
            val district = spinnerDistrict.selectedItem.toString()
            dialog.hide()
            if (optionLocation == "pickup")
                order_pickup_ubigeo.setText("$department - $province - $district")
            else
                order_delivery_ubigeo.setText("$department - $province - $district")
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

        verifyPermissionWriteStorage();
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
                        "com.example.android.fileprovider",
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
//                progress = ProgressDialog.show(this, "Loading", "Espere, por favor.")
                val f = saveBitmapToFile(File(currentPhotoPath))
//                orderDetailViewModel.uploadPhoto(f!!, getToken(context!!), "IMAGEN_PD", "dsandsjandsa")
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
