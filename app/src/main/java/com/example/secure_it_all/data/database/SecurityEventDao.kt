package com.example.secure_it_all.data.database


import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SecurityEventDao {
    @Insert
    suspend fun insert(event: SecurityEventEntity)

    @Query("SELECT * FROM security_events ORDER BY timestamp DESC")
    fun getAllEvents(): Flow<List<SecurityEventEntity>>

    @Query("SELECT COUNT(DISTINCT packageName) FROM security_events")
    suspend fun getMonitoredAppCount(): Int
}