package com.delycomps.entregasya

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.delycomps.entregasya.cache.SharedPrefsCache
import com.google.firebase.FirebaseApp

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val token: String = SharedPrefsCache(this).getToken()

        if (token != "")
            startActivity(Intent(this, ConnectActivity::class.java))
        else
            startActivity(Intent(this, InitialActivity::class.java))

    }
}