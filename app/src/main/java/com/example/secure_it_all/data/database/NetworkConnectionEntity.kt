package com.example.secure_it_all.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "network_connections")
data class NetworkConnectionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val uid: Int,
    val packageName: String?,
    val timestamp: Long,
    val protocol: String,
    val destinationIp: String,
    val destinationPort: Int,
    val domain: String?,
    val bytesSent: Long,
    val bytesReceived: Long
)