package com.example.secure_it_all.ui.alerts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.secure_it_all.data.database.SecurityEventEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AlertsScreen(viewModel: AlertsViewModel) {
    val events by viewModel.events.collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
        Text("Alerts", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        if (events.isEmpty()) {
            Text(
                "No security events yet",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        LazyColumn {
            items(events) { event ->
                AlertRow(event)
            }
        }
    }
}

@Composable
fun AlertRow(event: SecurityEventEntity) {
    val sdf = remember { SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()) }
    val severityColor = when (event.severity.uppercase()) {
        "HIGH" -> MaterialTheme.colorScheme.error
        "MEDIUM" -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.primary
    }

    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(event.eventType, style = MaterialTheme.typography.bodyMedium)
                Text(event.severity, color = severityColor)
            }
            event.packageName?.let {
                Text("App: $it", style = MaterialTheme.typography.bodySmall)
            }
            event.destination?.let {
                Text("To: $it", style = MaterialTheme.typography.bodySmall)
            }
            Text(
                sdf.format(Date(event.timestamp)),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}