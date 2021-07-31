package com.delycomps.entregasya

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ViewParent
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.delycomps.entregasya.helpers.Helpers
import com.delycomps.entregasya.model.Order
import com.google.android.material.snackbar.Snackbar

class ManageOrderActivity : AppCompatActivity() {

    private lateinit var order: Order
    private val CODE_CALL_PHONE = 2
    private var phoneToCall:String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_order)
        val navView: BottomNavigationView = findViewById(R.id.manage_nav_view)

        val navController = findNavController(R.id.manage_nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_info, R.id.navigation_gallery)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        order =  intent.getParcelableExtra("order")!!

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "PEDIDO ${order.orderCode}"

        navView.setOnNavigationItemSelectedListener { menuItem ->
            val handled = NavigationUI.onNavDestinationSelected(menuItem, navController)
            if (handled) {
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                supportActionBar?.title = "PEDIDO ${order.orderCode}"
                val parent: ViewParent = navView.parent
                if (parent is DrawerLayout) {
                    parent.closeDrawer(GravityCompat.START)
                }
            } else {
                when (menuItem.itemId) {
                    R.id.navigation_call -> {
                        phoneToCall = if(order.status == "ASIGNADO") order.deliveryPhone else order.deliveryPhone

                        if(phoneToCall.isNullOrEmpty()){
                            Toast.makeText(this, "La orden no cuenta con teléfono para llamar", Toast.LENGTH_LONG).show()
                        } else {
                            if(Helpers.verifyPermission(this, Manifest.permission.CALL_PHONE, CODE_CALL_PHONE)){
                                callOrder(phoneToCall!!)
                            }
                        }
                    }
                }
            }
            handled
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CODE_CALL_PHONE ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!phoneToCall.isNullOrEmpty())
                        callOrder(phoneToCall!!)
                }
                else
                    Helpers.showToast(this,"Haz rechazado la petición, por favor considere en aceptarla desde ajustes.")
        }
    }
    private fun callOrder(phone: String){
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:${phone}" )
        callIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(callIntent)
    }
}
