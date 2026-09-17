package com.example.secure_it_all.firewall

import com.example.secure_it_all.data.database.FirewallRuleEntity
import com.example.secure_it_all.data.models.NetworkConnection
import com.example.secure_it_all.detection.threatintel.LocalThreatList

enum class FirewallDecision { ALLOW, BLOCK }

data class FirewallResult(
    val decision: FirewallDecision,
    val reason: String
)

class FirewallEngine(private var rules: List<FirewallRuleEntity> = emptyList()) {

    fun updateRules(newRules: List<FirewallRuleEntity>) {
        rules = newRules
    }

    fun evaluate(connection: NetworkConnection): FirewallResult {
        val threat = LocalThreatList.check(connection.domain ?: connection.destinationIp)
        if (threat != null) {
            return FirewallResult(
                FirewallDecision.BLOCK,
                "Known ${threat.category.lowercase()} domain"
            )
        }

        val appRule = rules.firstOrNull {
            it.targetType == "APP" && it.target == connection.packageName
        }
        if (appRule != null) {
            val decision = if (appRule.action == "BLOCK") FirewallDecision.BLOCK else FirewallDecision.ALLOW
            return FirewallResult(decision, "User rule (app)")
        }

        val domainRule = rules.firstOrNull {
            it.targetType == "DOMAIN" &&
                    (it.target == connection.domain || it.target == connection.destinationIp)
        }
        if (domainRule != null) {
            val decision = if (domainRule.action == "BLOCK") FirewallDecision.BLOCK else FirewallDecision.ALLOW
            return FirewallResult(decision, "User rule (domain)")
        }

        return FirewallResult(FirewallDecision.ALLOW, "No matching rule")
    }
}