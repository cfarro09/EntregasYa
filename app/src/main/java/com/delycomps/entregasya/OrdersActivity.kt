package com.delycomps.entregasya

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.delycomps.entregasya.cache.SharedPrefsCache
import com.delycomps.entregasya.model.Order
import com.delycomps.entregasya.webservice.Repository
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_orders.*

class OrdersActivity : AppCompatActivity() {
    companion object {
        private lateinit var rv: RecyclerView
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        val builderLoading: AlertDialog.Builder = AlertDialog.Builder(this)
        builderLoading.setCancelable(false) // if you want user to wait for some process to finish,
        builderLoading.setView(R.layout.layout_loading_dialog)
        val dialogLoading: AlertDialog = builderLoading.create()

        rv = rv_orders
        rv.layoutManager = LinearLayoutManager(this)

        dialogLoading.show()
        Repository().getOrder(SharedPrefsCache(this).getToken()) { isSuccess, result, message ->
            dialogLoading.hide()
            if (isSuccess) {
                rv.adapter = AdapterOrder(result!!, null)
            } else {
                val oo = Order(1, "23232", "Cementro 40kg", "Carlos Farro",
                    "DNI","73147683", "943856850", "Jr enrique barron 1323",
                    "Por mi casa", "David Diaz", "DNI", "73147682", "943856850",
                    "03", "David Diaz", "DNI", "73147683", "943856850",
                    "03", "David Diaz", "DNI", "73147683", "DNI", "3232",
                    "943856850", "03", "NINGUNO", "01/01/1994")
//                val dd:List<Order> = listOf(oo, oo, oo)
                rv.adapter = AdapterOrder(listOf(oo, oo, oo), null)
                Snackbar.make(rv, message as CharSequence, Snackbar.LENGTH_LONG)
                    .setBackgroundTint(resources.getColor(R.color.colorPrimary)).show()
            }
        }
    }
}
