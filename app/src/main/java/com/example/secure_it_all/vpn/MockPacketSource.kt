package com.example.secure_it_all.vpn

import com.example.secure_it_all.data.models.NetworkConnection


import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

object MockPacketSource {
    private val sampleApps = listOf(
        "com.instagram.android" to "104.244.42.1",
        "com.whatsapp" to "157.240.22.60",
        "com.telegram.messenger" to "149.154.167.51",
        "com.suspicious.testapp" to "185.220.101.5" // domain مشبوه للتجربة
    )

    fun connectionStream(): Flow<NetworkConnection> = flow {
        while (true) {
            delay(Random.nextLong(500, 2000))
            val (pkg, ip) = sampleApps.random()
            emit(
                NetworkConnection(
                    uid = pkg.hashCode(),
                    packageName = pkg,
                    protocol = if (Random.nextBoolean()) "TCP" else "UDP",
                    sourcePort = Random.nextInt(30000, 65000),
                    destinationIp = ip,
                    destinationPort = if (Random.nextBoolean()) 443 else 80,
                    bytesSent = Random.nextLong(100, 50000),
                    bytesReceived = Random.nextLong(100, 50000)
                )
            )
        }
    }
}