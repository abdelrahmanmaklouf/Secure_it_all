package com.example.secure_it_all.risk

object RiskScoringRules {
    const val EXCESSIVE_PERMISSIONS = 10
    const val NOT_FROM_PLAY_STORE = 15
    const val KNOWN_MALICIOUS_INDICATOR = 70
    const val MAX_SCORE = 100

    fun classify(score: Int): RiskLevel = when {
        score >= 80 -> RiskLevel.HIGH
        score >= 60 -> RiskLevel.SUSPICIOUS
        score >= 30 -> RiskLevel.LOW
        else -> RiskLevel.SAFE
    }
}

enum class RiskLevel(val label: String, val emoji: String) {
    SAFE("Safe", "\uD83D\uDFE2"),
    LOW("Low", "\uD83D\uDFE1"),
    SUSPICIOUS("Suspicious", "\uD83D\uDFE0"),
    HIGH("High Risk", "\uD83D\uDD34")
}