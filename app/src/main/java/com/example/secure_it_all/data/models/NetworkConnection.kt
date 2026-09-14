package com.example.secure_it_all.data.models

data class NetworkConnection(
    val uid: Int,
    val packageName: String?,
    val timestamp: Long = System.currentTimeMillis(),
    val protocol: String,      // "TCP", "UDP", "DNS"
    val sourcePort: Int,
    val destinationIp: String,
    val destinationPort: Int,
    val domain: String? = null,
    val bytesSent: Long = 0,
    val bytesReceived: Long = 0
)