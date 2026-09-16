package com.example.secure_it_all.detection.threatintel

data class ThreatListEntry(
    val domain: String,
    val category: String,
    val severity: Int
)