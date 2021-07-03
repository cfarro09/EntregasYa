package com.delycomps.entregasya.ui.orderdetail

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.delycomps.entregasya.R
import com.delycomps.entregasya.adapters.AdapterGallery
import com.delycomps.entregasya.cache.SharedPrefsCache
import com.delycomps.entregasya.model.Order
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_info_order.view.*


class InfoOrderFragment : Fragment() {
    private var position = 0
    private var newStatus = ""
    private lateinit var manageViewModel: ManageViewModel

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
        position =  requireActivity().intent.getIntExtra("POSITION", -1)

        val layoutManager = GridLayoutManager(requireContext(), 2)
        val rv = view.findViewById<RecyclerView>(R.id.recyclerView_gallery)
        val editStatus = view.findViewById<EditText>(R.id.order_status)
        val builderLoading: AlertDialog.Builder = AlertDialog.Builder(activity)
        builderLoading.setCancelable(false) // if you want user to wait for some process to finish,
        builderLoading.setView(R.layout.layout_loading_dialog)
        val dialogLoading: AlertDialog = builderLoading.create()

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

        buttonSave.setOnClickListener {
            newStatus = if (editStatus.text.toString() == "ASIGNADO") "RECOGIDO" else "ENTREGADO"
            val dialogClickListener = DialogInterface.OnClickListener { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        manageViewModel.assignStatus(
                            SharedPrefsCache(requireContext()).getToken(),
                            newStatus,
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

        manageViewModel.success.observe(viewLifecycleOwner, {
            editStatus.setText(if (editStatus.text.toString() == "ASIGNADO") "RECOGIDO" else "ENTREGADO")
            if (editStatus.text.toString() == "ENTREGADO") {
                buttonSave.isEnabled = false
                buttonSave.setBackgroundResource(R.drawable.sty_button_disabled)
            }
            Snackbar.make(rv, "El pedido fue actualizado correctamente", Snackbar.LENGTH_LONG)
                .setBackgroundTint(
                    resources.getColor(
                        R.color.colorPrimaryDark
                    )
                ).show()
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
            buttonSave.isEnabled = false
            buttonSave.setBackgroundResource(R.drawable.sty_button_disabled)
        }
    }

    override fun onPause() {
        super.onPause()
        SharedPrefsCache(requireContext()).set("new_status", newStatus, "string")
        SharedPrefsCache(requireContext()).set("position", position, "int")
    }

}
