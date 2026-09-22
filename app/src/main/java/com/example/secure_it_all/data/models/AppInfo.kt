package com.example.secure_it_all.data.models

data class AppInfo(
    val packageName: String,
    val appName: String,
    val installTime: Long,
    val isSystemApp: Boolean,
    val installerSource: String?,
    val requestedPermissions: List<String>,
    val dangerousPermissionsCount: Int
)