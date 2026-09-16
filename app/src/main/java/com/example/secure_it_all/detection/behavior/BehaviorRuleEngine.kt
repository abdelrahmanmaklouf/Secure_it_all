package com.example.secure_it_all.detection.behavior

data class AnomalyFinding(val description: String, val severity: Int)

class BehaviorRuleEngine {

    fun evaluate(profile: AppBehaviorProfile): List<AnomalyFinding> {
        val findings = mutableListOf<AnomalyFinding>()

        if (profile.connectionsPerMinute > 20) {
            findings.add(
                AnomalyFinding(
                    "Unusually high connection rate (${profile.connectionsPerMinute.toInt()}/min)", 25
                )
            )
        }

        if (profile.newDomainsCount > 10) {
            findings.add(
                AnomalyFinding("Contacted ${profile.newDomainsCount} new domains recently", 20)
            )
        }

        if (profile.knownBadDomainsCount > 0) {
            findings.add(
                AnomalyFinding(
                    "Communicating with ${profile.knownBadDomainsCount} known malicious domain(s)", 70
                )
            )
        }

        if (profile.uniqueDomainsCount > 30) {
            findings.add(
                AnomalyFinding(
                    "Contacts an unusually large number of distinct domains (${profile.uniqueDomainsCount})", 15
                )
            )
        }

        return findings
    }
}