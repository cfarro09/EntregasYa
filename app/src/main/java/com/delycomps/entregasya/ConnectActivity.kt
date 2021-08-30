package com.delycomps.entregasya

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.delycomps.entregasya.cache.SharedPrefsCache
import kotlinx.android.synthetic.main.activity_connect.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ConnectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect)

        val firstName = SharedPrefsCache(this).get("first_name", "string") as CharSequence?
        val lastName = SharedPrefsCache(this).get("last_name", "string") as CharSequence?

        current_driver.text = "$firstName $lastName"

        button_connect.setOnClickListener {
            startActivity(Intent(this, com.delycomps.entregasya.HomeActivity::class.java))
        }
    }
}