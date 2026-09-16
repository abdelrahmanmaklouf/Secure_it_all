package com.example.secure_it_all.detection.phishing

import com.example.secure_it_all.data.models.UrlScanResult
import com.example.secure_it_all.data.models.UrlSignal
import com.example.secure_it_all.detection.threatintel.LocalThreatList
import java.net.URI

class PhishingChecker {

    fun scan(url: String): UrlScanResult {
        val signals = mutableListOf<UrlSignal>()

        val host = try { URI(url).host } catch (e: Exception) { null }
        val knownThreat = host?.let { LocalThreatList.check(it) }

        if (knownThreat != null) {
            signals.add(UrlSignal("Matches a known ${knownThreat.category.lowercase()} domain", 70))
        } else {
            signals.addAll(UrlHeuristics.analyze(url))
        }

        val totalScore = signals.sumOf { it.weight }.coerceAtMost(100)
        val isSuspicious = totalScore >= 40

        return UrlScanResult(
            url = url,
            isSuspicious = isSuspicious,
            confidencePercent = totalScore,
            signals = signals,
            recommendation = when {
                totalScore >= 70 -> "Do not respond or click any links. Report and delete this message immediately."
                totalScore >= 40 -> "This link shows suspicious signs. Avoid clicking it and verify the source before proceeding."
                else -> "No immediate threats detected. Stay cautious with unfamiliar links."
            }
        )
    }
}

