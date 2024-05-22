package com.example.storyapp.ui.main


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.storyapp.R
import com.example.storyapp.data.local.UserPreferences
import com.example.storyapp.data.local.dataStore
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.ui.login.LoginActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar: MaterialToolbar = binding.topAppBar
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setSupportActionBar(toolbar)

        binding.apply {
            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_home,
                    R.id.navigation_map
                )
            )
            val bottomNav: BottomNavigationView = bottomNavigation
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
            val navController = navHostFragment.navController
            setupActionBarWithNavController(
                navController, appBarConfiguration
            )
            bottomNav.setupWithNavController(navController)

            topAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.settingsLanguage -> {
                        startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                        true
                    }

                    R.id.logout -> {
                        val dataStore = UserPreferences.getInstance(this@MainActivity.dataStore)
                        lifecycleScope.launch {
                            dataStore.deleteTokenUser()
                            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                            finish()
                        }
                        true
                    }

                    else -> false
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.topbar_menu, menu)
        return true
    }
}