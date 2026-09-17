package com.example.secure_it_all.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.secure_it_all.data.database.AppDatabase
import com.example.secure_it_all.ui.dashboard.DashboardScreen
import com.example.secure_it_all.ui.dashboard.DashboardViewModel
import androidx.compose.foundation.layout.padding
import com.example.secure_it_all.ui.scanner.ScannerScreen
import com.example.secure_it_all.ui.scanner.ScannerViewModel

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Home", Icons.Default.Home)
    object Scanner : Screen("scanner", "Scan", Icons.Default.Search)
    object Risk : Screen("risk", "Apps", Icons.Default.List)
    object Firewall : Screen("firewall", "Firewall", Icons.Default.Shield)
}

@Composable
fun AppNavigation(db: AppDatabase) {
    val navController = rememberNavController()
    val items = listOf(Screen.Dashboard, Screen.Scanner, Screen.Risk, Screen.Firewall)

    val dashboardViewModel = remember { DashboardViewModel(db) }
    val scannerViewModel = remember { ScannerViewModel() }
    // TODO: لما شاشات Scanner وRisk وFirewall تكون جاهزة، نضيف هنا:
    // val riskViewModel = remember { RiskViewModel(db) }
    // val connectionsViewModel = remember { ConnectionsViewModel(db) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { screen ->
                    NavigationBarItem(
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Dashboard.route) { DashboardScreen(dashboardViewModel) }
            composable(Screen.Scanner.route) { ScannerScreen(scannerViewModel) }
            composable(Screen.Risk.route) { Text("Risk screen - coming soon") }
            composable(Screen.Firewall.route) { Text("Firewall screen - coming soon") }
        }
    }
}