package com.delycomps.entregasya.ui.orders

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.delycomps.entregasya.*
import com.delycomps.entregasya.Constants.CODE_LISTORRDER_TO_DETAIALORDER
import com.delycomps.entregasya.adapters.AdapterOrder
import com.delycomps.entregasya.cache.SharedPrefsCache
import com.delycomps.entregasya.model.Order
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_order.*

class OrdersFragment : Fragment() {

    private lateinit var orderViewModel: OrdersViewModel
    private lateinit var adapterOrder: AdapterOrder
    private lateinit var rv: RecyclerView
    private var typeUser = ""
    private val code = 2000
    private val codeManage = 2001
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        orderViewModel = ViewModelProviders.of(this).get(OrdersViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_order, container, false)

        typeUser = SharedPrefsCache(requireContext()).get("type", "string") as String
        val newStatus: String = if (typeUser === "CLIENT") "" else "ACTUAL"
        context?.let { orderViewModel.getListOrder(SharedPrefsCache(it).getToken(), typeUser, newStatus)}

        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == code) {
                val status = data!!.getBooleanExtra("neworder", false)
                if (status) {
                    val text = if (typeUser == "CLIENT") "El pedido fue asignado correctamente." else "Nuevo pedido registrado correctamente."
                    Snackbar.make(rv, text, Snackbar.LENGTH_LONG).show()
                    context?.let { orderViewModel.getListOrder(SharedPrefsCache(it).getToken(), typeUser, if (typeUser === "CLIENT") "ACTUAL" else "")}
                }
            }
        } else {
            if (requestCode == codeManage) {
                val newStatus = SharedPrefsCache(requireContext()).get("new_status", "string") as String
                val position = SharedPrefsCache(requireContext()).get("position", "int") as Int
                val countImagesDelivery = SharedPrefsCache(requireContext()).get("countImagesDelivery", "int") as Int
                val countImagesPickup = SharedPrefsCache(requireContext()).get("countImagesPickup", "int") as Int

                if (newStatus.isNotEmpty() && position > -1) {
                    adapterOrder.changeStatus(newStatus, position, countImagesDelivery, countImagesPickup)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val builderLoading: AlertDialog.Builder = AlertDialog.Builder(activity)
        builderLoading.setCancelable(false) // if you want user to wait for some process to finish,
        builderLoading.setView(R.layout.layout_loading_dialog)
        val dialogLoading: AlertDialog = builderLoading.create()

        typeUser = SharedPrefsCache(requireContext()).get("type", "string") as String
        rv =  view.findViewById(R.id.rv_orders)
        rv.layoutManager = LinearLayoutManager(activity)

        val swiper: SwipeRefreshLayout = view.findViewById(R.id.swiperefresh)

        val newStatus: String = if (typeUser === "CLIENT") "ACTUAL" else ""

        swiper.setOnRefreshListener {
            context?.let { orderViewModel.getListOrder(SharedPrefsCache(it).getToken(), typeUser, newStatus)}
            swiper.isRefreshing = false
        }

        orderViewModel.listOrders.observe(viewLifecycleOwner,  {
            adapterOrder = AdapterOrder(it,  object : AdapterOrder.ListAdapterListener {
                override fun onClickAtDetailOrder(order: Order, position: Int) {
                    if (typeUser == "CLIENT") {
                        val intent = Intent(activity, DetailOrderActivity::class.java)
                        intent.putExtra("id_order", order.idOrder)
                        intent.putExtra("code_order", order.orderCode)
                        startActivity(intent)
                    } else {
                        val intent = Intent(activity, ManageOrderActivity::class.java)
                        intent.putExtra("POSITION", position)
                        intent.putExtra("order", order)
                        startActivityForResult(intent, codeManage)
                    }
                }
            })

            rv.adapter = adapterOrder
        })

        orderViewModel.loading.observe(viewLifecycleOwner, {
            if (it)
                dialogLoading.show()
            else
                dialogLoading.hide()
        })

        orderViewModel.textError.observe(viewLifecycleOwner,  {
            Snackbar.make(rv, it as CharSequence, Snackbar.LENGTH_LONG).setBackgroundTint(resources.getColor(R.color.colorPrimary)).show()
        })

        button_new_order.setOnClickListener {
            if (typeUser == "CLIENT")
                startActivityForResult(Intent(activity, NewOrderActivity::class.java), code)
            else
                startActivityForResult(Intent(activity, OrderPendingActivity::class.java), code)
        }
    }
}


