package com.example.secure_it_all.detection.behavior

import com.example.secure_it_all.data.database.NetworkConnectionEntity
import com.example.secure_it_all.detection.threatintel.LocalThreatList

class BehaviorFeatureExtractor {

    fun extract(
        packageName: String,
        allConnections: List<NetworkConnectionEntity>,
        windowMinutes: Int = 5
    ): AppBehaviorProfile {
        val now = System.currentTimeMillis()
        val windowStart = now - (windowMinutes * 60_000)

        val recent = allConnections.filter {
            it.packageName == packageName && it.timestamp >= windowStart
        }
        val older = allConnections.filter {
            it.packageName == packageName && it.timestamp < windowStart
        }

        val connectionsPerMinute = if (recent.isEmpty()) 0.0
        else recent.size.toDouble() / windowMinutes

        val recentDomains = recent.mapNotNull { it.domain ?: it.destinationIp }.toSet()
        val olderDomains = older.mapNotNull { it.domain ?: it.destinationIp }.toSet()
        val newDomains = recentDomains - olderDomains

        val knownBadCount = recentDomains.count { d -> LocalThreatList.check(d) != null }

        val encryptedCount = recent.count { it.destinationPort == 443 }
        val encryptedRatio = if (recent.isEmpty()) 0.0
        else encryptedCount.toDouble() / recent.size

        val avgBytes = if (recent.isEmpty()) 0.0
        else recent.map { it.bytesSent + it.bytesReceived }.average()

        return AppBehaviorProfile(
            packageName = packageName,
            connectionsPerMinute = connectionsPerMinute,
            uniqueDomainsCount = recentDomains.size,
            newDomainsCount = newDomains.size,
            knownBadDomainsCount = knownBadCount,
            encryptedTrafficRatio = encryptedRatio,
            avgBytesPerConnection = avgBytes
        )
    }
}