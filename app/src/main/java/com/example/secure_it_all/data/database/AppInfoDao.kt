package com.example.secure_it_all.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(apps: List<AppInfoEntity>)

    @Query("SELECT * FROM app_info ORDER BY dangerousPermissionsCount DESC")
    fun getAllApps(): Flow<List<AppInfoEntity>>
}