package com.example.secure_it_all.ui.firewall

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.secure_it_all.data.database.FirewallRuleEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirewallScreen(viewModel: FirewallViewModel) {
    val rules by viewModel.rules.collectAsState(initial = emptyList())
    val apps = viewModel.installedApps
    val connectionsWithDecision by viewModel.connectionsWithDecision.collectAsState(initial = emptyList())

    var selectedPackage by remember { mutableStateOf<String?>(null) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {

        Text("Add App Rule", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        // Dropdown لاختيار التطبيق بدل كتابة اسم الـ package يدويًا
        ExposedDropdownMenuBox(
            expanded = dropdownExpanded,
            onExpandedChange = { dropdownExpanded = it }
        ) {
            OutlinedTextField(
                value = apps.find { it.packageName == selectedPackage }?.appName ?: "Select app",
                onValueChange = {},
                readOnly = true,
                label = { Text("App") },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false }
            ) {
                apps.forEach { app ->
                    DropdownMenuItem(
                        text = { Text(app.appName) },
                        onClick = {
                            selectedPackage = app.packageName
                            dropdownExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Row {
            Button(
                onClick = { selectedPackage?.let { viewModel.addAppRule(it, block = true) } },
                enabled = selectedPackage != null
            ) { Text("Block") }

            Spacer(Modifier.width(8.dp))

            Button(
                onClick = { selectedPackage?.let { viewModel.addAppRule(it, block = false) } },
                enabled = selectedPackage != null
            ) { Text("Allow") }
        }

        Spacer(Modifier.height(20.dp))
        HorizontalDivider()
        Spacer(Modifier.height(12.dp))

        Text("Current Rules", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(rules) { rule ->
                FirewallRuleRow(rule = rule, onDelete = { viewModel.deleteRule(rule) })
            }
        }

        Spacer(Modifier.height(20.dp))
        HorizontalDivider()
        Spacer(Modifier.height(12.dp))

        Text("Recent Connections", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        Button(onClick = { viewModel.startTestData() }) {
            Text("Start Test Data")
        }
        Spacer(Modifier.height(8.dp))

        Button(onClick = { viewModel.addTestBlockRule() }) {
            Text("Add Test Rule (Block suspicious app)")
        }
        Spacer(Modifier.height(8.dp))

        Button(onClick = { viewModel.addTestAppData() }) {
            Text("Add Test App Data")
        }
        Spacer(Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(connectionsWithDecision) { (conn, result) ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(conn.domain ?: conn.destinationIp, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            "${result.decision} — ${result.reason}",
                            color = if (result.decision == com.example.secure_it_all.firewall.FirewallDecision.BLOCK)
                                MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FirewallRuleRow(rule: FirewallRuleEntity, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("${rule.targetType}: ${rule.target}", style = MaterialTheme.typography.bodyMedium)
                Text(
                    rule.action,
                    color = if (rule.action == "BLOCK") MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = onDelete) {
                Text("✕")
            }
        }
    }
}