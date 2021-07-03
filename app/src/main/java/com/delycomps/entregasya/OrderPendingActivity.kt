package com.delycomps.entregasya

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.delycomps.entregasya.adapters.AdapterOrder
import com.delycomps.entregasya.cache.SharedPrefsCache
import com.delycomps.entregasya.model.Order
import com.google.android.material.snackbar.Snackbar

class OrderPendingActivity : AppCompatActivity() {
    private lateinit var orderViewModel: OrderPendingViewModel
    private lateinit var rv: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_pending)
        val contextff = this;
        orderViewModel = ViewModelProviders.of(this).get(OrderPendingViewModel::class.java)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        orderViewModel.getListOrder(SharedPrefsCache(this).getToken(), SharedPrefsCache(this).get("type", "string") as String)

        val swiper: SwipeRefreshLayout = findViewById(R.id.swiperefresh)
        swiper.setOnRefreshListener {
            orderViewModel.getListOrder(SharedPrefsCache(this).getToken(), SharedPrefsCache(this).get("type", "string") as String)
            swiper.isRefreshing = false
        }
        val builderLoading: AlertDialog.Builder = AlertDialog.Builder(this)
        builderLoading.setCancelable(false) // if you want user to wait for some process to finish,
        builderLoading.setView(R.layout.layout_loading_dialog)
        val dialogLoading: AlertDialog = builderLoading.create()

        rv =  findViewById(R.id.rv_orders)
        rv.layoutManager = LinearLayoutManager(this)

        orderViewModel.listOrders.observe(this, Observer {
            rv.adapter = AdapterOrder(it,  object : AdapterOrder.ListAdapterListener {
                override fun onClickAtDetailOrder(order: Order, position: Int) {

                    val dialogClickListener = DialogInterface.OnClickListener { _, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                                orderViewModel.assignOrder(SharedPrefsCache(contextff).getToken(), order.idOrder)
                            }
                        }
                    }
                    val builder = AlertDialog.Builder(contextff)
                    builder.setMessage("¿Está seguro de tomar el pedido?")
                        .setPositiveButton(Html.fromHtml("<b>ACEPTAR<b>"), dialogClickListener)
                        .setNegativeButton(Html.fromHtml("<b>CANCELAR<b>"), dialogClickListener)
                    val alert = builder.create()
                    alert.show()
                }
            })
        })

        orderViewModel.success.observe(this, Observer {
            if (it) {
                val data = Intent()
                data.putExtra("neworder", true)
                setResult(RESULT_OK, data)
                finish()
            }
            else
                Snackbar.make(rv, "Hubo un error.", Snackbar.LENGTH_LONG).show()
        })

        orderViewModel.loading.observe(this, Observer {
            if (it)
                dialogLoading.show()
            else
                dialogLoading.hide()
        })

        orderViewModel.textError.observe(this, Observer {
            Snackbar.make(rv, it as CharSequence, Snackbar.LENGTH_LONG).setBackgroundTint(resources.getColor(R.color.colorPrimary)).show()
        })
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
