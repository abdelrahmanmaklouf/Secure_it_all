package com.example.secure_it_all.data.database


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "security_events")
data class SecurityEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val source: String,
    val packageName: String?,
    val eventType: String,
    val destination: String?,
    val severity: String
)
