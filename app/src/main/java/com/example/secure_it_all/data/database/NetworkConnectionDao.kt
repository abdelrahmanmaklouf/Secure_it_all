package com.example.secure_it_all.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NetworkConnectionDao {
    @Insert
    suspend fun insert(connection: NetworkConnectionEntity)

    @Query("SELECT * FROM network_connections ORDER BY timestamp DESC LIMIT 200")
    fun getRecentConnections(): Flow<List<NetworkConnectionEntity>>
}