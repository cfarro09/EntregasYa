package com.delycomps.entregasya

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewParent
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.delycomps.entregasya.model.Order

class ManageOrderActivity : AppCompatActivity() {

    private lateinit var order: Order

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
            }
            handled
        }

    }

}
