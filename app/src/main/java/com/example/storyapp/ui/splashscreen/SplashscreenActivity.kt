package com.example.storyapp.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import com.example.storyapp.R
import com.example.storyapp.data.local.UserPreferences
import com.example.storyapp.data.local.dataStore
import com.example.storyapp.ui.main.MainActivity
import com.example.storyapp.ui.login.LoginActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@SuppressLint("CustomSplashScreen")
class SplashscreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
        val dataStore = UserPreferences.getInstance(this.dataStore)
        lifecycleScope.launch {
            delay(3000)
            withContext(Dispatchers.Main) {
                val user = runBlocking { dataStore.getUserToken().first() }
                if (user.isNullOrEmpty()) {
                    startActivity(Intent(this@SplashscreenActivity, LoginActivity::class.java))
                } else {
                    startActivity(Intent(this@SplashscreenActivity, MainActivity::class.java))
                }
                finish()
            }
        }
    }
}