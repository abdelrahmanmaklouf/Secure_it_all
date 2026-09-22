package com.example.secure_it_all

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.secure_it_all.data.database.AppDatabase

import com.example.secure_it_all.ui.appmonitor.AppMonitorFragment
import com.example.secure_it_all.ui.dashboard.HomeFragment
import com.example.secure_it_all.ui.scanner.ScannerFragment
import com.example.secure_it_all.ui.firewall.FirewallFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.secure_it_all.ui.alerts.AlertsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, HomeFragment())
                .commit()
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        bottomNav.setOnItemSelectedListener {
            val fragment = when (it.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_scanner -> ScannerFragment()
                R.id.nav_apps -> AppMonitorFragment()
                R.id.nav_rules -> FirewallFragment()
                R.id.nav_alerts -> AlertsFragment()

                else -> HomeFragment()
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit()
            true
        }
    }
}