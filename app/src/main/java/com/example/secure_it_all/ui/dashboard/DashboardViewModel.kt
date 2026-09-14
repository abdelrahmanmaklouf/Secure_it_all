package com.example.secure_it_all.ui.dashboard


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.secure_it_all.data.database.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class DashboardState(
    val serviceRunning: Boolean = true,
    val monitoredAppsCount: Int = 0
)

class DashboardViewModel(private val db: AppDatabase) : ViewModel() {
    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state

    init {
        viewModelScope.launch {
            val count = db.securityEventDao().getMonitoredAppCount()
            _state.value = _state.value.copy(monitoredAppsCount = count)
        }
    }
}