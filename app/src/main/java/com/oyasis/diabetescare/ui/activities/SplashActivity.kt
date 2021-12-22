package com.oyasis.diabetescare.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            delay(2000)
            val intent = Intent(this@SplashActivity, NavHost::class.java)
            startActivity(intent)
            finish()
        }
    }
}