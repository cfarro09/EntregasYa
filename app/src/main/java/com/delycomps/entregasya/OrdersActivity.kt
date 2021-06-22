package com.delycomps.entregasya

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_orders.*

class OrdersActivity : AppCompatActivity() {
    companion object {
        private lateinit var rv: RecyclerView
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        rv = rv_orders
        rv.layoutManager = LinearLayoutManager(this)
//        orderViewModel = ViewModelProviders.of(this).get(OrderViewModel::class.java)
    }
}
