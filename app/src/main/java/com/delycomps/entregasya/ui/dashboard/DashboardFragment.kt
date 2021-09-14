package com.delycomps.entregasya.ui.dashboard

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.delycomps.entregasya.*
import com.delycomps.entregasya.adapters.AdapterOrder
import com.delycomps.entregasya.cache.SharedPrefsCache
import com.delycomps.entregasya.model.Order
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_order.*

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var adapterOrder: AdapterOrder
    private lateinit var rv: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        context?.let { dashboardViewModel.getListOrder(SharedPrefsCache(it).getToken(), SharedPrefsCache(it).get("type", "string") as String)}
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val builderLoading: AlertDialog.Builder = AlertDialog.Builder(activity)
        builderLoading.setCancelable(false) // if you want user to wait for some process to finish,
        builderLoading.setView(R.layout.layout_loading_dialog)
        val dialogLoading: AlertDialog = builderLoading.create()

        val typeUser: String = SharedPrefsCache(requireContext()).get("type", "string") as String
        rv =  view.findViewById(R.id.rv_orders)
        rv.layoutManager = LinearLayoutManager(activity)

        rv =  view.findViewById(R.id.rv_orders)
        rv.layoutManager = LinearLayoutManager(activity)

        val swiper: SwipeRefreshLayout = view.findViewById(R.id.swiperefresh)


        swiper.setOnRefreshListener {
            context?.let { dashboardViewModel.getListOrder(SharedPrefsCache(it).getToken(), typeUser, "")}
            swiper.isRefreshing = false
        }

        dashboardViewModel.listOrders.observe(viewLifecycleOwner,  {
            adapterOrder = AdapterOrder(it,  object : AdapterOrder.ListAdapterListener {
                override fun onClickAtDetailOrder(order: Order, position: Int) {

                }
            })

            rv.adapter = adapterOrder
        })

        dashboardViewModel.loading.observe(viewLifecycleOwner, {
            if (it)
                dialogLoading.show()
            else
                dialogLoading.hide()
        })

        dashboardViewModel.textError.observe(viewLifecycleOwner,  {
            Snackbar.make(rv, it as CharSequence, Snackbar.LENGTH_LONG).setBackgroundTint(resources.getColor(R.color.colorPrimary)).show()
        })
    }
}
