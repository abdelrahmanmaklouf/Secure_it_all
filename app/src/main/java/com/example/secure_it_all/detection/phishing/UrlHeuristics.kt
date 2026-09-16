package com.example.secure_it_all.detection.phishing

import com.example.secure_it_all.data.models.UrlSignal
import java.net.URI

object UrlHeuristics {

    private val commonBrands = listOf("paypal", "facebook", "instagram", "google", "apple", "bank")
    private val suspiciousTlds = setOf(".xyz", ".top", ".tk", ".ml", ".gq", ".zip")

    fun analyze(url: String): List<UrlSignal> {
        val signals = mutableListOf<UrlSignal>()

        val uri = try { URI(url) } catch (e: Exception) { null }
        val host = uri?.host ?: return listOf(UrlSignal("Malformed URL", 30))

        if (host.matches(Regex("^\\d{1,3}(\\.\\d{1,3}){3}$"))) {
            signals.add(UrlSignal("Uses an IP address instead of a domain name", 45))
        }

        if (url.length > 100) {
            signals.add(UrlSignal("Unusually long URL", 10))
        }

        if (suspiciousTlds.any { host.endsWith(it) }) {
            signals.add(UrlSignal("Uses a high-risk domain extension", 15))
        }

        val subdomainCount = host.split(".").size - 2
        if (subdomainCount > 3) {
            signals.add(UrlSignal("Excessive number of subdomains", 15))
        }

        val brandInPath = commonBrands.firstOrNull { brand ->
            host.contains(brand) && !isOfficialDomain(host, brand)
        }
        if (brandInPath != null) {
            signals.add(UrlSignal(
                "Domain mimics \"$brandInPath\" but doesn't match its official domain", 40
            ))
        }

        if (url.count { it == '%' } > 3) {
            signals.add(UrlSignal("Contains excessive encoded characters", 15))
        }

        if (url.contains("@")) {
            signals.add(UrlSignal("Contains '@' symbol, often used to disguise real destination", 30))
        }

        return signals
    }

    private fun isOfficialDomain(host: String, brand: String): Boolean {
        return host == "$brand.com" || host == "www.$brand.com"
    }
}

