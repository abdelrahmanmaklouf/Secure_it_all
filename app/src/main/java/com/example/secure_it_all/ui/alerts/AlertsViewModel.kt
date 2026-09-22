package com.example.secure_it_all.ui.alerts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.secure_it_all.data.database.AppDatabase
import com.example.secure_it_all.data.database.SecurityEventEntity
import kotlinx.coroutines.flow.Flow

class AlertsViewModel(app: Application) : AndroidViewModel(app) {

    private val db = AppDatabase.getInstance(app)
    private val dao = db.securityEventDao()

    val events: Flow<List<SecurityEventEntity>> = dao.getAllEvents()
}