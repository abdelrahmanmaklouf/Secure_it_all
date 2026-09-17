package com.example.secure_it_all.risk

import com.example.secure_it_all.data.database.AppInfoEntity
import com.example.secure_it_all.data.database.NetworkConnectionEntity
import com.example.secure_it_all.detection.behavior.BehaviorFeatureExtractor
import com.example.secure_it_all.detection.behavior.BehaviorRuleEngine
import com.example.secure_it_all.detection.threatintel.LocalThreatList

class RiskEngine {

    private val behaviorExtractor = BehaviorFeatureExtractor()
    private val behaviorRules = BehaviorRuleEngine()

    fun calculateRisk(
        app: AppInfoEntity,
        connections: List<NetworkConnectionEntity>
    ): AppRiskProfile {
        val reasons = mutableListOf<RiskReason>()
        var score = 0

        if (app.dangerousPermissionsCount >= 3) {
            val points = RiskScoringRules.EXCESSIVE_PERMISSIONS
            score += points
            reasons.add(RiskReason("Requests ${app.dangerousPermissionsCount} sensitive permissions", points))
        }

        if (app.installerSource != "com.android.vending") {
            val points = RiskScoringRules.NOT_FROM_PLAY_STORE
            score += points
            reasons.add(RiskReason("Not installed from Play Store", points))
        }

        val appConnections = connections.filter { it.packageName == app.packageName }
        val hasKnownMalicious = appConnections.any {
            LocalThreatList.check(it.domain ?: it.destinationIp) != null
        }
        if (hasKnownMalicious) {
            val points = RiskScoringRules.KNOWN_MALICIOUS_INDICATOR
            score += points
            reasons.add(RiskReason("Contacted a known malicious domain", points))
        }

        val behaviorProfile = behaviorExtractor.extract(app.packageName, connections)
        val anomalies = behaviorRules.evaluate(behaviorProfile)
        anomalies.forEach { finding ->
            score += finding.severity
            reasons.add(RiskReason(finding.description, finding.severity))
        }

        val clampedScore = score.coerceAtMost(RiskScoringRules.MAX_SCORE)

        return AppRiskProfile(
            packageName = app.packageName,
            appName = app.appName,
            score = clampedScore,
            level = RiskScoringRules.classify(clampedScore),
            reasons = reasons
        )
    }
}