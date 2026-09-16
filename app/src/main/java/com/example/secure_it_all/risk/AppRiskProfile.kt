package com.example.secure_it_all.risk

data class RiskReason(val description: String, val points: Int)

data class AppRiskProfile(
    val packageName: String,
    val appName: String,
    val score: Int,
    val level: RiskLevel,
    val reasons: List<RiskReason>
)