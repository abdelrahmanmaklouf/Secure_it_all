package com.example.secure_it_all.monitoring

import android.content.Context
import android.content.pm.PackageManager
import com.example.secure_it_all.data.models.AppInfo

class AppScanner(private val context: Context) {

    private val dangerousPermissions = setOf(
        "android.permission.CAMERA",
        "android.permission.RECORD_AUDIO",
        "android.permission.ACCESS_FINE_LOCATION",
        "android.permission.READ_CONTACTS",
        "android.permission.READ_SMS",
        "android.permission.SEND_SMS",
        "android.permission.READ_CALL_LOG",
        "android.permission.SYSTEM_ALERT_WINDOW",
        "android.permission.BIND_ACCESSIBILITY_SERVICE"
    )

    fun scanInstalledApps(): List<AppInfo> {
        val pm = context.packageManager
        val packages = pm.getInstalledPackages(PackageManager.GET_PERMISSIONS)

        return packages.mapNotNull { pkgInfo ->
            try {
                val appInfo = pkgInfo.applicationInfo ?: return@mapNotNull null
                val appName = pm.getApplicationLabel(appInfo).toString()
                val isSystemApp = (appInfo.flags and
                        android.content.pm.ApplicationInfo.FLAG_SYSTEM) != 0

                val installerSource = try {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                        pm.getInstallSourceInfo(pkgInfo.packageName).installingPackageName
                    } else {
                        @Suppress("DEPRECATION")
                        pm.getInstallerPackageName(pkgInfo.packageName)
                    }
                } catch (e: Exception) {
                    null
                }
                val permissions = pkgInfo.requestedPermissions?.toList() ?: emptyList()
                val dangerousCount = permissions.count { it in dangerousPermissions }

                AppInfo(
                    packageName = pkgInfo.packageName,
                    appName = appName,
                    installTime = pkgInfo.firstInstallTime,
                    isSystemApp = isSystemApp,
                    installerSource = installerSource,
                    requestedPermissions = permissions,
                    dangerousPermissionsCount = dangerousCount
                )
            } catch (e: Exception) {
                null
            }
        }.filter { !it.isSystemApp }
    }
}