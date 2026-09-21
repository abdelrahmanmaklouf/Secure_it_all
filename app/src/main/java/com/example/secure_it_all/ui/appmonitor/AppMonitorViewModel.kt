package com.example.secure_it_all.ui.appmonitor

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.secure_it_all.data.database.AppDatabase
import com.example.secure_it_all.data.database.AppInfoEntity
import com.example.secure_it_all.monitoring.AppScanner
import kotlinx.coroutines.launch

class AppMonitorViewModel(app: Application, private val db: AppDatabase) : AndroidViewModel(app) {
    val apps = db.appInfoDao().getAllApps()

    init {
        scanNow()
    }

    fun scanNow() {
        viewModelScope.launch {
            val scanner = AppScanner(getApplication())
            val results = scanner.scanInstalledApps().map {
                AppInfoEntity(
                    packageName = it.packageName,
                    appName = it.appName,
                    installTime = it.installTime,
                    installerSource = it.installerSource,
                    dangerousPermissionsCount = it.dangerousPermissionsCount,
                    permissionsCsv = it.requestedPermissions.joinToString(",")
                )
            }
            db.appInfoDao().insertAll(results)
        }
    }
}