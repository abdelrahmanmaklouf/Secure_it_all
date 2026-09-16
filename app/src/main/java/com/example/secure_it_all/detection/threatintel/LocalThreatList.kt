package com.example.secure_it_all.detection.threatintel

object LocalThreatList {

    private val threats = mapOf(
        "test-phishing-example.com" to ThreatEntry("PHISHING", 80)
    )

    fun check(host: String): ThreatEntry? {
        return threats[host]
    }
}

data class ThreatEntry(
    val category: String,
    val score: Int
)

