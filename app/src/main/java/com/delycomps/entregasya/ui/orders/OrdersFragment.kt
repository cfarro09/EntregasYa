package com.delycomps.entregasya.ui.orders

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
import com.delycomps.entregasya.AdapterOrder
import com.delycomps.entregasya.NewOrderActivity
import com.delycomps.entregasya.R
import com.delycomps.entregasya.cache.SharedPrefsCache
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_order.*

class OrdersFragment : Fragment() {

    private lateinit var orderViewModel: OrdersViewModel
    private lateinit var rv: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        orderViewModel = ViewModelProviders.of(this).get(OrdersViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_order, container, false)

        context?.let { orderViewModel.getListOrder(SharedPrefsCache(it).getToken())}

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val builderLoading: AlertDialog.Builder = AlertDialog.Builder(activity)
        builderLoading.setCancelable(false) // if you want user to wait for some process to finish,
        builderLoading.setView(R.layout.layout_loading_dialog)
        val dialogLoading: AlertDialog = builderLoading.create()

        rv =  view.findViewById(R.id.rv_orders)
        rv.layoutManager = LinearLayoutManager(activity)

        orderViewModel.listOrders.observe(viewLifecycleOwner, Observer {
            rv.adapter = AdapterOrder(it, null)
        })

        orderViewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it)
                dialogLoading.show()
            else
                dialogLoading.hide()
        })

        orderViewModel.textError.observe(viewLifecycleOwner, Observer {
            Snackbar.make(rv, it as CharSequence, Snackbar.LENGTH_LONG).setBackgroundTint(resources.getColor(R.color.colorPrimary)).show()
        })

        button_new_order.setOnClickListener {
            context?.let { it1 -> it1.startActivity(Intent(it1, NewOrderActivity::class.java)) }
        }
    }
}

