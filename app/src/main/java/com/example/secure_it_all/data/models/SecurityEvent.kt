package com.example.secure_it_all.data.models

import java.util.Date

enum class EventSource { NETWORK, APP, URL, SYSTEM }
enum class Severity { LOW, MEDIUM, HIGH, CRITICAL }

data class SecurityEvent(
    val id: Long = 0,
    val timestamp: Date = Date(),
    val source: EventSource,
    val packageName: String? = null,
    val eventType: String,          // مثلاً "SUSPICIOUS_DOMAIN", "APP_INSTALLED"
    val destination: String? = null, // domain أو IP لو منطبق
    val metadata: Map<String, String> = emptyMap(),
    val severity: Severity = Severity.LOW
)