package com.example.secure_it_all.ui.scanner

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.secure_it_all.data.models.UrlScanResult

@Composable
fun ScannerScreen(viewModel: ScannerViewModel) {
    var input by remember { mutableStateOf("") }
    val state by viewModel.scanState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("Scanner", style = MaterialTheme.typography.headlineSmall)
        Text("Scan links for phishing or scam indicators.", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Paste a link or message") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { viewModel.scanUrl(input) },
            modifier = Modifier.fillMaxWidth(),
            enabled = input.isNotBlank()
        ) { Text("Scan for Scams") }

        Spacer(modifier = Modifier.height(20.dp))

        when (val s = state) {
            is ScanState.Loading -> CircularProgressIndicator()
            is ScanState.Result -> ScanResultCard(s.result)
            else -> {}
        }
    }
}

@Composable
private fun ScanResultCard(result: UrlScanResult) {
    val (containerColor, textColor, label) = when {
        result.confidencePercent >= 70 -> Triple(
            Color(0xFFFFCDD2), Color(0xFFB71C1C), "🚨 Likely a Scam"
        )
        result.confidencePercent >= 40 -> Triple(
            Color(0xFFFFE0B2), Color(0xFFE65100), "⚠️ Suspicious"
        )
        else -> Triple(
            Color(0xFFC8E6C9), Color(0xFF1B5E20), "✅ Looks Safe"
        )
    }

    Card(colors = CardDefaults.cardColors(containerColor = containerColor)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(label, style = MaterialTheme.typography.titleMedium, color = textColor)
                Text("${result.confidencePercent}%", style = MaterialTheme.typography.titleMedium, color = textColor)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(result.recommendation, style = MaterialTheme.typography.bodyMedium, color = textColor)

            if (result.signals.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text("Key Detections:", style = MaterialTheme.typography.labelLarge, color = textColor)
                result.signals.forEach { signal ->
                    Text("• ${signal.description}", style = MaterialTheme.typography.bodySmall, color = textColor)
                }
            }
        }
    }
}

