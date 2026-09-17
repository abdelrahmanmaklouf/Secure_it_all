package com.example.secure_it_all.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_info")
data class AppInfoEntity(
    @PrimaryKey val packageName: String,
    val appName: String,
    val installTime: Long,
    val installerSource: String?,
    val dangerousPermissionsCount: Int,
    val permissionsCsv: String
)