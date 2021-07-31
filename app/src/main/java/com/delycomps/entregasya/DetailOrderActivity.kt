package com.delycomps.entregasya

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.delycomps.entregasya.adapters.ExampleTimeLineAdapter
import com.delycomps.entregasya.cache.SharedPrefsCache
import com.delycomps.entregasya.model.Tracking
import kotlinx.android.synthetic.main.activity_detail_order.*

class DetailOrderActivity : AppCompatActivity() {

    private lateinit var mAdapter: ExampleTimeLineAdapter
    private val mDataList = ArrayList<Tracking>()
    private lateinit var mLayoutManager: LinearLayoutManager

    private lateinit var detailOrderViewModel: DetailOrderViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_order)

        detailOrderViewModel = ViewModelProviders.of(this).get(DetailOrderViewModel::class.java)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val idOrder = intent.getIntExtra("id_order", 0)
        val code = intent.getStringExtra("code_order")
        title =  "Tracking $code"

        setDataListItems()

        val builderLoading: AlertDialog.Builder = AlertDialog.Builder(this)
        builderLoading.setCancelable(false) // if you want user to wait for some process to finish,
        builderLoading.setView(R.layout.layout_loading_dialog)
        val dialogLoading: AlertDialog = builderLoading.create()

        dialogLoading.show()
        detailOrderViewModel.getTracking(idOrder, SharedPrefsCache(applicationContext).getToken())

        detailOrderViewModel.trackingList.observe(this, Observer {
            dialogLoading.hide()
            initRecyclerView(it)
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setDataListItems() {
        mDataList.add(Tracking("El pedido fue registrado", "", "DID", "", "", "")) //PENDIENTE
        mDataList.add(Tracking("Esperando a un motorizado tome su pedido", "", "DOING", "", "", "")) //ASIGNADO
        mDataList.add(Tracking("El motorizado está en camino a recoger el pedido", "", "TO DO", "", "", "")) //ASIGNADO
        mDataList.add(Tracking("El motorizado recogió el pedido", "", "TO DO", "", "", "")) //RECOGIDO
        mDataList.add(Tracking("El motorizado está en camino a entregar el pedido", "", "TO DO", "", "", "")) //RECOGIDO
        mDataList.add(Tracking("El motorizado entregó el pedido", "", "TO DO", "", "", "")) // ENTREGADO
    }

    private fun initRecyclerView(listTracking: List<Tracking>) {
        listTracking.forEach {
            when (it.status){
                "PENDIENTE" -> mDataList[0].dateCreated = it.dateCreated
                "ASIGNADO" -> {
                    plate_number.text = it.plateNumber
                    driver_name.text = it.driverFirstName + " " + it.driverLastName
                    mDataList[1].message = "Un motorizado tomó su pedido"
                    mDataList[1].dateCreated = it.dateCreated
                    mDataList[2].dateCreated = it.dateCreated
                    mDataList[0].status = "DID"
                    mDataList[1].status = "DID"
                    mDataList[2].status = "DOING"
                }
                "RECOGIDO" -> {
                    mDataList[3].dateCreated = it.dateCreated
                    mDataList[4].dateCreated = it.dateCreated
                    mDataList[0].status = "DID"
                    mDataList[1].status = "DID"
                    mDataList[2].status = "DID"
                    mDataList[3].status = "DID"
                    mDataList[4].status = "DOING"
                }
                "ENTREGADO" -> {
                    mDataList[0].status = "DID"
                    mDataList[1].status = "DID"
                    mDataList[2].status = "DID"
                    mDataList[3].status = "DID"
                    mDataList[4].status = "DID"
                    mDataList[4].status = "DID"
                    mDataList[5].status = "DID"
                    mDataList[5].dateCreated = it.dateCreated
                }
            }
        }
        mLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        recyclerView.layoutManager = mLayoutManager
        mAdapter = ExampleTimeLineAdapter(mDataList)
        recyclerView.adapter = mAdapter
    }
}
