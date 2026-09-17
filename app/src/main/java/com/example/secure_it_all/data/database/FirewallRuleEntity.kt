package com.example.secure_it_all.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "firewall_rules")
data class FirewallRuleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val targetType: String,
    val target: String,
    val action: String,
    val enabled: Boolean = true
)