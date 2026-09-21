package com.example.secure_it_all.ui.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.secure_it_all.data.database.AppDatabase
import com.example.secure_it_all.data.database.NetworkConnectionEntity
import com.example.secure_it_all.data.repository.NetworkConnectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NetworkConnectionViewModel(
    db: AppDatabase
) : ViewModel() {

    private val repository =
        NetworkConnectionRepository(db.networkConnectionDao())

    val connections: Flow<List<NetworkConnectionEntity>> =
        repository.getRecentConnections()

    init {
        viewModelScope.launch {
            repository.startMockCollection()
        }
    }
}