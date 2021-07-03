package com.delycomps.entregasya

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.delycomps.entregasya.cache.SharedPrefsCache

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val token: String = SharedPrefsCache(this).getToken()

        Handler().postDelayed({
            if (token != "")
                startActivity(Intent(this, HomeActivity::class.java))
            else
                startActivity(Intent(this, InitialActivity::class.java))
        }, 1000)

    }
}