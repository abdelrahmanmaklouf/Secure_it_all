package com.example.secure_it_all

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.secure_it_all.data.database.AppDatabase
import com.example.secure_it_all.ui.dashboard.DashboardScreen
import com.example.secure_it_all.ui.dashboard.DashboardViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.getInstance(applicationContext)
        val viewModel = DashboardViewModel(db)

        setContent {
            DashboardScreen(viewModel)
        }
    }
}