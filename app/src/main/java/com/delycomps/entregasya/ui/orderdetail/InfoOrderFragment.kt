package com.delycomps.entregasya.ui.orderdetail

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.delycomps.entregasya.R
import com.delycomps.entregasya.adapters.AdapterGallery
import com.delycomps.entregasya.cache.SharedPrefsCache
import com.delycomps.entregasya.helpers.Helpers
import com.delycomps.entregasya.model.Order
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_info_order.*
import kotlinx.android.synthetic.main.fragment_info_order.view.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class InfoOrderFragment : Fragment() {
    private var position = 0
    private var newStatus = ""
    private var countImagesDelivery = 0
    private var countImagesPickup = 0
    private lateinit var orderCurrent: Order
    private lateinit var manageViewModel: ManageViewModel
    private val WRITE_EXTERNAL_STORAGE_PERMISSION = 100
    private val CODE_RESULT_CAMERA = 10001
    private lateinit var dialogLoading: AlertDialog
    private var currentPhotoPath: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        manageViewModel = ViewModelProviders.of(this).get(ManageViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_order, container, false)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val order: Order =  requireActivity().intent.getParcelableExtra("order")!!
        orderCurrent = order
        Log.d("imagesorder", "x" + order.imagesOrder + " countdleivery " + order.countImagesDelivery + " countImagesPickup "  + countImagesPickup)
        position =  requireActivity().intent.getIntExtra("POSITION", -1)

        val layoutManager = GridLayoutManager(requireContext(), 2)
        val rv = view.findViewById<RecyclerView>(R.id.recyclerView_gallery)
        val editStatus = view.findViewById<EditText>(R.id.order_status)
        val builderLoading: AlertDialog.Builder = AlertDialog.Builder(activity)
        builderLoading.setCancelable(false) // if you want user to wait for some process to finish,
        builderLoading.setView(R.layout.layout_loading_dialog)
        dialogLoading = builderLoading.create()

        rv.setHasFixedSize(true)
        rv.layoutManager = layoutManager

        rv.adapter = if(!order.imagesProduct.isNullOrEmpty()) AdapterGallery(
            order.imagesProduct!!.split(
                ","
            ).toList()
        ) else AdapterGallery(listOf())

        val buttonSave: Button = view.findViewById(R.id.button_status_order)
        if (order.status == "ASIGNADO") {
            buttonSave.text = "ASIGNAR PEDIDO RECOGIDO"
        } else {
            buttonSave.text = "ASIGNAR PEDIDO ENTREGADO"
        }

        verifyPermissionWriteStorage()

        buttonSave.setOnClickListener {
            val newStatusTmp = if (order.status == "ASIGNADO") "RECOGIDO" else "ENTREGADO"

            val dialogClickListener = DialogInterface.OnClickListener { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        manageViewModel.assignStatus(
                            SharedPrefsCache(requireContext()).getToken(),
                            newStatusTmp,
                            order.idOrder
                        )
                    }
                }
            }
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("¿Está seguro actualizar el pedido?")
                .setPositiveButton(Html.fromHtml("<b>ACEPTAR<b>"), dialogClickListener)
                .setNegativeButton(Html.fromHtml("<b>CANCELAR<b>"), dialogClickListener)
            val alert = builder.create()
            alert.show()
        }

        manageViewModel.image.observe(viewLifecycleOwner, {
            val newStatus = if (order.status == "ASIGNADO") "RECOGIDO" else "ENTREGADO"

            if (buttonSave.visibility == View.GONE) {
                buttonSave.visibility = View.VISIBLE
                buttonSave.setBackgroundResource(R.drawable.sty_button_primary)
            }

            if (newStatus == "RECOGIDO")
                countImagesDelivery++
            else
                countImagesDelivery++

        })

        manageViewModel.loading.observe(viewLifecycleOwner, {
            if (it)
                dialogLoading.show()
            else
                dialogLoading.hide()
        })

        manageViewModel.textError.observe(viewLifecycleOwner, {
            Snackbar.make(rv, it as CharSequence, Snackbar.LENGTH_LONG).setBackgroundTint(
                resources.getColor(
                    R.color.colorPrimary
                )
            ).show()
        })

        countImagesDelivery = order.countImagesDelivery
        countImagesPickup = order.countImagesPickup

        button_take_photo.setOnClickListener {
            dispatchTakePictureIntent()
        }

        manageViewModel.success.observe(viewLifecycleOwner, {
            dialogLoading.hide()
            if (it) {
                newStatus = if (order.status == "ASIGNADO") "RECOGIDO" else "ENTREGADO"
                order.status = newStatus
                orderCurrent.status = newStatus
                editStatus.setText(newStatus)

                if (order.status == "RECOGIDO") {
                    buttonSave.text = "ASIGNAR PEDIDO ENTREGADO"
                }

                if (newStatus == "ENTREGADO") {
                    button_take_photo.visibility = View.GONE
                    button_take_photo.setBackgroundResource(R.drawable.sty_button_disabled)
                }

                if (newStatus == "ENTREGADO" || (newStatus == "RECOGIDO" && order.countImagesPickup == 0) || (newStatus == "ENTREGADO" && order.countImagesDelivery == 0)) {
                    buttonSave.visibility = View.GONE
                    buttonSave.setBackgroundResource(R.drawable.sty_button_disabled)
                }
                Snackbar.make(rv, "El pedido fue actualizado correctamente", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(
                        resources.getColor(
                            R.color.colorPrimaryDark
                        )
                    ).show()
            }
        })

        view.order_pickup_address.setText(order.pickupAddress)
        view.findViewById<EditText>(R.id.product_name).setText(order.products)
        editStatus.setText(order.status)
        view.findViewById<EditText>(R.id.order_pickup_reference).setText(order.pickupReference)
        view.findViewById<EditText>(R.id.order_pickup_ubigeo).setText(order.pickupTextUbigeo)
        view.findViewById<EditText>(R.id.order_pickup_contact_name).setText(order.pickupContactName)
        view.findViewById<EditText>(R.id.order_pickup_contact_phone).setText(order.pickupPhone)
        view.findViewById<EditText>(R.id.order_delivery_address).setText(order.deliveryAddress)
        view.findViewById<EditText>(R.id.order_delivery_reference).setText(order.deliveryReference)
        view.findViewById<EditText>(R.id.order_delivery_ubigeo).setText(order.deliveryTextUbigeo)
        view.findViewById<EditText>(R.id.order_delivery_contact_name).setText(order.deliveryContactName)
        view.findViewById<EditText>(R.id.order_delivery_contact_phone).setText(order.deliveryPhone)

        if (order.status == "ENTREGADO") {
            button_take_photo.visibility = View.GONE
            button_take_photo.setBackgroundResource(R.drawable.sty_button_disabled)
        }

        if ((order.status == "ENTREGADO" || (order.status == "ASIGNADO" && order.countImagesPickup == 0) || (order.status == "RECOGIDO" && order.countImagesDelivery == 0))) {
            buttonSave.visibility = View.GONE
            buttonSave.setBackgroundResource(R.drawable.sty_button_disabled)
        }
    }

    override fun onPause() {
        super.onPause()
        SharedPrefsCache(requireContext()).set("new_status", newStatus, "string")
        SharedPrefsCache(requireContext()).set("position", position, "int")
        SharedPrefsCache(requireContext()).set("countImagesDelivery", countImagesDelivery, "int")
        SharedPrefsCache(requireContext()).set("countImagesPickup", countImagesPickup, "int")
    }

    private fun verifyPermissionWriteStorage() : Boolean { //si la API 23 a mas
        return Helpers.verifyPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE_PERMISSION)
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


    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(requireActivity().packageManager).also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
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
            CODE_RESULT_CAMERA -> if (resultCode == AppCompatActivity.RESULT_OK) {
                dialogLoading.show()
                val f = saveBitmapToFile(File(currentPhotoPath))
                val newStatus = if (orderCurrent.status == "ASIGNADO") "RECOGIDO" else "ENTREGADO"
                manageViewModel.uploadImage(f!!, newStatus, orderCurrent.idOrder, SharedPrefsCache(requireContext()).getToken())
            }
        }
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
                    Helpers.showToast(
                        requireContext(),
                        "Haz rechazado la petición, por favor considere en aceptarla desde ajustes."
                    )
        }
    }


}
