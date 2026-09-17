package com.example.secure_it_all.detection.threatintel

object LocalThreatList {
    private val entries = mapOf(
        "suspicious-domain.test" to ThreatListEntry("suspicious-domain.test", "MALWARE", 90),
        "secure-bank-login.example" to ThreatListEntry("secure-bank-login.example", "PHISHING", 85),
        "185.220.101.5" to ThreatListEntry("185.220.101.5", "MALWARE", 75)
    )

    fun check(domainOrIp: String?): ThreatListEntry? {
        if (domainOrIp == null) return null
        return entries[domainOrIp]
    }
}

    private val threats = mapOf(
        "test-phishing-example.com" to ThreatEntry("PHISHING", 80)
    )

    fun check(host: String): ThreatEntry? {
        return threats[host]
    }


data class ThreatEntry(
    val category: String,
    val score: Int
)

