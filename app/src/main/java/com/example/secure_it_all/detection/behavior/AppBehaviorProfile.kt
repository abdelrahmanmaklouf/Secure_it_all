package com.example.secure_it_all.detection.behavior

data class AppBehaviorProfile(
    val packageName: String,
    val connectionsPerMinute: Double,
    val uniqueDomainsCount: Int,
    val newDomainsCount: Int,
    val knownBadDomainsCount: Int,
    val encryptedTrafficRatio: Double,
    val avgBytesPerConnection: Double
)