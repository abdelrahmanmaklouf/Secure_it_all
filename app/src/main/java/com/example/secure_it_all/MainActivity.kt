package com.example.secure_it_all

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.secure_it_all.data.database.AppDatabase
import com.example.secure_it_all.ui.dashboard.DashboardScreen
import com.example.secure_it_all.ui.dashboard.DashboardViewModel
import com.example.secure_it_all.ui.network.NetworkConnectionViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getInstance(applicationContext)

        val dashboardViewModel = DashboardViewModel(db)

        NetworkConnectionViewModel(db)

        setContent {
            DashboardScreen(dashboardViewModel)
        }
    }
}