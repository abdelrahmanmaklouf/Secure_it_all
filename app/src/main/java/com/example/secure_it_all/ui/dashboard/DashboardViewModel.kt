package com.example.secure_it_all.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.secure_it_all.data.database.AppDatabase
import com.example.secure_it_all.model.AlertStatus
import com.example.secure_it_all.model.AppAlert
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.Calendar

data class DashboardState(
    val serviceRunning: Boolean = true,
    val monitoredAppsCount: Int = 0,
    val connectionsToday: Int = 0,
    val threatsBlocked: Int = 0,
    val needALookCount: Int = 0,
    val alerts: List<AppAlert> = emptyList()
)

class DashboardViewModel(private val db: AppDatabase) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state

    init {
        viewModelScope.launch {
            combine(
                db.appInfoDao().getAllApps(),
                db.networkConnectionDao().getRecentConnections(),
                db.securityEventDao().getAllEvents()
            ) { apps, connections, events ->

                val startOfDay = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis

                val connectionsToday = connections.count { it.timestamp >= startOfDay }
                val threatsBlockedToday = events.count {
                    it.eventType == "Connection Blocked" && it.timestamp >= startOfDay
                }
                val needALook = apps.count { it.dangerousPermissionsCount >= 4 }

                val alerts = apps
                    .filter { it.dangerousPermissionsCount >= 2 }
                    .sortedByDescending { it.dangerousPermissionsCount }
                    .take(5)
                    .map { app ->
                        val status = if (app.dangerousPermissionsCount >= 4)
                            AlertStatus.DANGER else AlertStatus.WARNING
                        val initials = app.appName.take(2).uppercase()
                        AppAlert(app.appName, "", status, initials)
                    }

                DashboardState(
                    serviceRunning = true,
                    monitoredAppsCount = apps.size,
                    connectionsToday = connectionsToday,
                    threatsBlocked = threatsBlockedToday,
                    needALookCount = needALook,
                    alerts = alerts
                )
            }.collect { newState ->
                _state.value = newState
            }
        }
    }
}