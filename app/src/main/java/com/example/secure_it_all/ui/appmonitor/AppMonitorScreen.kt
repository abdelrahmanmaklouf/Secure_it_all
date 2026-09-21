package com.example.secure_it_all.ui.appmonitor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppMonitorScreen(viewModel: AppMonitorViewModel) {
    val apps by viewModel.apps.collectAsState(initial = emptyList())

    LazyColumn(modifier = Modifier.padding(12.dp)) {
        items(apps) { app ->
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(app.appName, style = MaterialTheme.typography.titleMedium)
                    Text(app.packageName, style = MaterialTheme.typography.bodySmall)
                    val riskLabel = when {
                        app.dangerousPermissionsCount >= 4 -> "🔴 High sensitivity"
                        app.dangerousPermissionsCount >= 2 -> "🟡 Medium sensitivity"
                        else -> "🟢 Low sensitivity"
                    }
                    Text("$riskLabel — ${app.dangerousPermissionsCount} sensitive permissions")
                    if (app.installerSource != "com.android.vending") {
                        Text(
                            "⚠️ Not installed from Play Store (source: ${app.installerSource ?: "unknown"})",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}